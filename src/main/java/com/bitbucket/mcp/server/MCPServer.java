package com.bitbucket.mcp.server;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.config.BitbucketConfig;
import com.bitbucket.mcp.protocol.*;
import com.bitbucket.mcp.tools.ToolRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Main MCP Server that handles JSON-RPC communication over STDIO
 */
public class MCPServer {
    private static final Logger logger = LoggerFactory.getLogger(MCPServer.class);
    private static final String SERVER_NAME = "bitbucket-mcp-server";
    private static final String SERVER_VERSION = "1.0.0";

    private final Gson gson;
    private final ToolRegistry toolRegistry;
    private final BitbucketClient bitbucketClient;
    private boolean initialized = false;

    public MCPServer(BitbucketConfig config) {
        // Use compact JSON for MCP protocol (no pretty printing)
        // MCP expects single-line JSON-RPC messages for STDIO transport
        this.gson = new GsonBuilder().create();
        this.bitbucketClient = new BitbucketClient(config);
        this.toolRegistry = new ToolRegistry(bitbucketClient);
    }

    /**
     * Start the server and listen for JSON-RPC messages on STDIN
     */
    public void start() {
        logger.info("Starting {} v{}", SERVER_NAME, SERVER_VERSION);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    handleMessage(line);
                } catch (Exception e) {
                    logger.error("Error handling message", e);
                    sendError(null, JsonRpcError.INTERNAL_ERROR, "Internal error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Error reading from stdin", e);
        }

        logger.info("Server stopped");
    }

    /**
     * Handle incoming JSON-RPC message
     */
    private void handleMessage(String message) {
        logger.debug("Received message: {}", message);

        JsonRpcRequest request;
        try {
            request = gson.fromJson(message, JsonRpcRequest.class);
        } catch (Exception e) {
            logger.error("Failed to parse message", e);
            sendError(null, JsonRpcError.PARSE_ERROR, "Parse error");
            return;
        }

        if (request.getMethod() == null) {
            sendError(request.getId(), JsonRpcError.INVALID_REQUEST, "Missing method");
            return;
        }

        String method = request.getMethod();
        logger.debug("Handling method: {}", method);

        switch (method) {
            case "initialize":
                handleInitialize(request);
                break;
            case "initialized":
                handleInitialized(request);
                break;
            case "tools/list":
                handleToolsList(request);
                break;
            case "tools/call":
                handleToolsCall(request);
                break;
            case "resources/list":
                handleResourcesList(request);
                break;
            case "resources/read":
                handleResourcesRead(request);
                break;
            case "prompts/list":
                handlePromptsList(request);
                break;
            case "prompts/get":
                handlePromptsGet(request);
                break;
            default:
                sendError(request.getId(), JsonRpcError.METHOD_NOT_FOUND, "Method not found: " + method);
        }
    }

    /**
     * Handle initialize request
     */
    private void handleInitialize(JsonRpcRequest request) {
        ServerInfo serverInfo = new ServerInfo(SERVER_NAME, SERVER_VERSION);

        JsonObject result = new JsonObject();
        result.addProperty("protocolVersion", "2024-11-05");
        result.add("serverInfo", gson.toJsonTree(serverInfo));

        sendResponse(request.getId(), result);
    }

    /**
     * Handle initialized notification
     */
    private void handleInitialized(JsonRpcRequest request) {
        initialized = true;
        logger.info("Server initialized");
        // No response needed for notification
    }

    /**
     * Handle tools/list request
     */
    private void handleToolsList(JsonRpcRequest request) {
        List<MCPTool> tools = toolRegistry.getAllTools();

        JsonObject result = new JsonObject();
        result.add("tools", gson.toJsonTree(tools));

        sendResponse(request.getId(), result);
    }

    /**
     * Handle tools/call request
     */
    private void handleToolsCall(JsonRpcRequest request) {
        if (!initialized) {
            sendError(request.getId(), JsonRpcError.INTERNAL_ERROR, "Server not initialized");
            return;
        }

        JsonObject params = request.getParams().getAsJsonObject();
        String toolName = params.get("name").getAsString();
        JsonObject arguments = params.has("arguments") ? params.get("arguments").getAsJsonObject() : new JsonObject();

        logger.info("Calling tool: {}", toolName);

        try {
            JsonObject toolResult = toolRegistry.executeTool(toolName, arguments);

            JsonObject result = new JsonObject();
            result.add("content", gson.toJsonTree(List.of(Map.of(
                    "type", "text",
                    "text", gson.toJson(toolResult)
            ))));

            sendResponse(request.getId(), result);
        } catch (Exception e) {
            logger.error("Error executing tool: {}", toolName, e);
            sendError(request.getId(), JsonRpcError.INTERNAL_ERROR, "Tool execution failed: " + e.getMessage());
        }
    }

    /**
     * Handle resources/list request
     */
    private void handleResourcesList(JsonRpcRequest request) {
        // Return empty list for now - can be enhanced later
        JsonObject result = new JsonObject();
        result.add("resources", gson.toJsonTree(List.of()));
        sendResponse(request.getId(), result);
    }

    /**
     * Handle resources/read request
     */
    private void handleResourcesRead(JsonRpcRequest request) {
        sendError(request.getId(), JsonRpcError.METHOD_NOT_FOUND, "Resources not implemented yet");
    }

    /**
     * Handle prompts/list request
     */
    private void handlePromptsList(JsonRpcRequest request) {
        // Return empty list for now - can be enhanced later
        JsonObject result = new JsonObject();
        result.add("prompts", gson.toJsonTree(List.of()));
        sendResponse(request.getId(), result);
    }

    /**
     * Handle prompts/get request
     */
    private void handlePromptsGet(JsonRpcRequest request) {
        sendError(request.getId(), JsonRpcError.METHOD_NOT_FOUND, "Prompts not implemented yet");
    }

    /**
     * Send successful response
     */
    private void sendResponse(Object id, JsonObject result) {
        JsonRpcResponse response = new JsonRpcResponse(id, result);
        String json = gson.toJson(response);
        System.out.println(json);
        System.out.flush();
        logger.debug("Sent response: {}", json);
    }

    /**
     * Send error response
     */
    private void sendError(Object id, int code, String message) {
        JsonRpcError error = new JsonRpcError(code, message);
        JsonRpcResponse response = new JsonRpcResponse(id, error);
        String json = gson.toJson(response);
        System.out.println(json);
        System.out.flush();
        logger.debug("Sent error: {}", json);
    }

    // Helper class for Map creation
    private static class Map {
        public static java.util.Map<String, String> of(String k1, String v1, String k2, String v2) {
            java.util.Map<String, String> map = new java.util.HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            return map;
        }
    }
}
