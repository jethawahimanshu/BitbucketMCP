package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreatePRTaskTool extends BaseToolExecutor {
    public CreatePRTaskTool(BitbucketClient client) {
        super(client, "create_pr_task",
              "Create a new task in a pull request.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addIntegerProperty(schema, "pull_request_id", "Pull request ID", true);
        addStringProperty(schema, "content", "Task description", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = getRequiredInt(arguments, "pull_request_id");

        JsonObject body = new JsonObject();
        JsonObject content = new JsonObject();
        content.addProperty("raw", getRequiredString(arguments, "content"));
        body.add("content", content);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/tasks", body);
    }
}
