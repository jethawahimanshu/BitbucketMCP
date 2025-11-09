package com.bitbucket.mcp;

import com.bitbucket.mcp.config.BitbucketConfig;
import com.bitbucket.mcp.server.MCPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for Bitbucket MCP Server
 */
public class BitbucketMCPServer {
    private static final Logger logger = LoggerFactory.getLogger(BitbucketMCPServer.class);

    public static void main(String[] args) {
        try {
            // Load configuration
            BitbucketConfig config = BitbucketConfig.load();

            // Validate configuration
            if (!config.isValid()) {
                logger.error("Invalid configuration. Please provide authentication credentials.");
                System.err.println("Error: Missing Bitbucket credentials.");
                System.err.println();
                System.err.println("Option 1: Use Access Token (Recommended)");
                System.err.println("  Set environment variable:");
                System.err.println("    BITBUCKET_ACCESS_TOKEN=your-access-token");
                System.err.println("    BITBUCKET_WORKSPACE=your-workspace (optional)");
                System.err.println();
                System.err.println("Option 2: Use Username + App Password");
                System.err.println("  Set environment variables:");
                System.err.println("    BITBUCKET_USERNAME=your-username");
                System.err.println("    BITBUCKET_APP_PASSWORD=your-app-password");
                System.err.println("    BITBUCKET_WORKSPACE=your-workspace (optional)");
                System.err.println();
                System.err.println("Option 3: Configuration File");
                System.err.println("  Create ~/.bitbucket-mcp.json with:");
                System.err.println("  {");
                System.err.println("    \"accessToken\": \"your-access-token\",");
                System.err.println("    \"workspace\": \"your-workspace\"");
                System.err.println("  }");
                System.err.println();
                System.err.println("  OR");
                System.err.println("  {");
                System.err.println("    \"username\": \"your-username\",");
                System.err.println("    \"appPassword\": \"your-app-password\",");
                System.err.println("    \"workspace\": \"your-workspace\"");
                System.err.println("  }");
                System.exit(1);
            }

            logger.info("Configuration loaded successfully");
            logger.info("Authentication method: {}", config.getAuthType());
            if (config.getWorkspace() != null) {
                logger.info("Default workspace: {}", config.getWorkspace());
            }

            // Create and start server
            MCPServer server = new MCPServer(config);
            server.start();

        } catch (Exception e) {
            logger.error("Fatal error", e);
            System.err.println("Fatal error: " + e.getMessage());
            System.exit(1);
        }
    }
}
