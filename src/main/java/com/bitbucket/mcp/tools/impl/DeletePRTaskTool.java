package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeletePRTaskTool extends BaseToolExecutor {
    public DeletePRTaskTool(BitbucketClient client) {
        super(client, "delete_pr_task",
              "Delete a task from a pull request.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addIntegerProperty(schema, "pull_request_id", "Pull request ID", true);
        addIntegerProperty(schema, "task_id", "Task ID to delete", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = getRequiredInt(arguments, "pull_request_id");
        int taskId = getRequiredInt(arguments, "task_id");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/tasks/" + taskId);
    }
}
