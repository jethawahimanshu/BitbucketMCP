package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class VoteIssueTool extends BaseToolExecutor {
    public VoteIssueTool(BitbucketClient client) {
        super(client, "vote_issue",
              "Vote for an issue (upvote).");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addIntegerProperty(schema, "issue_id", "Issue ID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int issueId = getRequiredInt(arguments, "issue_id");

        return client.put("/repositories/" + workspace + "/" + repoSlug + "/issues/" + issueId + "/vote", new JsonObject());
    }
}
