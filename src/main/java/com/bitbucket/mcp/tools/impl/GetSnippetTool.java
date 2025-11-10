package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class GetSnippetTool extends BaseToolExecutor {
    public GetSnippetTool(BitbucketClient client) {
        super(client, "get_snippet",
              "Get details about a specific snippet.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "snippet_id", "Snippet ID", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String snippetId = getRequiredString(arguments, "snippet_id");

        return client.get("/snippets/" + workspace + "/" + snippetId);
    }
}
