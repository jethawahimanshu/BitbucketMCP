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
                logger.error("Invalid configuration. Please set BITBUCKET_USERNAME and BITBUCKET_APP_PASSWORD " +
                            "environment variables or create ~/.bitbucket-mcp.json configuration file.");
                System.err.println("Error: Missing Bitbucket credentials.");
                System.err.println("Please set environment variables:");
                System.err.println("  BITBUCKET_USERNAME=your-username");
                System.err.println("  BITBUCKET_APP_PASSWORD=your-app-password");
                System.err.println("  BITBUCKET_WORKSPACE=your-workspace (optional)");
                System.err.println();
                System.err.println("Or create ~/.bitbucket-mcp.json with:");
                System.err.println("{");
                System.err.println("  \"username\": \"your-username\",");
                System.err.println("  \"appPassword\": \"your-app-password\",");
                System.err.println("  \"workspace\": \"your-workspace\"");
                System.err.println("}");
                System.exit(1);
            }

            logger.info("Configuration loaded successfully");
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
