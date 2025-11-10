package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class DeleteTagTool extends BaseToolExecutor {
    public DeleteTagTool(BitbucketClient client) {
        super(client, "delete_tag",
              "Delete a tag from a repository.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "tag_name", "Tag name to delete", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String tagName = getRequiredString(arguments, "tag_name");

        return client.delete("/repositories/" + workspace + "/" + repoSlug + "/refs/tags/" + tagName);
    }
}
