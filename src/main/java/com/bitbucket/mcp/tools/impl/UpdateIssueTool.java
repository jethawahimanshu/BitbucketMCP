package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class UpdateIssueTool extends BaseToolExecutor {
    public UpdateIssueTool(BitbucketClient client) {
        super(client, "update_issue",
              "Update an existing issue's title, description, state, priority, or kind.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addNumberProperty(schema, "issue_id", "Issue ID", true);
        addStringProperty(schema, "title", "New title", false);
        addStringProperty(schema, "content", "New content/description", false);
        addStringProperty(schema, "state", "New state: new, open, resolved, on hold, invalid, duplicate, wontfix, closed", false);
        addStringProperty(schema, "kind", "New kind: bug, enhancement, proposal, task", false);
        addStringProperty(schema, "priority", "New priority: trivial, minor, major, critical, blocker", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        int issueId = arguments.get("issue_id").getAsInt();

        JsonObject body = new JsonObject();

        if (arguments.has("title")) {
            body.addProperty("title", arguments.get("title").getAsString());
        }
        if (arguments.has("content")) {
            JsonObject contentObj = new JsonObject();
            contentObj.addProperty("raw", arguments.get("content").getAsString());
            body.add("content", contentObj);
        }
        if (arguments.has("state")) {
            body.addProperty("state", arguments.get("state").getAsString());
        }
        if (arguments.has("kind")) {
            body.addProperty("kind", arguments.get("kind").getAsString());
        }
        if (arguments.has("priority")) {
            body.addProperty("priority", arguments.get("priority").getAsString());
        }

        return client.put("/repositories/" + workspace + "/" + repoSlug + "/issues/" + issueId, body);
    }
}
