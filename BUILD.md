# Build Instructions

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Quick Start

### 1. Build the Project

```bash
mvn clean package
```

This will:
- Compile all Java source files
- Run tests (use `-DskipTests` to skip)
- Create an executable JAR with all dependencies in `target/bitbucket-mcp-server-1.0.0.jar`

### 2. Verify the Build

```bash
java -jar target/bitbucket-mcp-server-1.0.0.jar
```

You should see an error message about missing credentials (expected if not configured).

### 3. Test with Configuration

```bash
export BITBUCKET_USERNAME="your-username"
export BITBUCKET_APP_PASSWORD="your-app-password"
export BITBUCKET_WORKSPACE="your-workspace"

echo '{"jsonrpc":"2.0","method":"initialize","params":{},"id":1}' | java -jar target/bitbucket-mcp-server-1.0.0.jar
```

## Development

### Run Tests

```bash
mvn test
```

### Clean Build

```bash
mvn clean
```

### Generate Sources JAR

```bash
mvn source:jar
```

### Generate Javadocs

```bash
mvn javadoc:javadoc
```

## IDE Setup

### IntelliJ IDEA

1. Open the project folder in IntelliJ
2. IntelliJ will automatically detect the Maven project
3. Wait for Maven to download dependencies
4. Run configuration:
   - Main class: `com.bitbucket.mcp.BitbucketMCPServer`
   - Set environment variables for credentials

### Eclipse

1. Import as Maven project
2. Right-click project → Maven → Update Project
3. Run as Java Application
   - Main class: `com.bitbucket.mcp.BitbucketMCPServer`

### VS Code

1. Install Java Extension Pack
2. Open folder
3. VS Code will detect Maven project
4. Use the Run and Debug panel

## Troubleshooting Build Issues

### Maven Not Found

Install Maven:
```bash
# macOS
brew install maven

# Ubuntu/Debian
sudo apt-get install maven

# Windows
# Download from https://maven.apache.org/download.cgi
```

### Java Version Issues

Ensure Java 17+:
```bash
java -version
```

Set JAVA_HOME if needed:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)  # macOS
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk     # Linux
```

### Dependency Download Failures

Clear Maven cache and retry:
```bash
rm -rf ~/.m2/repository
mvn clean install
```

### OutOfMemoryError During Build

Increase Maven memory:
```bash
export MAVEN_OPTS="-Xmx2g"
mvn clean package
```

## Advanced Build Options

### Custom Output Directory

```bash
mvn package -DoutputDirectory=/custom/path
```

### Skip Specific Plugins

```bash
mvn package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
```

### Build Specific Profile

```bash
mvn package -P production
```

### Create Distribution Package

```bash
mvn assembly:single
```

This creates a full distribution package with scripts and documentation.

## Continuous Integration

### GitHub Actions Example

```yaml
name: Build

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Build with Maven
      run: mvn clean package
    - name: Upload JAR
      uses: actions/upload-artifact@v3
      with:
        name: bitbucket-mcp-server
        path: target/*.jar
```

## Release Process

1. Update version in `pom.xml`
2. Build and test:
   ```bash
   mvn clean verify
   ```
3. Create git tag:
   ```bash
   git tag -a v1.0.0 -m "Release version 1.0.0"
   git push origin v1.0.0
   ```
4. Build final release:
   ```bash
   mvn clean package
   ```
5. Upload `target/bitbucket-mcp-server-1.0.0.jar` to releases
