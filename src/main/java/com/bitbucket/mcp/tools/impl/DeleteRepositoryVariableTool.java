package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteRepositoryVariableTool extends BaseToolExecutor {
    public DeleteRepositoryVariableTool(BitbucketClient client) {
        super(client, "delete_repository_variable",
              "Delete a repository-level pipeline variable.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "variable_uuid", "Variable UUID to delete", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String variableUuid = getRequiredString(arguments, "variable_uuid");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/pipelines_config/variables/" + variableUuid);
    }
}
