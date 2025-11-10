package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreatePipelineScheduleTool extends BaseToolExecutor {
    public CreatePipelineScheduleTool(BitbucketClient client) {
        super(client, "create_pipeline_schedule",
              "Create a new pipeline schedule (cron-based).");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "cron_pattern", "Cron pattern (e.g., '0 0 * * *')", true);
        addStringProperty(schema, "target_branch", "Target branch name", true);
        addBooleanProperty(schema, "enabled", "Enable schedule", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");

        JsonObject body = new JsonObject();
        body.addProperty("cron_pattern", getRequiredString(arguments, "cron_pattern"));

        JsonObject target = new JsonObject();
        target.addProperty("ref_type", "branch");
        target.addProperty("ref_name", getRequiredString(arguments, "target_branch"));
        body.add("target", target);

        body.addProperty("enabled", getOptionalBoolean(arguments, "enabled", true));

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/pipelines_config/schedules", body);
    }
}
