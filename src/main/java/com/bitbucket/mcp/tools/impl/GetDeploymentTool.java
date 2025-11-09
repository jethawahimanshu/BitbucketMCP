package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetDeploymentTool extends BaseToolExecutor {
    public GetDeploymentTool(BitbucketClient client) {
        super(client, "get_deployment",
              "Get details about a specific deployment.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "deployment_uuid", "Deployment UUID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String deploymentUuid = getRequiredString(arguments, "deployment_uuid");

        return client.get("/repositories/" + workspace + "/" + repoSlug + "/deployments/" + deploymentUuid);
    }
}
