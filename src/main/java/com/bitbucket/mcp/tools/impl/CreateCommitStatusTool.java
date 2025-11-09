package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateCommitStatusTool extends BaseToolExecutor {
    public CreateCommitStatusTool(BitbucketClient client) {
        super(client, "create_commit_status",
              "Create a build status for a commit. State can be SUCCESSFUL, FAILED, INPROGRESS, or STOPPED.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "commit_hash", "Commit hash/SHA", true);
        addStringProperty(schema, "state", "Status state: SUCCESSFUL, FAILED, INPROGRESS, STOPPED", true);
        addStringProperty(schema, "key", "Unique key for this status", true);
        addStringProperty(schema, "name", "Display name", false);
        addStringProperty(schema, "url", "URL to details", false);
        addStringProperty(schema, "description", "Description", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String commitHash = getRequiredString(arguments, "commit_hash");
        String state = getRequiredString(arguments, "state");
        String key = getRequiredString(arguments, "key");
        String name = getOptionalString(arguments, "name", key);
        String url = getOptionalString(arguments, "url", null);
        String description = getOptionalString(arguments, "description", null);

        JsonObject body = new JsonObject();
        body.addProperty("state", state);
        body.addProperty("key", key);
        body.addProperty("name", name);
        if (url != null) body.addProperty("url", url);
        if (description != null) body.addProperty("description", description);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/commit/" + commitHash + "/statuses/build", body);
    }
}
