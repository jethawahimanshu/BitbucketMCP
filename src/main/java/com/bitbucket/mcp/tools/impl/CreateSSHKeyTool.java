package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateSSHKeyTool extends BaseToolExecutor {
    public CreateSSHKeyTool(BitbucketClient client) {
        super(client, "create_ssh_key",
              "Add a new SSH key to the authenticated user's account.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "label", "SSH key label/name", true);
        addStringProperty(schema, "key", "SSH public key content", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String label = getRequiredString(arguments, "label");
        String key = getRequiredString(arguments, "key");

        JsonObject body = new JsonObject();
        body.addProperty("label", label);
        body.addProperty("key", key);

        return client.post("/user/ssh-keys", body);
    }
}
