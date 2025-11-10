package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetTagTool extends BaseToolExecutor {
    public GetTagTool(BitbucketClient client) {
        super(client, "get_tag",
              "Get details about a specific tag.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "tag_name", "Tag name", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String tagName = getRequiredString(arguments, "tag_name");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/refs/tags/" + tagName);
    }
}
