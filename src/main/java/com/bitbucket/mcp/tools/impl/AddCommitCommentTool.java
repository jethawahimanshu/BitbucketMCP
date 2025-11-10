package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class AddCommitCommentTool extends BaseToolExecutor {
    public AddCommitCommentTool(BitbucketClient client) {
        super(client, "add_commit_comment",
              "Add a comment to a specific commit. Can be inline on a specific file/line.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "commit_hash", "Commit hash/SHA", true);
        addStringProperty(schema, "content", "Comment content", true);
        addStringProperty(schema, "path", "File path for inline comment (optional)", false);
        addNumberProperty(schema, "line", "Line number for inline comment (optional)", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String commitHash = getRequiredString(arguments, "commit_hash");
        String content = getRequiredString(arguments, "content");
        String path = getOptionalString(arguments, "path", null);
        Integer line = getOptionalInt(arguments, "line", null);

        JsonObject body = new JsonObject();
        JsonObject contentObj = new JsonObject();
        contentObj.addProperty("raw", content);
        body.add("content", contentObj);

        if (path != null && line != null) {
            JsonObject inline = new JsonObject();
            inline.addProperty("path", path);
            inline.addProperty("to", line);
            body.add("inline", inline);
        }

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/commit/" + commitHash + "/comments", body);
    }
}
