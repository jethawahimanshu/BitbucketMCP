package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListSSHKeysTool extends BaseToolExecutor {
    public ListSSHKeysTool(BitbucketClient client) {
        super(client, "list_ssh_keys",
              "List all SSH keys for the authenticated user.");
    }

    @Override
    protected JsonObject createInputSchema() {
        return createSchema();
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        return client.get("/user/ssh-keys");
    }
}
