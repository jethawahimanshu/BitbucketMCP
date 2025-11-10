package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdateProjectTool extends BaseToolExecutor {
    public UpdateProjectTool(BitbucketClient client) {
        super(client, "update_project",
              "Update a project's name, description, or privacy settings.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "project_key", "Project key", true);
        addStringProperty(schema, "name", "New project name", false);
        addStringProperty(schema, "description", "New description", false);
        addBooleanProperty(schema, "is_private", "Make project private/public", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String projectKey = getRequiredString(arguments, "project_key");

        JsonObject body = new JsonObject();
        if (arguments.has("name")) {
            body.addProperty("name", arguments.get("name").getAsString());
        }
        if (arguments.has("description")) {
            body.addProperty("description", arguments.get("description").getAsString());
        }
        if (arguments.has("is_private")) {
            body.addProperty("is_private", arguments.get("is_private").getAsBoolean());
        }

        return client.put("/workspaces/" + workspace + "/projects/" + projectKey, body);
    }
}
