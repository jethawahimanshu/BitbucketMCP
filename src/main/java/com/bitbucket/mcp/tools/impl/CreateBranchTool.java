package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateBranchTool extends BaseToolExecutor {
    public CreateBranchTool(BitbucketClient client) {
        super(client, "create_branch",
              "Create a new branch from a specific commit or existing branch.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "branch_name", "New branch name", true);
        addStringProperty(schema, "target", "Target commit hash or branch name", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String branchName = getRequiredString(arguments, "branch_name");
        String target = getRequiredString(arguments, "target");

        JsonObject body = new JsonObject();
        body.addProperty("name", branchName);
        JsonObject targetObj = new JsonObject();
        targetObj.addProperty("hash", target);
        body.add("target", targetObj);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/refs/branches", body);
    }
}
