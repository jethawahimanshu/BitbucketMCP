package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SearchRepositoriesTool extends BaseToolExecutor {
    public SearchRepositoriesTool(BitbucketClient client) {
        super(client, "search_repositories",
              "Search for repositories by name or description across workspaces.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "query", "Search query string", true);
        addStringProperty(schema, "workspace", "Limit search to specific workspace", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String query = getRequiredString(arguments, "query");
        String workspace = getOptionalString(arguments, "workspace", null);

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        String path;
        if (workspace != null) {
            path = "/repositories/" + workspace + "?q=name~\"" + encodedQuery + "\"";
        } else {
            path = "/repositories?q=name~\"" + encodedQuery + "\"";
        }

        return client.get(path);
    }
}
