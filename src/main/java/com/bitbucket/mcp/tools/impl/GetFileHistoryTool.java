package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetFileHistoryTool extends BaseToolExecutor {
    public GetFileHistoryTool(BitbucketClient client) {
        super(client, "get_file_history",
              "Get the commit history for a specific file.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "path", "File path", true);
        addStringProperty(schema, "ref", "Branch or commit (default: main)", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String path = getRequiredString(arguments, "path");
        String ref = getOptionalString(arguments, "ref", "main");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/filehistory/" + ref + "/" + path);
    }
}
