package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CreatePullRequestTool extends BaseToolExecutor {
    public CreatePullRequestTool(BitbucketClient client) {
        super(client, "create_pull_request",
              "Create a new pull request with title, description, source and destination branches.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "title", "Pull request title", true);
        addStringProperty(schema, "description", "Pull request description", false);
        addStringProperty(schema, "source_branch", "Source branch name", true);
        addStringProperty(schema, "destination_branch", "Destination branch name", true);
        addBooleanProperty(schema, "close_source_branch", "Close source branch after merge", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String title = getRequiredString(arguments, "title");
        String description = getOptionalString(arguments, "description", "");
        String sourceBranch = getRequiredString(arguments, "source_branch");
        String destBranch = getRequiredString(arguments, "destination_branch");
        Boolean closeSourceBranch = getOptionalBoolean(arguments, "close_source_branch", false);

        JsonObject body = new JsonObject();
        body.addProperty("title", title);
        body.addProperty("description", description);
        body.addProperty("close_source_branch", closeSourceBranch);

        JsonObject source = new JsonObject();
        JsonObject sourceBranchObj = new JsonObject();
        sourceBranchObj.addProperty("name", sourceBranch);
        source.add("branch", sourceBranchObj);
        body.add("source", source);

        JsonObject destination = new JsonObject();
        JsonObject destBranchObj = new JsonObject();
        destBranchObj.addProperty("name", destBranch);
        destination.add("branch", destBranchObj);
        body.add("destination", destination);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/pullrequests", body);
    }
}
