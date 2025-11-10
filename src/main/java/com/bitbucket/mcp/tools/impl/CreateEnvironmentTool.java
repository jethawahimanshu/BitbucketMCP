package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateEnvironmentTool extends BaseToolExecutor {
    public CreateEnvironmentTool(BitbucketClient client) {
        super(client, "create_environment",
              "Create a new deployment environment.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "name", "Environment name", true);
        addStringProperty(schema, "environment_type", "Environment type (Production, Staging, Test)", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");

        JsonObject body = new JsonObject();
        body.addProperty("name", getRequiredString(arguments, "name"));

        JsonObject envType = new JsonObject();
        envType.addProperty("name", getRequiredString(arguments, "environment_type"));
        body.add("environment_type", envType);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/environments", body);
    }
}
