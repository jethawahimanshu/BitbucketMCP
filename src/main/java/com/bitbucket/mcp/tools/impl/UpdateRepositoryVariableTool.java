package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdateRepositoryVariableTool extends BaseToolExecutor {
    public UpdateRepositoryVariableTool(BitbucketClient client) {
        super(client, "update_repository_variable",
              "Update an existing repository-level pipeline variable.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "variable_uuid", "Variable UUID", true);
        addStringProperty(schema, "key", "New variable name/key", false);
        addStringProperty(schema, "value", "New variable value", false);
        addBooleanProperty(schema, "secured", "Make variable secured (encrypted)", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String variableUuid = getRequiredString(arguments, "variable_uuid");

        JsonObject body = new JsonObject();

        if (arguments.has("key")) {
            body.addProperty("key", arguments.get("key").getAsString());
        }
        if (arguments.has("value")) {
            body.addProperty("value", arguments.get("value").getAsString());
        }
        if (arguments.has("secured")) {
            body.addProperty("secured", arguments.get("secured").getAsBoolean());
        }

        return client.put("/repositories/" + workspace + "/" + repoSlug + "/pipelines_config/variables/" + variableUuid, body);
    }
}
