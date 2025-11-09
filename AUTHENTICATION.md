# Authentication Guide

The Bitbucket MCP Server supports two authentication methods for connecting to Bitbucket Cloud API. You can choose the method that best fits your security and workflow requirements.

## Authentication Methods

### 1. Access Token (OAuth 2.0) - **Recommended** ✅

Access tokens provide secure, OAuth 2.0-based authentication with fine-grained permissions and easier token rotation.

#### Advantages
- ✅ **More Secure**: OAuth 2.0 standard
- ✅ **Token Rotation**: Easy to rotate without changing password
- ✅ **Fine-grained Permissions**: Precise control over API access
- ✅ **Easier Revocation**: Revoke specific tokens without affecting other integrations
- ✅ **No Password Exposure**: Your main password remains secure

#### How to Create an Access Token

1. Log into **Bitbucket Cloud**
2. Go to **Settings** → **Personal settings** → **Access tokens**
3. Click **Create token**
4. Configure the token:
   - **Name**: e.g., "MCP Server for IntelliJ"
   - **Expiration**: Choose based on your security policy
   - **Permissions**: Select required scopes:
     - ✅ Repositories: Read, Write, Admin
     - ✅ Pull requests: Read, Write
     - ✅ Pipelines: Read, Write
     - ✅ Issues: Read, Write
     - ✅ Webhooks: Read, Write
     - ✅ Account: Read
5. Click **Create**
6. **Copy the token immediately** (it won't be shown again!)

#### Configuration

**Environment Variable:**
```bash
export BITBUCKET_ACCESS_TOKEN="your-access-token-here"
export BITBUCKET_WORKSPACE="your-workspace"  # Optional
```

**Configuration File** (`~/.bitbucket-mcp.json`):
```json
{
  "accessToken": "your-access-token-here",
  "workspace": "your-workspace"
}
```

**MCP Configuration** (`.vscode/mcp.json`):
```json
{
  "mcpServers": {
    "bitbucket": {
      "command": "java",
      "args": ["-jar", "/path/to/bitbucket-mcp-server-1.0.0.jar"],
      "env": {
        "BITBUCKET_ACCESS_TOKEN": "your-access-token",
        "BITBUCKET_WORKSPACE": "your-workspace"
      }
    }
  }
}
```

---

### 2. Username + App Password (Basic Auth)

App passwords are application-specific passwords that work with Basic authentication.

#### When to Use
- Legacy integrations that require Basic Auth
- You prefer traditional username/password authentication
- Compatibility with older tools

#### How to Create an App Password

1. Log into **Bitbucket Cloud**
2. Go to **Settings** → **Personal settings** → **App passwords**
3. Click **Create app password**
4. Configure the password:
   - **Label**: e.g., "MCP Server"
   - **Permissions**: Select required permissions:
     - ✅ Repositories: Read, Write, Admin
     - ✅ Pull requests: Read, Write
     - ✅ Pipelines: Read, Write, Edit variables
     - ✅ Issues: Read, Write
     - ✅ Webhooks: Read, Write
     - ✅ Account: Read
5. Click **Create**
6. **Copy the password** (it won't be shown again!)

#### Configuration

**Environment Variables:**
```bash
export BITBUCKET_USERNAME="your-username"
export BITBUCKET_APP_PASSWORD="your-app-password"
export BITBUCKET_WORKSPACE="your-workspace"  # Optional
```

**Configuration File** (`~/.bitbucket-mcp.json`):
```json
{
  "username": "your-username",
  "appPassword": "your-app-password",
  "workspace": "your-workspace"
}
```

**MCP Configuration** (`.vscode/mcp.json`):
```json
{
  "mcpServers": {
    "bitbucket": {
      "command": "java",
      "args": ["-jar", "/path/to/bitbucket-mcp-server-1.0.0.jar"],
      "env": {
        "BITBUCKET_USERNAME": "your-username",
        "BITBUCKET_APP_PASSWORD": "your-app-password",
        "BITBUCKET_WORKSPACE": "your-workspace"
      }
    }
  }
}
```

---

## Comparison

| Feature | Access Token | Username + App Password |
|---------|-------------|------------------------|
| **Authentication Type** | OAuth 2.0 Bearer | Basic Auth |
| **Security** | ⭐⭐⭐⭐⭐ Modern OAuth | ⭐⭐⭐⭐ Traditional |
| **Token Rotation** | ✅ Easy | ⚠️ Must regenerate |
| **Granular Permissions** | ✅ Yes | ✅ Yes |
| **Password Protection** | ✅ Main password safe | ⚠️ Username exposed |
| **Revocation** | ✅ Per-token | ⚠️ All or nothing |
| **Expiration** | ✅ Configurable | ❌ No built-in expiry |
| **Recommended** | ✅ **Yes** | Only for legacy |

---

## Priority and Fallback

The server checks for credentials in this order:

1. **Environment Variables** (highest priority)
   - `BITBUCKET_ACCESS_TOKEN` if set, OR
   - `BITBUCKET_USERNAME` + `BITBUCKET_APP_PASSWORD`

2. **Configuration File** (`~/.bitbucket-mcp.json`)
   - `accessToken` field if present, OR
   - `username` + `appPassword` fields

If both methods are configured, **Access Token takes precedence**.

---

## Security Best Practices

### For Access Tokens ✅

1. **Store Securely**
   - Never commit tokens to version control
   - Use environment variables or secure config files
   - Set restrictive file permissions: `chmod 600 ~/.bitbucket-mcp.json`

2. **Rotate Regularly**
   - Create new tokens periodically
   - Revoke old tokens after rotation
   - Use token expiration when available

3. **Minimal Permissions**
   - Grant only required permissions
   - Review and audit token permissions regularly

4. **Monitor Usage**
   - Check Bitbucket access logs
   - Revoke suspicious tokens immediately

### For App Passwords

1. **Treat as Passwords**
   - Never share or expose
   - Store in secure locations only
   - Use password managers if needed

2. **Create Specific Passwords**
   - One app password per integration
   - Label clearly for identification
   - Revoke unused passwords

3. **Regular Audits**
   - Review active app passwords
   - Remove unused integrations
   - Update permissions as needed

---

## Troubleshooting Authentication

### 401 Unauthorized Error

**Possible Causes:**
- Token/password is incorrect or expired
- Insufficient permissions
- Token has been revoked

**Solutions:**
1. Verify the token/password is correct
2. Check token hasn't expired (for access tokens)
3. Verify all required permissions are granted
4. Try regenerating the token/password
5. Check Bitbucket account status

### Authentication Type Not Detected

**Symptoms:**
- Server fails to start
- "Invalid configuration" error

**Solutions:**
1. Check environment variables are set correctly:
   ```bash
   echo $BITBUCKET_ACCESS_TOKEN
   # OR
   echo $BITBUCKET_USERNAME
   echo $BITBUCKET_APP_PASSWORD
   ```

2. Verify config file exists and is readable:
   ```bash
   cat ~/.bitbucket-mcp.json
   ls -la ~/.bitbucket-mcp.json
   ```

3. Check file permissions:
   ```bash
   chmod 600 ~/.bitbucket-mcp.json
   ```

### Wrong Authentication Method Used

**Symptoms:**
- Server logs show unexpected auth type

**Solutions:**
1. Check server logs for authentication method:
   ```
   tail -f ~/.bitbucket-mcp.log
   ```

2. Logs will show: "Using access token authentication" or "Using username/app password authentication"

3. Clear conflicting environment variables if needed

---

## Migration Guide

### From Username/Password to Access Token

1. **Create an Access Token** (see instructions above)

2. **Update Environment Variables:**
   ```bash
   # Remove old variables
   unset BITBUCKET_USERNAME
   unset BITBUCKET_APP_PASSWORD

   # Add new variable
   export BITBUCKET_ACCESS_TOKEN="your-new-token"
   ```

3. **Update Configuration File:**
   ```json
   {
     "accessToken": "your-new-token",
     "workspace": "your-workspace"
   }
   ```

   Remove the `username` and `appPassword` fields.

4. **Update MCP Configuration:**
   Replace username/password env vars with access token in `.vscode/mcp.json`

5. **Restart the MCP Server**

6. **Verify Authentication:**
   Check logs to confirm: "Using access token authentication"

7. **Revoke Old App Password** (optional but recommended)

---

## FAQ

**Q: Can I use both authentication methods simultaneously?**
A: The server will use access token if both are configured (access token has priority).

**Q: Which method is more secure?**
A: Access tokens are recommended as they use OAuth 2.0 and offer better security features.

**Q: How do I know which authentication method is being used?**
A: Check the server logs (`~/.bitbucket-mcp.log`). It will log the authentication method on startup.

**Q: Can I have different tokens for different workspaces?**
A: Yes, configure separate MCP servers in your `mcp.json` with different tokens.

**Q: What happens if my token expires?**
A: API calls will fail with 401 errors. Create a new token and update your configuration.

**Q: Do access tokens support all API operations?**
A: Yes, access tokens support all Bitbucket API v2 operations when granted appropriate permissions.

---

## Support

For authentication issues:
1. Check the [Bitbucket API documentation](https://developer.atlassian.com/cloud/bitbucket/rest/)
2. Review server logs: `~/.bitbucket-mcp.log`
3. Open an issue on GitHub with relevant log excerpts (redact sensitive data)
