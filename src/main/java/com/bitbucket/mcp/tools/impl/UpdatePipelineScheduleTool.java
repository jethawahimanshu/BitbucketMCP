package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdatePipelineScheduleTool extends BaseToolExecutor {
    public UpdatePipelineScheduleTool(BitbucketClient client) {
        super(client, "update_pipeline_schedule",
              "Update an existing pipeline schedule.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "schedule_uuid", "Schedule UUID", true);
        addStringProperty(schema, "cron_pattern", "New cron pattern", false);
        addStringProperty(schema, "target_branch", "New target branch", false);
        addBooleanProperty(schema, "enabled", "Enable/disable schedule", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String scheduleUuid = getRequiredString(arguments, "schedule_uuid");

        JsonObject body = new JsonObject();

        if (arguments.has("cron_pattern")) {
            body.addProperty("cron_pattern", arguments.get("cron_pattern").getAsString());
        }

        if (arguments.has("target_branch")) {
            JsonObject target = new JsonObject();
            target.addProperty("ref_type", "branch");
            target.addProperty("ref_name", arguments.get("target_branch").getAsString());
            body.add("target", target);
        }

        if (arguments.has("enabled")) {
            body.addProperty("enabled", arguments.get("enabled").getAsBoolean());
        }

        return client.put("/repositories/" + workspace + "/" + repoSlug + "/pipelines_config/schedules/" + scheduleUuid, body);
    }
}
