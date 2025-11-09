package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CreateWebhookTool extends BaseToolExecutor {
    public CreateWebhookTool(BitbucketClient client) {
        super(client, "create_webhook",
              "Create a new webhook for a repository with specified URL and events.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "description", "Webhook description", true);
        addStringProperty(schema, "url", "Webhook URL", true);
        addStringProperty(schema, "events", "Comma-separated events (e.g., repo:push,pullrequest:created)", true);
        addBooleanProperty(schema, "active", "Whether webhook is active", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String description = getRequiredString(arguments, "description");
        String url = getRequiredString(arguments, "url");
        String eventsStr = getRequiredString(arguments, "events");
        Boolean active = getOptionalBoolean(arguments, "active", true);

        JsonObject body = new JsonObject();
        body.addProperty("description", description);
        body.addProperty("url", url);
        body.addProperty("active", active);

        JsonArray events = new JsonArray();
        for (String event : eventsStr.split(",")) {
            events.add(event.trim());
        }
        body.add("events", events);

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/hooks", body);
    }
}
