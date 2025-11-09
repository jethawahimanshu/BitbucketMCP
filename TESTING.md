# Testing Guide

## What Can Be Tested in This Environment ✅

### 1. **MCP Protocol Compliance** ✅
I can fully test:
- JSON-RPC 2.0 message handling
- Initialize/initialized handshake
- Tools discovery (`tools/list`)
- Tool execution protocol (`tools/call`)
- Error handling and responses
- Server startup and configuration

**How to test:**
```bash
./test-server.sh
```

### 2. **Authentication Logic** ✅
I can verify:
- Access token authentication setup
- Username/password authentication setup
- Priority handling (access token > username/password)
- Configuration loading from environment variables
- Configuration loading from JSON file
- Credential validation

**How to test:**
```bash
# Test with access token
export BITBUCKET_ACCESS_TOKEN="test-token"
echo '{"jsonrpc":"2.0","method":"initialize","id":1}' | java -jar target/bitbucket-mcp-server-1.0.0.jar

# Check logs for: "Using access token authentication"
```

### 3. **Tool Registration** ✅
I can confirm:
- All 50+ tools are registered
- Tool schemas are valid
- Tool names and descriptions are correct

**How to test:**
```bash
./interactive-test.sh
```

### 4. **Build and Compilation** ✅
I can verify:
- Maven build succeeds
- JAR is created correctly
- All dependencies are included
- No compilation errors

**How to test:**
```bash
mvn clean package
```

---

## What CANNOT Be Tested Here ❌

### 1. **Real Bitbucket API Calls** ❌
**Why:** Requires real Bitbucket credentials and network access to Bitbucket Cloud

**What's missing:**
- Actual API authentication with Bitbucket
- Real repository operations
- Actual pull request management
- Live webhook configuration

**Where to test:** You'll need to test with your real Bitbucket account

### 2. **IntelliJ IDEA Integration** ❌
**Why:** Cannot install and run IntelliJ IDEA in this environment

**What's missing:**
- IntelliJ IDEA installed
- GitHub Copilot extension
- Actual IDE interaction
- MCP configuration in IntelliJ
- Visual UI testing

**Where to test:** Your local development machine with IntelliJ installed

### 3. **GitHub Copilot Extension** ❌
**Why:** GitHub Copilot requires authentication and runs as IntelliJ plugin

**What's missing:**
- GitHub Copilot authentication
- Natural language processing
- AI-powered tool selection
- Conversation context
- Real user queries

**Where to test:** Your IntelliJ with GitHub Copilot configured

---

## Complete Testing Strategy

### Phase 1: Automated Testing (HERE) ✅

**What I can do:**
```bash
# 1. Build the project
mvn clean package

# 2. Run automated tests
./test-server.sh

# 3. Run interactive protocol test
./interactive-test.sh

# 4. Verify tool count
echo '{"jsonrpc":"2.0","method":"tools/list","id":1}' | \
  BITBUCKET_ACCESS_TOKEN=test java -jar target/bitbucket-mcp-server-1.0.0.jar | \
  grep -o '"name"' | wc -l
```

**Results:**
- ✅ MCP protocol works correctly
- ✅ Authentication methods work
- ✅ 50+ tools are registered
- ✅ Server starts and responds

### Phase 2: Local Integration Testing (YOUR MACHINE) 🖥️

**You'll need to:**

1. **Copy the built JAR to your machine:**
   ```bash
   scp target/bitbucket-mcp-server-1.0.0.jar your-machine:/path/to/
   ```

2. **Create Bitbucket credentials:**
   - Get an access token from Bitbucket Cloud
   - Or create username + app password

3. **Configure IntelliJ:**
   - Install GitHub Copilot extension
   - Create `.vscode/mcp.json` with server configuration
   - Add your real Bitbucket credentials

4. **Test in IntelliJ:**
   ```
   # Open GitHub Copilot Chat
   # Try: "List my Bitbucket repositories"
   # Try: "Show open pull requests in my-repo"
   # Try: "Create a pull request from feature to main"
   ```

### Phase 3: Real-World Testing (PRODUCTION) 🚀

**Test actual operations:**
1. List real repositories
2. Create a test pull request
3. Add PR comments
4. Run a pipeline
5. Search code
6. Create an issue

---

## Test Scripts Provided

### 1. `test-server.sh` - Automated Test Suite
**Tests:**
- Server startup
- Authentication methods
- Configuration loading
- MCP protocol compliance
- Tool discovery
- Error handling

**Usage:**
```bash
./test-server.sh
```

**Output:**
- Green ✓ for passed tests
- Red ✗ for failed tests
- Summary with pass/fail counts

### 2. `interactive-test.sh` - Interactive Protocol Test
**Simulates:**
- GitHub Copilot's MCP handshake
- Initialize sequence
- Tool discovery
- Tool execution

**Usage:**
```bash
./interactive-test.sh
```

**Output:**
- Step-by-step protocol simulation
- Server responses
- Tool listing

### 3. Manual Testing
**Quick protocol test:**
```bash
# Start server with test credentials
export BITBUCKET_ACCESS_TOKEN="test-token"

# Send initialize message
echo '{"jsonrpc":"2.0","method":"initialize","params":{},"id":1}' | \
  java -jar target/bitbucket-mcp-server-1.0.0.jar

# Expected: Server info response
```

---

## Testing Checklist

### Before Local Testing ✅
- [ ] Build succeeds: `mvn clean package`
- [ ] Test suite passes: `./test-server.sh`
- [ ] Interactive test works: `./interactive-test.sh`
- [ ] JAR file created: `ls -lh target/bitbucket-mcp-server-1.0.0.jar`

### For Local Testing 📝
- [ ] IntelliJ IDEA installed
- [ ] GitHub Copilot extension installed
- [ ] Bitbucket account created
- [ ] Access token or app password created
- [ ] `mcp.json` configured
- [ ] Server JAR copied to local machine

### During Local Testing 🔍
- [ ] Server starts without errors
- [ ] Copilot sees "bitbucket" server
- [ ] Copilot shows 50+ tools available
- [ ] Can query: "List my repositories"
- [ ] Can execute: "Get pull requests"
- [ ] Authentication works
- [ ] Tools return valid responses

### Troubleshooting Checklist 🔧
- [ ] Check logs: `~/.bitbucket-mcp.log`
- [ ] Verify credentials are correct
- [ ] Confirm MCP config path is right
- [ ] Restart IntelliJ after config changes
- [ ] Check network connectivity to Bitbucket
- [ ] Verify token/password permissions

---

## Expected Test Results

### Automated Tests (Here)
```
==========================================
Test Results
==========================================
Passed: 8-10
Failed: 0

✓ All tests passed!
```

### Local Integration (Your Machine)
```
IntelliJ GitHub Copilot Chat:
> List my Bitbucket repositories

MCP Server executes: list_repositories
Returns: { repositories: [...], size: 25 }

Copilot: "You have 25 repositories in your workspace..."
```

---

## Limitations of Automated Testing

### What We Know Works ✅
1. **Protocol**: MCP JSON-RPC implementation is correct
2. **Authentication**: Both methods detect and configure properly
3. **Tools**: All 50+ tools are registered with valid schemas
4. **Server**: Starts, responds, handles errors

### What We Can't Verify ❌
1. **Real API calls**: Need actual Bitbucket credentials
2. **Network issues**: Firewall, proxy, connectivity
3. **User experience**: Natural language → tool selection
4. **Edge cases**: Specific Bitbucket API quirks
5. **Performance**: Real-world latency and throughput

---

## Next Steps

### In This Environment:
```bash
# Run all automated tests
./test-server.sh

# See interactive protocol simulation
./interactive-test.sh

# Check build artifacts
ls -lh target/
```

### On Your Local Machine:
1. Copy JAR file
2. Set up credentials
3. Configure IntelliJ MCP
4. Test with GitHub Copilot
5. Report any issues

---

## Getting Help

If automated tests **pass** here but **fail** locally:
- Check credentials are valid
- Verify network access to Bitbucket
- Review IntelliJ logs
- Check MCP configuration syntax

If automated tests **fail** here:
- Check Java version (need 17+)
- Verify Maven built successfully
- Review error messages in output
- Check file permissions

---

## Summary

| Testing Aspect | Can Test Here | Need Local Testing |
|----------------|---------------|-------------------|
| MCP Protocol | ✅ Yes | - |
| Authentication Logic | ✅ Yes | - |
| Tool Registration | ✅ Yes | - |
| Build/Compilation | ✅ Yes | - |
| Real Bitbucket API | ❌ No | ✅ Required |
| IntelliJ Integration | ❌ No | ✅ Required |
| GitHub Copilot | ❌ No | ✅ Required |
| End-to-End UX | ❌ No | ✅ Required |

**Bottom line:** I can verify the server is **technically correct** and **protocol-compliant**. You'll need to test the **real integration** with IntelliJ Copilot and Bitbucket on your machine.
