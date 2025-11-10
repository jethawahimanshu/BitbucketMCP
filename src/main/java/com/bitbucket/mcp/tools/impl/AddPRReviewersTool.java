package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AddPRReviewersTool extends BaseToolExecutor {
    public AddPRReviewersTool(BitbucketClient client) {
        super(client, "add_pr_reviewers",
              "Add reviewers to an existing pull request.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "pull_request_id", "Pull request ID", true);
        addStringProperty(schema, "reviewers", "Comma-separated list of usernames to add as reviewers", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = arguments.get("pull_request_id").getAsInt();
        String reviewersStr = getRequiredString(arguments, "reviewers");

        JsonObject body = new JsonObject();
        JsonArray reviewers = new JsonArray();
        for (String username : reviewersStr.split(",")) {
            JsonObject reviewer = new JsonObject();
            reviewer.addProperty("username", username.trim());
            reviewers.add(reviewer);
        }
        body.add("reviewers", reviewers);

        return client.put("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId, body);
    }
}
