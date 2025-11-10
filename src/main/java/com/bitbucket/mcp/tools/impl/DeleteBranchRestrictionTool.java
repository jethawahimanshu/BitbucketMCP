package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteBranchRestrictionTool extends BaseToolExecutor {
    public DeleteBranchRestrictionTool(BitbucketClient client) {
        super(client, "delete_branch_restriction",
              "Delete a branch restriction rule by its ID.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "restriction_id", "Branch restriction ID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String restrictionId = getRequiredString(arguments, "restriction_id");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/branch-restrictions/" + restrictionId);
    }
}
