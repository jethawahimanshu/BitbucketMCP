package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class AddDefaultReviewerTool extends BaseToolExecutor {
    public AddDefaultReviewerTool(BitbucketClient client) {
        super(client, "add_default_reviewer",
              "Add a user as a default reviewer for all new pull requests.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "target_username", "Username to add as default reviewer", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String targetUsername = getRequiredString(arguments, "target_username");

        return client.put("/repositories/" + workspace + "/" + repoSlug + "/default-reviewers/" + targetUsername, new JsonObject());
    }
}
