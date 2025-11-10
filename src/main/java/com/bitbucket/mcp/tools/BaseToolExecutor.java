package com.bitbucket.mcp.tools;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.protocol.MCPTool;
import com.google.gson.JsonObject;

/**
 * Base class for tool executors with common functionality
 */
public abstract class BaseToolExecutor implements ToolExecutor {
    protected final BitbucketClient client;
    protected final MCPTool tool;

    protected BaseToolExecutor(BitbucketClient client, String name, String description) {
        this.client = client;
        this.tool = new MCPTool(name, description, createInputSchema());
    }

    @Override
    public MCPTool getTool() {
        return tool;
    }

    /**
     * Create the JSON schema for tool input parameters
     */
    protected abstract JsonObject createInputSchema();

    /**
     * Helper method to create a JSON schema for a tool
     */
    protected JsonObject createSchema() {
        JsonObject schema = new JsonObject();
        schema.addProperty("type", "object");
        schema.add("properties", new JsonObject());
        schema.add("required", new com.google.gson.JsonArray());
        return schema;
    }

    /**
     * Helper to add a string property to schema
     */
    protected void addStringProperty(JsonObject schema, String name, String description, boolean required) {
        JsonObject properties = schema.getAsJsonObject("properties");
        JsonObject property = new JsonObject();
        property.addProperty("type", "string");
        property.addProperty("description", description);
        properties.add(name, property);

        if (required) {
            schema.getAsJsonArray("required").add(name);
        }
    }

    /**
     * Helper to add a number property to schema
     */
    protected void addNumberProperty(JsonObject schema, String name, String description, boolean required) {
        JsonObject properties = schema.getAsJsonObject("properties");
        JsonObject property = new JsonObject();
        property.addProperty("type", "number");
        property.addProperty("description", description);
        properties.add(name, property);

        if (required) {
            schema.getAsJsonArray("required").add(name);
        }
    }

    /**
     * Helper to add an integer property to schema
     */
    protected void addIntegerProperty(JsonObject schema, String name, String description, boolean required) {
        JsonObject properties = schema.getAsJsonObject("properties");
        JsonObject property = new JsonObject();
        property.addProperty("type", "integer");
        property.addProperty("description", description);
        properties.add(name, property);

        if (required) {
            schema.getAsJsonArray("required").add(name);
        }
    }

    /**
     * Helper to add a boolean property to schema
     */
    protected void addBooleanProperty(JsonObject schema, String name, String description, boolean required) {
        JsonObject properties = schema.getAsJsonObject("properties");
        JsonObject property = new JsonObject();
        property.addProperty("type", "boolean");
        property.addProperty("description", description);
        properties.add(name, property);

        if (required) {
            schema.getAsJsonArray("required").add(name);
        }
    }

    /**
     * Helper to get a required string parameter
     */
    protected String getRequiredString(JsonObject args, String name) {
        if (!args.has(name)) {
            throw new IllegalArgumentException("Missing required parameter: " + name);
        }
        return args.get(name).getAsString();
    }

    /**
     * Helper to get an optional string parameter
     */
    protected String getOptionalString(JsonObject args, String name, String defaultValue) {
        if (!args.has(name) || args.get(name).isJsonNull()) {
            return defaultValue;
        }
        return args.get(name).getAsString();
    }

    /**
     * Helper to get a required integer parameter
     */
    protected int getRequiredInt(JsonObject args, String name) {
        if (!args.has(name)) {
            throw new IllegalArgumentException("Missing required parameter: " + name);
        }
        return args.get(name).getAsInt();
    }

    /**
     * Helper to get an optional integer parameter
     */
    protected Integer getOptionalInt(JsonObject args, String name, Integer defaultValue) {
        if (!args.has(name) || args.get(name).isJsonNull()) {
            return defaultValue;
        }
        return args.get(name).getAsInt();
    }

    /**
     * Helper to get an optional boolean parameter
     */
    protected Boolean getOptionalBoolean(JsonObject args, String name, Boolean defaultValue) {
        if (!args.has(name) || args.get(name).isJsonNull()) {
            return defaultValue;
        }
        return args.get(name).getAsBoolean();
    }
}
