package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreatePipelineVariableTool extends BaseToolExecutor {
    public CreatePipelineVariableTool(BitbucketClient client) {
        super(client, "create_pipeline_variable",
              "Create a new pipeline variable (environment variable) for a repository.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "key", "Variable key/name", true);
        addStringProperty(schema, "value", "Variable value", true);
        addBooleanProperty(schema, "secured", "Mark as secured/secret variable", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String key = getRequiredString(arguments, "key");
        String value = getRequiredString(arguments, "value");
        Boolean secured = getOptionalBoolean(arguments, "secured", false);

        JsonObject body = new JsonObject();
        body.addProperty("key", key);
        body.addProperty("value", value);
        body.addProperty("secured", secured);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/pipelines_config/variables/", body);
    }
}
