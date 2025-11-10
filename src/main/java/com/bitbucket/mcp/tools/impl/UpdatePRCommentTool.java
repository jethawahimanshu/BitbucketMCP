package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdatePRCommentTool extends BaseToolExecutor {
    public UpdatePRCommentTool(BitbucketClient client) {
        super(client, "update_pr_comment",
              "Update an existing pull request comment.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "pull_request_id", "Pull request ID", true);
        addNumberProperty(schema, "comment_id", "Comment ID", true);
        addStringProperty(schema, "content", "New comment content", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = arguments.get("pull_request_id").getAsInt();
        int commentId = arguments.get("comment_id").getAsInt();
        String content = getRequiredString(arguments, "content");

        JsonObject body = new JsonObject();
        JsonObject contentObj = new JsonObject();
        contentObj.addProperty("raw", content);
        body.add("content", contentObj);

        return client.put("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/comments/" + commentId, body);
    }
}
