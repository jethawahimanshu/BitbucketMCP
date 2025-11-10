package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListIssueWatchersTool extends BaseToolExecutor {
    public ListIssueWatchersTool(BitbucketClient client) {
        super(client, "list_issue_watchers",
              "List all users watching an issue.");
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

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/issues/" + issueId + "/watch");
    }
}
