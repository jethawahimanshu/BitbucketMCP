package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetEnvironmentTool extends BaseToolExecutor {
    public GetEnvironmentTool(BitbucketClient client) {
        super(client, "get_environment",
              "Get details about a specific deployment environment.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "environment_uuid", "Environment UUID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String environmentUuid = getRequiredString(arguments, "environment_uuid");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/environments/" + environmentUuid);
    }
}
