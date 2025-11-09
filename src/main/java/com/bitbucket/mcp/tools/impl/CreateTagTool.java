package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateTagTool extends BaseToolExecutor {
    public CreateTagTool(BitbucketClient client) {
        super(client, "create_tag",
              "Create a new tag at a specific commit.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "tag_name", "Tag name", true);
        addStringProperty(schema, "target", "Target commit hash", true);
        addStringProperty(schema, "message", "Tag message", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String tagName = getRequiredString(arguments, "tag_name");
        String target = getRequiredString(arguments, "target");
        String message = getOptionalString(arguments, "message", null);

        JsonObject body = new JsonObject();
        body.addProperty("name", tagName);

        JsonObject targetObj = new JsonObject();
        targetObj.addProperty("hash", target);
        body.add("target", targetObj);

        if (message != null) {
            body.addProperty("message", message);
        }

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/refs/tags", body);
    }
}
