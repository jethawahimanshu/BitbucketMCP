package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdateRepositoryTool extends BaseToolExecutor {
    public UpdateRepositoryTool(BitbucketClient client) {
        super(client, "update_repository",
              "Update repository settings like name, description, is_private, has_issues, has_wiki, etc.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "name", "New repository name", false);
        addStringProperty(schema, "description", "New description", false);
        addBooleanProperty(schema, "is_private", "Make repository private/public", false);
        addBooleanProperty(schema, "has_issues", "Enable/disable issues", false);
        addBooleanProperty(schema, "has_wiki", "Enable/disable wiki", false);
        addStringProperty(schema, "language", "Main programming language", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");

        JsonObject body = new JsonObject();

        if (arguments.has("name")) {
            body.addProperty("name", arguments.get("name").getAsString());
        }
        if (arguments.has("description")) {
            body.addProperty("description", arguments.get("description").getAsString());
        }
        if (arguments.has("is_private")) {
            body.addProperty("is_private", arguments.get("is_private").getAsBoolean());
        }
        if (arguments.has("has_issues")) {
            body.addProperty("has_issues", arguments.get("has_issues").getAsBoolean());
        }
        if (arguments.has("has_wiki")) {
            body.addProperty("has_wiki", arguments.get("has_wiki").getAsBoolean());
        }
        if (arguments.has("language")) {
            body.addProperty("language", arguments.get("language").getAsString());
        }

        return client.put("/repositories/" + workspace + "/" + repoSlug, body);
    }
}
