package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class RemoveDefaultReviewerTool extends BaseToolExecutor {
    public RemoveDefaultReviewerTool(BitbucketClient client) {
        super(client, "remove_default_reviewer",
              "Remove a user from default reviewers list.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "target_username", "Username to remove", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String targetUsername = getRequiredString(arguments, "target_username");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/default-reviewers/" + targetUsername);
    }
}
