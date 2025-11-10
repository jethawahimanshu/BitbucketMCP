package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteEnvironmentTool extends BaseToolExecutor {
    public DeleteEnvironmentTool(BitbucketClient client) {
        super(client, "delete_environment",
              "Delete a deployment environment.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "environment_uuid", "Environment UUID to delete", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String environmentUuid = getRequiredString(arguments, "environment_uuid");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/environments/" + environmentUuid);
    }
}
