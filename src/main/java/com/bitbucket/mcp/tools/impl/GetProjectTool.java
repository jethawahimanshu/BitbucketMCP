package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetProjectTool extends BaseToolExecutor {
    public GetProjectTool(BitbucketClient client) {
        super(client, "get_project",
              "Get details about a specific project.");
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

        return client.get("/workspaces/" + workspace + "/projects/" + projectKey);
    }
}
