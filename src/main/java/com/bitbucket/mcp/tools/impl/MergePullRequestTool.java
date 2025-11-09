package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class MergePullRequestTool extends BaseToolExecutor {
    public MergePullRequestTool(BitbucketClient client) {
        super(client, "merge_pull_request",
              "Merge a pull request. Can specify merge strategy and commit message.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "pull_request_id", "Pull request ID", true);
        addStringProperty(schema, "merge_strategy", "Merge strategy: merge_commit, squash, fast_forward", false);
        addStringProperty(schema, "message", "Merge commit message", false);
        addBooleanProperty(schema, "close_source_branch", "Close source branch after merge", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = arguments.get("pull_request_id").getAsInt();
        String mergeStrategy = getOptionalString(arguments, "merge_strategy", null);
        String message = getOptionalString(arguments, "message", null);
        Boolean closeSourceBranch = getOptionalBoolean(arguments, "close_source_branch", null);

        JsonObject body = new JsonObject();
        if (mergeStrategy != null) {
            body.addProperty("type", mergeStrategy);
        }
        if (message != null) {
            body.addProperty("message", message);
        }
        if (closeSourceBranch != null) {
            body.addProperty("close_source_branch", closeSourceBranch);
        }

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/merge", body);
    }
}
