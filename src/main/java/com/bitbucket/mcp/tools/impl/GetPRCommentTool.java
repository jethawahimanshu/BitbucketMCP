package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetPRCommentTool extends BaseToolExecutor {
    public GetPRCommentTool(BitbucketClient client) {
        super(client, "get_pr_comment",
              "Get details of a specific pull request comment including inline location.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "pull_request_id", "Pull request ID", true);
        addNumberProperty(schema, "comment_id", "Comment ID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = arguments.get("pull_request_id").getAsInt();
        int commentId = arguments.get("comment_id").getAsInt();

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/comments/" + commentId);
    }
}
