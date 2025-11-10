package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteSSHKeyTool extends BaseToolExecutor {
    public DeleteSSHKeyTool(BitbucketClient client) {
        super(client, "delete_ssh_key",
              "Delete an SSH key from the authenticated user's account.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "key_uuid", "SSH key UUID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String keyUuid = getRequiredString(arguments, "key_uuid");
        return client.delete("/user/ssh-keys/" + keyUuid);
    }
}
