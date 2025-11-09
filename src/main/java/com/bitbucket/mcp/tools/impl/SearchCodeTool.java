package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SearchCodeTool extends BaseToolExecutor {
    public SearchCodeTool(BitbucketClient client) {
        super(client, "search_code",
              "Search for code within a repository.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "search_query", "Code search query", true);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String searchQuery = getRequiredString(arguments, "search_query");

        String encodedQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
        String path = "/repositories/" + workspace + "/" + repoSlug + "/search/code?search_query=" + encodedQuery;

        return client.get(path);
    }
}
