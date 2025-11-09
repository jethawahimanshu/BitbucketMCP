package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateIssueTool extends BaseToolExecutor {
    public CreateIssueTool(BitbucketClient client) {
        super(client, "create_issue",
              "Create a new issue in a repository with title, description, and metadata.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "title", "Issue title", true);
        addStringProperty(schema, "content", "Issue description/content", false);
        addStringProperty(schema, "kind", "Issue kind: bug, enhancement, proposal, task", false);
        addStringProperty(schema, "priority", "Priority: trivial, minor, major, critical, blocker", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String title = getRequiredString(arguments, "title");
        String content = getOptionalString(arguments, "content", "");
        String kind = getOptionalString(arguments, "kind", "bug");
        String priority = getOptionalString(arguments, "priority", "major");

        JsonObject body = new JsonObject();
        body.addProperty("title", title);
        body.addProperty("kind", kind);
        body.addProperty("priority", priority);

        if (!content.isEmpty()) {
            JsonObject contentObj = new JsonObject();
            contentObj.addProperty("raw", content);
            body.add("content", contentObj);
        }

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/issues", body);
    }
}
