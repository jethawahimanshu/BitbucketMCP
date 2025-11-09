package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetRepositoryTool extends BaseToolExecutor {
    public GetRepositoryTool(BitbucketClient client) {
        super(client, "get_repository",
              "Get detailed information about a specific repository including size, language, clone URLs, and project details.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID or username", false);
        addStringProperty(schema, "repo_slug", "Repository slug or name", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");

        return client.get("/repositories/" + workspace + "/" + repoSlug);
    }
}
