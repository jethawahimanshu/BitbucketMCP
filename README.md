# Bitbucket MCP Server

A **truly exhaustive** Model Context Protocol (MCP) server for Bitbucket Cloud, written in Java. This server enables AI assistants like GitHub Copilot in IntelliJ IDEA to interact with Bitbucket repositories, pull requests, pipelines, and more.

With **140+ comprehensive tools**, this server provides complete coverage of virtually every Bitbucket Cloud API operation.

## Features

This MCP server provides **truly exhaustive coverage** of Bitbucket Cloud operations with 140+ tools:

### 🗂️ Repository Operations (6 tools)
- List repositories in a workspace
- Get repository details
- Create new repositories
- Update repository settings
- Delete repositories
- Fork repositories

### 🔀 Pull Request Operations (24 tools)
- List pull requests (filter by state)
- Get pull request details
- Create, update, merge, decline pull requests
- Approve/unapprove pull requests
- Add/update/delete PR comments (including inline comments on specific lines)
- Get PR diffs, diffstat, commits, activity, statuses
- Add reviewers to pull requests
- Create/update/delete PR tasks
- Get PR as patch file

### 📝 Commit Operations (9 tools)
- List commits (by branch or all)
- Get commit details
- List commit statuses (CI/CD results)
- Create commit statuses
- Get commit diffs
- List and add commit comments (including inline)
- Get commit as patch file

### 🌿 Branch Operations (7 tools)
- List all branches
- Get branch details
- Create new branches
- Delete branches
- List/create/delete branch restrictions (protection rules)

### 🚀 Pipeline Operations (13 tools)
- List all pipelines (CI/CD runs)
- Get pipeline details
- Run/trigger pipelines
- Stop running pipelines
- Get pipeline steps and step logs
- List/create pipeline variables
- List/create/update/delete pipeline schedules (cron-based)

### 📦 Deployment Operations (6 tools)
- List deployments
- Get deployment details
- List/create/delete deployment environments

### 🔔 Webhook Operations (3 tools)
- List webhooks
- Create webhooks with event subscriptions
- Delete webhooks

### 👥 Workspace & Project Operations (8 tools)
- List workspaces
- Get workspace details
- List projects
- Create/update/delete projects
- List workspace members

### 📄 File/Source Operations (5 tools)
- Get file content from repository
- List directory contents
- Create and update files via API
- Get file commit history

### 🐛 Issue Operations (13 tools)
- List issues (filter by state/kind)
- Get issue details
- Create/update/delete issues
- List and add issue comments
- Watch/unwatch issues
- Vote/unvote on issues
- List issue watchers and voters

### 🔍 Search Operations (2 tools)
- Search repositories by name/description
- Search code within repositories

### 🏷️ Tag Operations (4 tools)
- List tags
- Create new tags
- Get tag details
- Delete tags

### 👤 User Operations (2 tools)
- Get current authenticated user
- Get user details by username

### 👀 Watchers & Forks Operations (4 tools)
- List repository watchers
- List repository forks
- Watch/unwatch repositories

### 👥 Default Reviewers (3 tools)
- List default reviewers
- Add/remove default reviewers

### 🔑 SSH & Deploy Keys (6 tools)
- List/create/delete user SSH keys
- List/create/delete repository deploy keys

### 📥 Downloads (2 tools)
- List repository downloads
- Delete downloads

### 📝 Snippets (5 tools)
- List snippets
- Get/create/update/delete snippets

### 🔧 Repository Variables (4 tools)
- List/create/update/delete repository-level pipeline variables (secure vars)

### 🎯 Milestones (4 tools)
- List/get/create/delete milestones for issues

### 📦 Components (4 tools)
- List/get/create/delete components for issue categorization

### 📌 Versions (4 tools)
- List/get/create/delete versions for issue tracking

## Requirements

- **Java 17 or higher**
- **Bitbucket Cloud account**
- **Authentication credentials** (choose one):
  - **Access Token** (OAuth 2.0 - Recommended) OR
  - **Username + App Password** (Basic Auth)
- **Maven** (for building from source)

## Installation

### 1. Build from Source

```bash
git clone <repository-url>
cd BitbucketMCP
mvn clean package
```

This creates an executable JAR: `target/bitbucket-mcp-server-1.0.0.jar`

### 2. Configure Bitbucket Credentials

You can authenticate using either **Access Token** (recommended) or **Username + App Password**.

#### Option A: Access Token (Recommended)

**Using Environment Variables:**
```bash
export BITBUCKET_ACCESS_TOKEN="your-access-token"
export BITBUCKET_WORKSPACE="your-default-workspace"  # Optional
```

**Using Configuration File:**

Create `~/.bitbucket-mcp.json`:
```json
{
  "accessToken": "your-access-token",
  "workspace": "your-default-workspace"
}
```

**How to create an Access Token:**
1. Go to **Bitbucket Settings** → **Personal settings** → **Access tokens**
2. Click **Create token**
3. Give it a name and select permissions:
   - **Repositories**: Read, Write, Admin
   - **Pull requests**: Read, Write
   - **Pipelines**: Read, Write
   - **Issues**: Read, Write
   - **Webhooks**: Read, Write
   - **Account**: Read
4. Copy the generated token immediately (it won't be shown again)

#### Option B: Username + App Password

**Using Environment Variables:**
```bash
export BITBUCKET_USERNAME="your-username"
export BITBUCKET_APP_PASSWORD="your-app-password"
export BITBUCKET_WORKSPACE="your-default-workspace"  # Optional
```

**Using Configuration File:**

Create `~/.bitbucket-mcp.json`:
```json
{
  "username": "your-bitbucket-username",
  "appPassword": "your-app-password",
  "workspace": "your-default-workspace"
}
```

**How to create an App Password:**
1. Go to **Bitbucket Settings** → **Personal settings** → **App passwords**
2. Click **Create app password**
3. Select required permissions:
   - **Repositories**: Read, Write, Admin
   - **Pull requests**: Read, Write
   - **Pipelines**: Read, Write, Edit variables
   - **Issues**: Read, Write
   - **Webhooks**: Read, Write
   - **Account**: Read
4. Copy the generated password

## Usage with IntelliJ IDEA & GitHub Copilot

### Configure IntelliJ IDEA

1. **Install GitHub Copilot** plugin in IntelliJ
2. **Create or edit** the MCP configuration file

#### For IntelliJ on macOS/Linux:
Create/edit `.vscode/mcp.json` in your home directory or project:

**Using Access Token (Recommended):**
```json
{
  "mcpServers": {
    "bitbucket": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/bitbucket-mcp-server-1.0.0.jar"
      ],
      "env": {
        "BITBUCKET_ACCESS_TOKEN": "your-access-token",
        "BITBUCKET_WORKSPACE": "your-workspace"
      }
    }
  }
}
```

**Using Username + App Password:**
```json
{
  "mcpServers": {
    "bitbucket": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/bitbucket-mcp-server-1.0.0.jar"
      ],
      "env": {
        "BITBUCKET_USERNAME": "your-username",
        "BITBUCKET_APP_PASSWORD": "your-app-password",
        "BITBUCKET_WORKSPACE": "your-workspace"
      }
    }
  }
}
```

#### For IntelliJ on Windows:

**Using Access Token (Recommended):**
```json
{
  "mcpServers": {
    "bitbucket": {
      "command": "java",
      "args": [
        "-jar",
        "C:\\path\\to\\bitbucket-mcp-server-1.0.0.jar"
      ],
      "env": {
        "BITBUCKET_ACCESS_TOKEN": "your-access-token",
        "BITBUCKET_WORKSPACE": "your-workspace"
      }
    }
  }
}
```

**Using Username + App Password:**
```json
{
  "mcpServers": {
    "bitbucket": {
      "command": "java",
      "args": [
        "-jar",
        "C:\\path\\to\\bitbucket-mcp-server-1.0.0.jar"
      ],
      "env": {
        "BITBUCKET_USERNAME": "your-username",
        "BITBUCKET_APP_PASSWORD": "your-app-password",
        "BITBUCKET_WORKSPACE": "your-workspace"
      }
    }
  }
}
```

3. **Restart IntelliJ IDEA**
4. **Open GitHub Copilot Chat** and verify the Bitbucket tools are available

### Using with GitHub Copilot

Once configured, you can interact with Bitbucket using natural language in GitHub Copilot Chat:

#### Example Queries:

```
List all repositories in my workspace

Show me open pull requests for my-repo

Create a new pull request from feature-branch to main

Get the details of commit abc123

List all branches in my-repo

Run the pipeline for the main branch

Search for TODO comments in my-repo

Create a new issue titled "Fix login bug"

Get the content of src/main.py file

List all recent commits on the develop branch
```

## Available Tools

The server exposes **50+ MCP tools** covering all major Bitbucket operations:

| Category | Tools |
|----------|-------|
| Repositories | `list_repositories`, `get_repository`, `create_repository`, `delete_repository`, `fork_repository` |
| Pull Requests | `list_pull_requests`, `get_pull_request`, `create_pull_request`, `update_pull_request`, `merge_pull_request`, `decline_pull_request`, `approve_pull_request`, `unapprove_pull_request`, `list_pr_comments`, `add_pr_comment` |
| Commits | `list_commits`, `get_commit`, `list_commit_statuses`, `create_commit_status` |
| Branches | `list_branches`, `get_branch`, `create_branch`, `delete_branch` |
| Pipelines | `list_pipelines`, `get_pipeline`, `run_pipeline`, `stop_pipeline` |
| Deployments | `list_deployments`, `get_deployment` |
| Webhooks | `list_webhooks`, `create_webhook`, `delete_webhook` |
| Workspaces | `list_workspaces`, `get_workspace` |
| Projects | `list_projects`, `create_project` |
| Files | `get_file_content`, `list_directory` |
| Issues | `list_issues`, `get_issue`, `create_issue` |
| Search | `search_repositories`, `search_code` |
| Tags | `list_tags`, `create_tag` |
| Users | `get_current_user`, `get_user` |

## Architecture

```
┌─────────────────────────────────────┐
│   IntelliJ IDEA + GitHub Copilot    │
└──────────────┬──────────────────────┘
               │ MCP Protocol (JSON-RPC 2.0)
               │ STDIO Transport
┌──────────────┴──────────────────────┐
│   Bitbucket MCP Server (Java)       │
│   ┌─────────────────────────────┐   │
│   │  MCP Server Core            │   │
│   │  - Protocol Handler         │   │
│   │  - Tool Registry            │   │
│   └──────────┬──────────────────┘   │
│              │                       │
│   ┌──────────┴──────────────────┐   │
│   │  Bitbucket API Client       │   │
│   │  - HTTP Client (OkHttp)     │   │
│   │  - Authentication           │   │
│   └──────────┬──────────────────┘   │
└──────────────┼──────────────────────┘
               │ HTTPS
┌──────────────┴──────────────────────┐
│   Bitbucket Cloud REST API v2       │
└─────────────────────────────────────┘
```

## Configuration Reference

### Environment Variables

| Variable | Required | Description |
|----------|----------|-------------|
| `BITBUCKET_ACCESS_TOKEN` | Yes* | Bitbucket access token (OAuth 2.0) |
| `BITBUCKET_USERNAME` | Yes** | Your Bitbucket username |
| `BITBUCKET_APP_PASSWORD` | Yes** | Bitbucket app password |
| `BITBUCKET_WORKSPACE` | No | Default workspace slug |
| `BITBUCKET_BASE_URL` | No | API base URL (default: https://api.bitbucket.org/2.0) |

\* Required if not using username/app password
\** Required if not using access token

### Configuration File Format

File location: `~/.bitbucket-mcp.json`

**Using Access Token (Recommended):**
```json
{
  "accessToken": "your-access-token",
  "workspace": "your-workspace (optional)",
  "baseUrl": "https://api.bitbucket.org/2.0 (optional)"
}
```

**Using Username + App Password:**
```json
{
  "username": "your-username",
  "appPassword": "your-app-password",
  "workspace": "your-workspace (optional)",
  "baseUrl": "https://api.bitbucket.org/2.0 (optional)"
}
```

## Logging

Logs are written to:
- **Console (stderr)**: Warnings and errors
- **File**: `~/.bitbucket-mcp.log` (all log levels)

To change log levels, edit `src/main/resources/logback.xml`

## Troubleshooting

### "Invalid configuration" error

**Solution**: Ensure credentials are set via environment variables or `~/.bitbucket-mcp.json`

### "401 Unauthorized" errors

**Solution**:
1. Verify your access token or app password is correct and not expired
2. Ensure the token/password has required permissions
3. If using username/password, check that your username is correct
4. Try regenerating your access token or app password

### Tools not appearing in GitHub Copilot

**Solution**:
1. Verify `mcp.json` is in the correct location
2. Check that the JAR path is absolute and correct
3. Restart IntelliJ IDEA
4. Check GitHub Copilot MCP settings

### Network errors

**Solution**:
1. Check internet connectivity
2. Verify you can access https://api.bitbucket.org
3. Check for proxy settings if behind corporate firewall

## Development

### Project Structure

```
BitbucketMCP/
├── src/main/java/com/bitbucket/mcp/
│   ├── BitbucketMCPServer.java     # Main entry point
│   ├── server/
│   │   └── MCPServer.java          # MCP protocol server
│   ├── protocol/                   # MCP protocol classes
│   ├── bitbucket/
│   │   └── BitbucketClient.java    # Bitbucket API client
│   ├── config/
│   │   └── BitbucketConfig.java    # Configuration management
│   └── tools/
│       ├── ToolRegistry.java       # Tool registration
│       ├── ToolExecutor.java       # Tool interface
│       ├── BaseToolExecutor.java   # Base implementation
│       └── impl/                   # Tool implementations
├── src/main/resources/
│   └── logback.xml                 # Logging configuration
├── pom.xml                         # Maven configuration
└── README.md
```

### Adding New Tools

1. Create a new class in `tools/impl/` extending `BaseToolExecutor`
2. Implement `createInputSchema()` and `execute()` methods
3. Register the tool in `ToolRegistry.registerAllTools()`

Example:

```java
public class MyCustomTool extends BaseToolExecutor {
    public MyCustomTool(BitbucketClient client) {
        super(client, "my_custom_tool", "Description of what it does");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "param1", "Parameter description", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String param1 = getRequiredString(arguments, "param1");
        // Implementation
        return client.get("/some/endpoint");
    }
}
```

## API Coverage

This MCP server implements **comprehensive coverage** of Bitbucket Cloud REST API v2:

✅ Repositories
✅ Pull Requests
✅ Commits & Commit Statuses
✅ Branches
✅ Tags
✅ Pipelines (CI/CD)
✅ Deployments
✅ Webhooks
✅ Issues
✅ Workspaces
✅ Projects
✅ File/Source browsing
✅ Search (repositories and code)
✅ Users

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes with tests
4. Submit a pull request

## License

[Specify your license here]

## Support

For issues, questions, or contributions:
- Open an issue on GitHub
- Check the [Bitbucket API documentation](https://developer.atlassian.com/cloud/bitbucket/rest/)
- Review the [MCP specification](https://modelcontextprotocol.io/specification/latest)

## Acknowledgments

- Built on the [Model Context Protocol](https://modelcontextprotocol.io/) by Anthropic
- Uses [Bitbucket Cloud REST API v2](https://developer.atlassian.com/cloud/bitbucket/rest/)
- Powered by [OkHttp](https://square.github.io/okhttp/) and [Gson](https://github.com/google/gson)

---

**Made with ❤️ for the developer community**
