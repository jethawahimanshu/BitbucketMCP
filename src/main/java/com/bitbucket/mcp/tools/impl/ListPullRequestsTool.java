package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListPullRequestsTool extends BaseToolExecutor {
    public ListPullRequestsTool(BitbucketClient client) {
        super(client, "list_pull_requests",
              "List pull requests in a repository. Can filter by state (OPEN, MERGED, DECLINED, SUPERSEDED).");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "state", "PR state: OPEN, MERGED, DECLINED, SUPERSEDED", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String state = getOptionalString(arguments, "state", null);

        String path = "/repositories/" + workspace + "/" + repoSlug + "/pullrequests";
        if (state != null) {
            path += "?state=" + state;
        }

        return client.get(path);
    }
}
