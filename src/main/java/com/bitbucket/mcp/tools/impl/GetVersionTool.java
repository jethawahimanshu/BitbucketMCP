package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetVersionTool extends BaseToolExecutor {
    public GetVersionTool(BitbucketClient client) {
        super(client, "get_version",
              "Get details about a specific version.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addIntegerProperty(schema, "version_id", "Version ID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int versionId = getRequiredInt(arguments, "version_id");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/versions/" + versionId);
    }
}
