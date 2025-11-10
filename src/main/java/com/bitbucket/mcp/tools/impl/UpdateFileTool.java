package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdateFileTool extends BaseToolExecutor {
    public UpdateFileTool(BitbucketClient client) {
        super(client, "update_file",
              "Update an existing file in a repository with new content.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "path", "File path", true);
        addStringProperty(schema, "content", "New file content", true);
        addStringProperty(schema, "message", "Commit message", true);
        addStringProperty(schema, "branch", "Branch name (default: main)", false);
        addStringProperty(schema, "parent", "Parent commit hash (for conflict detection)", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String path = getRequiredString(arguments, "path");
        String content = getRequiredString(arguments, "content");
        String message = getRequiredString(arguments, "message");
        String branch = getOptionalString(arguments, "branch", "main");
        String parent = getOptionalString(arguments, "parent", null);

        JsonObject body = new JsonObject();
        body.addProperty("message", message);
        body.addProperty("branch", branch);
        body.addProperty("content", content);
        if (parent != null) {
            body.addProperty("parent", parent);
        }

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/src", body);
    }
}
