package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetCommitDiffTool extends BaseToolExecutor {
    public GetCommitDiffTool(BitbucketClient client) {
        super(client, "get_commit_diff",
              "Get the full diff of a commit showing all file changes.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "commit_hash", "Commit hash/SHA", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String commitHash = getRequiredString(arguments, "commit_hash");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/diff/" + commitHash);
    }
}
