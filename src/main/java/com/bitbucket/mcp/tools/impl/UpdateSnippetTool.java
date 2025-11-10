package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdateSnippetTool extends BaseToolExecutor {
    public UpdateSnippetTool(BitbucketClient client) {
        super(client, "update_snippet",
              "Update an existing snippet.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "snippet_id", "Snippet ID", true);
        addStringProperty(schema, "title", "New title", false);
        addStringProperty(schema, "content", "New content", false);
        addStringProperty(schema, "filename", "File name", false);
        addBooleanProperty(schema, "is_private", "Make snippet private/public", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String snippetId = getRequiredString(arguments, "snippet_id");

        JsonObject body = new JsonObject();

        if (arguments.has("title")) {
            body.addProperty("title", arguments.get("title").getAsString());
        }

        if (arguments.has("content")) {
            JsonObject files = new JsonObject();
            String filename = getOptionalString(arguments, "filename", "snippet.txt");
            files.addProperty(filename, arguments.get("content").getAsString());
            body.add("files", files);
        }

        if (arguments.has("is_private")) {
            body.addProperty("is_private", arguments.get("is_private").getAsBoolean());
        }

        return client.put("/snippets/" + workspace + "/" + snippetId, body);
    }
}
