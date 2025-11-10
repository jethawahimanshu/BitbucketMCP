package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteDownloadTool extends BaseToolExecutor {
    public DeleteDownloadTool(BitbucketClient client) {
        super(client, "delete_download",
              "Delete a download/artifact from a repository.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "filename", "Download filename", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String filename = getRequiredString(arguments, "filename");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/downloads/" + filename);
    }
}
