package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class AddPRInlineCommentTool extends BaseToolExecutor {
    public AddPRInlineCommentTool(BitbucketClient client) {
        super(client, "add_pr_inline_comment",
              "Add an inline comment on a specific line of code in a pull request diff.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "pull_request_id", "Pull request ID", true);
        addStringProperty(schema, "content", "Comment content", true);
        addStringProperty(schema, "path", "File path in diff", true);
        addNumberProperty(schema, "line", "Line number in file (to)", true);
        addNumberProperty(schema, "from_line", "Starting line number (optional, for range comments)", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = arguments.get("pull_request_id").getAsInt();
        String content = getRequiredString(arguments, "content");
        String path = getRequiredString(arguments, "path");
        int line = arguments.get("line").getAsInt();
        Integer fromLine = getOptionalInt(arguments, "from_line", null);

        JsonObject body = new JsonObject();

        // Content
        JsonObject contentObj = new JsonObject();
        contentObj.addProperty("raw", content);
        body.add("content", contentObj);

        // Inline location
        JsonObject inline = new JsonObject();
        inline.addProperty("path", path);
        inline.addProperty("to", line);
        if (fromLine != null) {
            inline.addProperty("from", fromLine);
        }
        body.add("inline", inline);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/comments", body);
    }
}
