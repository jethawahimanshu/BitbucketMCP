package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListIssuesTool extends BaseToolExecutor {
    public ListIssuesTool(BitbucketClient client) {
        super(client, "list_issues",
              "List all issues in a repository. Can filter by state, kind, or priority.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "state", "Filter by state (new, open, resolved, on hold, invalid, duplicate, wontfix, closed)", false);
        addStringProperty(schema, "kind", "Filter by kind (bug, enhancement, proposal, task)", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String state = getOptionalString(arguments, "state", null);
        String kind = getOptionalString(arguments, "kind", null);

        StringBuilder path = new StringBuilder("/repositories/" + workspace + "/" + repoSlug + "/issues");
        boolean firstParam = true;

        if (state != null) {
            path.append("?state=").append(state);
            firstParam = false;
        }
        if (kind != null) {
            path.append(firstParam ? "?" : "&").append("kind=").append(kind);
        }

        return client.get(path.toString());
    }
}
