package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteDeployKeyTool extends BaseToolExecutor {
    public DeleteDeployKeyTool(BitbucketClient client) {
        super(client, "delete_deploy_key",
              "Delete a deploy key from a repository.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "key_id", "Deploy key ID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String keyId = getRequiredString(arguments, "key_id");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/deploy-keys/" + keyId);
    }
}
