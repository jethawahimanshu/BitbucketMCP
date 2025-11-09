package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListWorkspacesTool extends BaseToolExecutor {
    public ListWorkspacesTool(BitbucketClient client) {
        super(client, "list_workspaces",
              "List all workspaces the authenticated user has access to.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        return client.get("/workspaces");
    }
}
