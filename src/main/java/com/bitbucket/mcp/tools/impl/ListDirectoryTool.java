package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListDirectoryTool extends BaseToolExecutor {
    public ListDirectoryTool(BitbucketClient client) {
        super(client, "list_directory",
              "List contents of a directory in a repository at a specific commit or branch.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "path", "Directory path (empty for root)", false);
        addStringProperty(schema, "ref", "Branch name or commit hash", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String path = getOptionalString(arguments, "path", "");
        String ref = getOptionalString(arguments, "ref", "main");

        String apiPath = "/repositories/" + workspace + "/" + repoSlug + "/src/" + ref;
        if (!path.isEmpty()) {
            apiPath += "/" + path;
        }

        return client.get(apiPath);
    }
}
