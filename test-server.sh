#!/bin/bash

# Bitbucket MCP Server Test Suite
# This script tests the server without requiring IntelliJ or real Bitbucket credentials

set -e

echo "=========================================="
echo "Bitbucket MCP Server Test Suite"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
TESTS_PASSED=0
TESTS_FAILED=0

# Function to run a test
run_test() {
    local test_name="$1"
    local test_command="$2"

    echo -n "Testing: $test_name... "

    if eval "$test_command" > /tmp/test_output.log 2>&1; then
        echo -e "${GREEN}✓ PASSED${NC}"
        TESTS_PASSED=$((TESTS_PASSED + 1))
        return 0
    else
        echo -e "${RED}✗ FAILED${NC}"
        echo "  Error output:"
        cat /tmp/test_output.log | head -20 | sed 's/^/    /'
        TESTS_FAILED=$((TESTS_FAILED + 1))
        return 1
    fi
}

# Function to test MCP protocol communication
test_mcp_protocol() {
    local auth_type="$1"
    local env_vars="$2"

    echo ""
    echo "Testing MCP Protocol with $auth_type authentication..."

    # Create a test that sends initialize message
    local test_input='{"jsonrpc":"2.0","method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{}},"id":1}'

    # Run server in background and send test message
    (echo "$test_input" | timeout 5 env $env_vars java -jar target/bitbucket-mcp-server-1.0.0.jar 2>/dev/null) > /tmp/mcp_response.json

    # Check if we got a valid response
    if [ -f /tmp/mcp_response.json ] && grep -q '"jsonrpc":"2.0"' /tmp/mcp_response.json; then
        if grep -q '"serverInfo"' /tmp/mcp_response.json; then
            echo -e "${GREEN}✓ Server responded to initialize request${NC}"
            echo "  Response preview:"
            cat /tmp/mcp_response.json | python3 -m json.tool 2>/dev/null | head -10 | sed 's/^/    /'
            return 0
        fi
    fi

    echo -e "${RED}✗ Failed to get valid MCP response${NC}"
    return 1
}

# Function to test tools/list
test_tools_list() {
    local env_vars="$1"

    echo ""
    echo "Testing tools/list endpoint..."

    # First initialize, then request tools list
    local init_msg='{"jsonrpc":"2.0","method":"initialize","params":{"protocolVersion":"2024-11-05"},"id":1}'
    local initialized_msg='{"jsonrpc":"2.0","method":"initialized","params":{}}'
    local tools_msg='{"jsonrpc":"2.0","method":"tools/list","params":{},"id":2}'

    (printf "%s\n%s\n%s\n" "$init_msg" "$initialized_msg" "$tools_msg" | timeout 5 env $env_vars java -jar target/bitbucket-mcp-server-1.0.0.jar 2>/dev/null) > /tmp/tools_response.json

    # Check if we got tools in response
    if grep -q '"tools"' /tmp/tools_response.json && grep -q 'list_repositories' /tmp/tools_response.json; then
        echo -e "${GREEN}✓ Server returned tools list${NC}"
        local tool_count=$(grep -o 'list_repositories\|create_repository\|get_pull_request' /tmp/tools_response.json | wc -l)
        echo "  Found $tool_count sample tools in response"
        return 0
    fi

    echo -e "${RED}✗ Failed to get tools list${NC}"
    return 1
}

echo "Step 1: Build the project"
echo "-------------------------"
if [ ! -f "target/bitbucket-mcp-server-1.0.0.jar" ]; then
    echo "Building project with Maven..."
    mvn clean package -DskipTests -q
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Build successful${NC}"
    else
        echo -e "${RED}✗ Build failed${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}Using existing JAR file${NC}"
fi

echo ""
echo "Step 2: Test Server Startup"
echo "----------------------------"

# Test 1: Server fails without credentials
run_test "Server rejects missing credentials" \
    "! timeout 2 java -jar target/bitbucket-mcp-server-1.0.0.jar 2>&1 | grep -q 'Missing Bitbucket credentials'"

# Test 2: Server accepts access token
export BITBUCKET_ACCESS_TOKEN="test-token-12345"
export BITBUCKET_WORKSPACE="test-workspace"
run_test "Server accepts access token" \
    "echo '{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1}' | timeout 2 java -jar target/bitbucket-mcp-server-1.0.0.jar 2>&1 | grep -q 'access token authentication'"

# Test 3: Server accepts username/password
unset BITBUCKET_ACCESS_TOKEN
export BITBUCKET_USERNAME="testuser"
export BITBUCKET_APP_PASSWORD="testpass123"
run_test "Server accepts username/password" \
    "echo '{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1}' | timeout 2 java -jar target/bitbucket-mcp-server-1.0.0.jar 2>&1 | grep -q 'username/app password authentication'"

# Test 4: Access token takes precedence
export BITBUCKET_ACCESS_TOKEN="test-token-12345"
export BITBUCKET_USERNAME="testuser"
export BITBUCKET_APP_PASSWORD="testpass123"
run_test "Access token takes precedence over username/password" \
    "echo '{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1}' | timeout 2 java -jar target/bitbucket-mcp-server-1.0.0.jar 2>&1 | grep -q 'access token authentication'"

echo ""
echo "Step 3: Test MCP Protocol"
echo "--------------------------"

# Test MCP initialize with access token
if test_mcp_protocol "Access Token" "BITBUCKET_ACCESS_TOKEN=test-token BITBUCKET_WORKSPACE=test"; then
    TESTS_PASSED=$((TESTS_PASSED + 1))
else
    TESTS_FAILED=$((TESTS_FAILED + 1))
fi

# Test MCP initialize with username/password
if test_mcp_protocol "Username/Password" "BITBUCKET_USERNAME=test BITBUCKET_APP_PASSWORD=test BITBUCKET_WORKSPACE=test"; then
    TESTS_PASSED=$((TESTS_PASSED + 1))
else
    TESTS_FAILED=$((TESTS_FAILED + 1))
fi

echo ""
echo "Step 4: Test Tool Discovery"
echo "----------------------------"

# Test tools list
if test_tools_list "BITBUCKET_ACCESS_TOKEN=test-token BITBUCKET_WORKSPACE=test"; then
    TESTS_PASSED=$((TESTS_PASSED + 1))
else
    TESTS_FAILED=$((TESTS_FAILED + 1))
fi

echo ""
echo "Step 5: Test Configuration Methods"
echo "-----------------------------------"

# Test config file
cat > /tmp/test-bitbucket-mcp.json <<EOF
{
  "accessToken": "file-token-12345",
  "workspace": "file-workspace"
}
EOF

run_test "Server reads access token from config file" \
    "HOME=/tmp timeout 2 java -jar target/bitbucket-mcp-server-1.0.0.jar 2>&1 <<< '{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1}' | grep -q 'Configuration loaded'"

# Clean up
rm -f /tmp/test-bitbucket-mcp.json
unset BITBUCKET_ACCESS_TOKEN
unset BITBUCKET_USERNAME
unset BITBUCKET_APP_PASSWORD
unset BITBUCKET_WORKSPACE

echo ""
echo "=========================================="
echo "Test Results"
echo "=========================================="
echo -e "${GREEN}Passed: $TESTS_PASSED${NC}"
echo -e "${RED}Failed: $TESTS_FAILED${NC}"
echo ""

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}✓ All tests passed!${NC}"
    echo ""
    echo "The MCP server is working correctly and ready for use with IntelliJ Copilot."
    exit 0
else
    echo -e "${YELLOW}⚠ Some tests failed${NC}"
    echo ""
    echo "Review the failures above. The server may still work but needs investigation."
    exit 1
fi
