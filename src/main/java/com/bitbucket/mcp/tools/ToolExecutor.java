package com.bitbucket.mcp.tools;

import com.bitbucket.mcp.protocol.MCPTool;
import com.google.gson.JsonObject;

/**
 * Interface for tool executors
 */
public interface ToolExecutor {
    /**
     * Get the tool definition
     */
    MCPTool getTool();

    /**
     * Execute the tool with given arguments
     */
    JsonObject execute(JsonObject arguments) throws Exception;
}
