package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdatePullRequestTool extends BaseToolExecutor {
    public UpdatePullRequestTool(BitbucketClient client) {
        super(client, "update_pull_request",
              "Update a pull request's title, description, or reviewers.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "pull_request_id", "Pull request ID", true);
        addStringProperty(schema, "title", "New title", false);
        addStringProperty(schema, "description", "New description", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int prId = arguments.get("pull_request_id").getAsInt();
        String title = getOptionalString(arguments, "title", null);
        String description = getOptionalString(arguments, "description", null);

        JsonObject body = new JsonObject();
        if (title != null) {
            body.addProperty("title", title);
        }
        if (description != null) {
            body.addProperty("description", description);
        }

        return client.put("/repositories/" + workspace + "/" + repoSlug + "/pullrequests/" + prId, body);
    }
}
