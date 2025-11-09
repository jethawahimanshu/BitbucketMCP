package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class RunPipelineTool extends BaseToolExecutor {
    public RunPipelineTool(BitbucketClient client) {
        super(client, "run_pipeline",
              "Trigger a new pipeline run for a specific branch or commit.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "branch", "Branch name to run pipeline for", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String branch = getRequiredString(arguments, "branch");

        JsonObject body = new JsonObject();
        JsonObject target = new JsonObject();
        target.addProperty("ref_type", "branch");
        target.addProperty("type", "pipeline_ref_target");
        target.addProperty("ref_name", branch);
        body.add("target", target);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/pipelines/", body);
    }
}
