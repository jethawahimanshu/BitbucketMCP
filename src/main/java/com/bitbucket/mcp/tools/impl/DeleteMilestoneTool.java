package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteMilestoneTool extends BaseToolExecutor {
    public DeleteMilestoneTool(BitbucketClient client) {
        super(client, "delete_milestone",
              "Delete a milestone.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addIntegerProperty(schema, "milestone_id", "Milestone ID to delete", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int milestoneId = getRequiredInt(arguments, "milestone_id");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/milestones/" + milestoneId);
    }
}
