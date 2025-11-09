package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListRepositoriesTool extends BaseToolExecutor {
    public ListRepositoriesTool(BitbucketClient client) {
        super(client, "list_repositories",
              "List all repositories in a workspace. Returns repository names, slugs, URLs, and metadata.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID or username", false);
        addStringProperty(schema, "role", "Filter by role (owner, admin, contributor, member)", false);
        addStringProperty(schema, "q", "Query string to filter repositories", false);
        addStringProperty(schema, "sort", "Sort field (created_on, updated_on, name)", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String role = getOptionalString(arguments, "role", null);
        String query = getOptionalString(arguments, "q", null);
        String sort = getOptionalString(arguments, "sort", null);

        StringBuilder path = new StringBuilder("/repositories/" + workspace);
        boolean firstParam = true;

        if (role != null) {
            path.append("?role=").append(role);
            firstParam = false;
        }
        if (query != null) {
            path.append(firstParam ? "?" : "&").append("q=").append(query);
            firstParam = false;
        }
        if (sort != null) {
            path.append(firstParam ? "?" : "&").append("sort=").append(sort);
        }

        return client.get(path.toString());
    }
}
