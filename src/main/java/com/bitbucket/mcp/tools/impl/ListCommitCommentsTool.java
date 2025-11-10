package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListCommitCommentsTool extends BaseToolExecutor {
    public ListCommitCommentsTool(BitbucketClient client) {
        super(client, "list_commit_comments",
              "List all comments on a specific commit.");
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

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/commit/" + commitHash + "/comments");
    }
}
