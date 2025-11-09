package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateProjectTool extends BaseToolExecutor {
    public CreateProjectTool(BitbucketClient client) {
        super(client, "create_project",
              "Create a new project in a workspace.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "name", "Project name", true);
        addStringProperty(schema, "key", "Project key (2-10 uppercase letters)", true);
        addStringProperty(schema, "description", "Project description", false);
        addBooleanProperty(schema, "is_private", "Whether project is private", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String name = getRequiredString(arguments, "name");
        String key = getRequiredString(arguments, "key");
        String description = getOptionalString(arguments, "description", "");
        Boolean isPrivate = getOptionalBoolean(arguments, "is_private", true);

        JsonObject body = new JsonObject();
        body.addProperty("name", name);
        body.addProperty("key", key);
        body.addProperty("description", description);
        body.addProperty("is_private", isPrivate);

        return client.post("/workspaces/" + workspace + "/projects", body);
    }
}
