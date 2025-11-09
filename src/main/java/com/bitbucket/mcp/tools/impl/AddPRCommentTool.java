package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class AddPRCommentTool extends BaseToolExecutor {
    public AddPRCommentTool(BitbucketClient client) {
        super(client, "add_pr_comment",
              "Add a comment to a pull request.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "pull_request_id", "Pull request ID", true);
        addStringProperty(schema, "content", "Comment content", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = arguments.get("pull_request_id").getAsInt();
        String content = getRequiredString(arguments, "content");

        JsonObject body = new JsonObject();
        JsonObject contentObj = new JsonObject();
        contentObj.addProperty("raw", content);
        body.add("content", contentObj);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/comments", body);
    }
}
