package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class AddIssueCommentTool extends BaseToolExecutor {
    public AddIssueCommentTool(BitbucketClient client) {
        super(client, "add_issue_comment",
              "Add a comment to an issue.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "issue_id", "Issue ID", true);
        addStringProperty(schema, "content", "Comment content", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int issueId = arguments.get("issue_id").getAsInt();
        String content = getRequiredString(arguments, "content");

        JsonObject body = new JsonObject();
        JsonObject contentObj = new JsonObject();
        contentObj.addProperty("raw", content);
        body.add("content", contentObj);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/issues/" + issueId + "/comments", body);
    }
}
