package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetPRPatchTool extends BaseToolExecutor {
    public GetPRPatchTool(BitbucketClient client) {
        super(client, "get_pr_patch",
              "Get a pull request as a patch file.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addIntegerProperty(schema, "pull_request_id", "Pull request ID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = getRequiredInt(arguments, "pull_request_id");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/patch");
    }
}
