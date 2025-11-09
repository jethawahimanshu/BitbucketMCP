package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetBranchTool extends BaseToolExecutor {
    public GetBranchTool(BitbucketClient client) {
        super(client, "get_branch",
              "Get detailed information about a specific branch.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "branch_name", "Branch name", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String branchName = getRequiredString(arguments, "branch_name");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/refs/branches/" + branchName);
    }
}
