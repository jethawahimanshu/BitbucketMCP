package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetCurrentUserTool extends BaseToolExecutor {
    public GetCurrentUserTool(BitbucketClient client) {
        super(client, "get_current_user",
              "Get information about the currently authenticated user.");
    }

    @Override
    protected JsonObject createInputSchema() {
        return createSchema();
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        return client.get("/user");
    }
}
