package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetMilestoneTool extends BaseToolExecutor {
    public GetMilestoneTool(BitbucketClient client) {
        super(client, "get_milestone",
              "Get details about a specific milestone.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addIntegerProperty(schema, "milestone_id", "Milestone ID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int milestoneId = getRequiredInt(arguments, "milestone_id");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/milestones/" + milestoneId);
    }
}
