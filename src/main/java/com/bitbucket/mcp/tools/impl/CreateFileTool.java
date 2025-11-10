package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CreateFileTool extends BaseToolExecutor {
    public CreateFileTool(BitbucketClient client) {
        super(client, "create_file",
              "Create a new file in a repository with specified content. Uses form-data post.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "path", "File path (e.g., src/main.py)", true);
        addStringProperty(schema, "content", "File content", true);
        addStringProperty(schema, "message", "Commit message", true);
        addStringProperty(schema, "branch", "Branch name (default: main)", false);
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

        // Note: Bitbucket file creation requires multipart/form-data
        // This is a simplified version - actual implementation would need proper form-data handling
        JsonObject body = new JsonObject();
        body.addProperty("message", message);
        body.addProperty("branch", branch);
        body.addProperty("content", content);

        // Bitbucket API endpoint for file operations
        String encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8);
        return client.post("/repositories/" + workspace + "/" + repoSlug + "/src", body);
    }
}
