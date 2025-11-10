package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateRepositoryVariableTool extends BaseToolExecutor {
    public CreateRepositoryVariableTool(BitbucketClient client) {
        super(client, "create_repository_variable",
              "Create a new repository-level pipeline variable.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "key", "Variable name/key", true);
        addStringProperty(schema, "value", "Variable value", true);
        addBooleanProperty(schema, "secured", "Make variable secured (encrypted)", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");

        JsonObject body = new JsonObject();
        body.addProperty("key", getRequiredString(arguments, "key"));
        body.addProperty("value", getRequiredString(arguments, "value"));
        body.addProperty("secured", getOptionalBoolean(arguments, "secured", false));

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/pipelines_config/variables", body);
    }
}
