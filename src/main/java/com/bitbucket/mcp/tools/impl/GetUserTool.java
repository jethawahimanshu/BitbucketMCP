package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetUserTool extends BaseToolExecutor {
    public GetUserTool(BitbucketClient client) {
        super(client, "get_user",
              "Get information about a specific Bitbucket user by username.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "username", "Username to lookup", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String username = getRequiredString(arguments, "username");
        return client.get("/users/" + username);
    }
}
