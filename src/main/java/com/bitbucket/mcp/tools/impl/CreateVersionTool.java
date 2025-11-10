package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateVersionTool extends BaseToolExecutor {
    public CreateVersionTool(BitbucketClient client) {
        super(client, "create_version",
              "Create a new version for issue tracking.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "name", "Version name", true);
        addStringProperty(schema, "description", "Version description", false);
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

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/versions", body);
    }
}
