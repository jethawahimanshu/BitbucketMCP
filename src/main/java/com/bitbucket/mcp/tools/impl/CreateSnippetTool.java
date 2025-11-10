package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateSnippetTool extends BaseToolExecutor {
    public CreateSnippetTool(BitbucketClient client) {
        super(client, "create_snippet",
              "Create a new code snippet.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "title", "Snippet title", true);
        addStringProperty(schema, "content", "Snippet content", true);
        addStringProperty(schema, "filename", "File name", false);
        addBooleanProperty(schema, "is_private", "Make snippet private", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());

        JsonObject body = new JsonObject();
        body.addProperty("title", getRequiredString(arguments, "title"));

        // Create files array for snippet content
        JsonObject files = new JsonObject();
        String filename = getOptionalString(arguments, "filename", "snippet.txt");
        String content = getRequiredString(arguments, "content");
        files.addProperty(filename, content);
        body.add("files", files);

        if (arguments.has("is_private")) {
            body.addProperty("is_private", arguments.get("is_private").getAsBoolean());
        }

        return client.post("/snippets/" + workspace, body);
    }
}
