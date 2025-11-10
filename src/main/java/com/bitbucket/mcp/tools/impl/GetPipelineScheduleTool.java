package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetPipelineScheduleTool extends BaseToolExecutor {
    public GetPipelineScheduleTool(BitbucketClient client) {
        super(client, "get_pipeline_schedule",
              "Get details about a specific pipeline schedule.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "schedule_uuid", "Schedule UUID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String scheduleUuid = getRequiredString(arguments, "schedule_uuid");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/pipelines_config/schedules/" + scheduleUuid);
    }
}
