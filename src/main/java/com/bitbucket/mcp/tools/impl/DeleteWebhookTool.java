package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteWebhookTool extends BaseToolExecutor {
    public DeleteWebhookTool(BitbucketClient client) {
        super(client, "delete_webhook",
              "Delete a webhook from a repository.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "webhook_uid", "Webhook UID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String webhookUid = getRequiredString(arguments, "webhook_uid");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/hooks/" + webhookUid);
    }
}
