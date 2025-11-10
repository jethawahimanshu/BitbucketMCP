package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetPipelineStepsTool extends BaseToolExecutor {
    public GetPipelineStepsTool(BitbucketClient client) {
        super(client, "get_pipeline_steps",
              "Get all steps in a pipeline execution with their status and logs.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "pipeline_uuid", "Pipeline UUID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String pipelineUuid = getRequiredString(arguments, "pipeline_uuid");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/pipelines/" + pipelineUuid + "/steps/");
    }
}
