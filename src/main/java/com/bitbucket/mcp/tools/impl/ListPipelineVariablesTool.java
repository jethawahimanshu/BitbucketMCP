package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListPipelineVariablesTool extends BaseToolExecutor {
    public ListPipelineVariablesTool(BitbucketClient client) {
        super(client, "list_pipeline_variables",
              "List all pipeline variables (environment variables) for a repository.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/pipelines_config/variables/");
    }
}
