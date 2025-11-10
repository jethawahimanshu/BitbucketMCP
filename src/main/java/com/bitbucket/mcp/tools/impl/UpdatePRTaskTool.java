package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdatePRTaskTool extends BaseToolExecutor {
    public UpdatePRTaskTool(BitbucketClient client) {
        super(client, "update_pr_task",
              "Update a task in a pull request (mark as resolved/unresolved).");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addIntegerProperty(schema, "pull_request_id", "Pull request ID", true);
        addIntegerProperty(schema, "task_id", "Task ID", true);
        addStringProperty(schema, "state", "Task state (RESOLVED or UNRESOLVED)", false);
        addStringProperty(schema, "content", "New task description", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = getRequiredInt(arguments, "pull_request_id");
        int taskId = getRequiredInt(arguments, "task_id");

        JsonObject body = new JsonObject();

        if (arguments.has("state")) {
            body.addProperty("state", arguments.get("state").getAsString());
        }

        if (arguments.has("content")) {
            JsonObject content = new JsonObject();
            content.addProperty("raw", arguments.get("content").getAsString());
            body.add("content", content);
        }

        return client.put("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/tasks/" + taskId, body);
    }
}
