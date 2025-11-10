package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateComponentTool extends BaseToolExecutor {
    public CreateComponentTool(BitbucketClient client) {
        super(client, "create_component",
              "Create a new component for issue categorization.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "name", "Component name", true);
        addStringProperty(schema, "description", "Component description", false);
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

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/components", body);
    }
}
