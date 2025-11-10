package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UnwatchIssueTool extends BaseToolExecutor {
    public UnwatchIssueTool(BitbucketClient client) {
        super(client, "unwatch_issue",
              "Stop watching an issue.");
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

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/issues/" + issueId + "/watch");
    }
}
