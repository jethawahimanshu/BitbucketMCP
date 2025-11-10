package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateMilestoneTool extends BaseToolExecutor {
    public CreateMilestoneTool(BitbucketClient client) {
        super(client, "create_milestone",
              "Create a new milestone for issue tracking.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "name", "Milestone name", true);
        addStringProperty(schema, "description", "Milestone description", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");

        JsonObject body = new JsonObject();
        body.addProperty("name", getRequiredString(arguments, "name"));

        if (arguments.has("description")) {
            body.addProperty("description", arguments.get("description").getAsString());
        }

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/milestones", body);
    }
}
