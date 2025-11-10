package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteProjectTool extends BaseToolExecutor {
    public DeleteProjectTool(BitbucketClient client) {
        super(client, "delete_project",
              "Delete a project from a workspace.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "project_key", "Project key", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String projectKey = getRequiredString(arguments, "project_key");

        return client.delete("/workspaces/" + workspace + "/projects/" + projectKey);
    }
}
