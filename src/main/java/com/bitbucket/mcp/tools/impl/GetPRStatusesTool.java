package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetPRStatusesTool extends BaseToolExecutor {
    public GetPRStatusesTool(BitbucketClient client) {
        super(client, "get_pr_statuses",
              "Get build statuses for a pull request (CI/CD pipeline results).");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "pull_request_id", "Pull request ID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = arguments.get("pull_request_id").getAsInt();

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId + "/statuses");
    }
}
