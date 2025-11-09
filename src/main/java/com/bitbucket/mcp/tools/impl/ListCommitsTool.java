package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListCommitsTool extends BaseToolExecutor {
    public ListCommitsTool(BitbucketClient client) {
        super(client, "list_commits",
              "List commits in a repository or on a specific branch.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "branch", "Branch name to list commits from", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String branch = getOptionalString(arguments, "branch", null);

        String path = "/repositories/" + workspace + "/" + repoSlug + "/commits";
        if (branch != null) {
            path += "/" + branch;
        }

        return client.get(path);
    }
}
