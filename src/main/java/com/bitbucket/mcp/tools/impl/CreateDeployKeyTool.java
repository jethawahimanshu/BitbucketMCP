package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateDeployKeyTool extends BaseToolExecutor {
    public CreateDeployKeyTool(BitbucketClient client) {
        super(client, "create_deploy_key",
              "Add a new deploy key (read-only SSH key) to a repository.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "label", "Deploy key label/name", true);
        addStringProperty(schema, "key", "SSH public key content", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String label = getRequiredString(arguments, "label");
        String key = getRequiredString(arguments, "key");

        JsonObject body = new JsonObject();
        body.addProperty("label", label);
        body.addProperty("key", key);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/deploy-keys", body);
    }
}
