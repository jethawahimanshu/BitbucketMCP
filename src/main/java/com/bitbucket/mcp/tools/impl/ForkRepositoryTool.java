package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ForkRepositoryTool extends BaseToolExecutor {
    public ForkRepositoryTool(BitbucketClient client) {
        super(client, "fork_repository",
              "Fork a repository to your workspace or another workspace.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Source workspace ID", false);
        addStringProperty(schema, "repo_slug", "Source repository slug", true);
        addStringProperty(schema, "name", "Name for the forked repository", false);
        addStringProperty(schema, "target_workspace", "Target workspace for the fork", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String name = getOptionalString(arguments, "name", null);
        String targetWorkspace = getOptionalString(arguments, "target_workspace", null);

        JsonObject body = new JsonObject();
        if (name != null) {
            body.addProperty("name", name);
        }
        if (targetWorkspace != null) {
            JsonObject workspace_obj = new JsonObject();
            workspace_obj.addProperty("slug", targetWorkspace);
            body.add("workspace", workspace_obj);
        }

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/forks", body);
    }
}
