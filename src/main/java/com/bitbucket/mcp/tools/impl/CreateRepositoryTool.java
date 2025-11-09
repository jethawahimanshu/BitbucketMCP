package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class CreateRepositoryTool extends BaseToolExecutor {
    public CreateRepositoryTool(BitbucketClient client) {
        super(client, "create_repository",
              "Create a new repository in a workspace with specified name, description, and settings.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID or username", false);
        addStringProperty(schema, "repo_slug", "Repository slug/name", true);
        addStringProperty(schema, "description", "Repository description", false);
        addBooleanProperty(schema, "is_private", "Whether repository is private", false);
        addStringProperty(schema, "scm", "Source control management (git or hg)", false);
        addStringProperty(schema, "project_key", "Project key to add repository to", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String description = getOptionalString(arguments, "description", "");
        Boolean isPrivate = getOptionalBoolean(arguments, "is_private", true);
        String scm = getOptionalString(arguments, "scm", "git");
        String projectKey = getOptionalString(arguments, "project_key", null);

        JsonObject body = new JsonObject();
        body.addProperty("scm", scm);
        body.addProperty("is_private", isPrivate);
        if (!description.isEmpty()) {
            body.addProperty("description", description);
        }
        if (projectKey != null) {
            JsonObject project = new JsonObject();
            project.addProperty("key", projectKey);
            body.add("project", project);
        }

        return client.post("/repositories/" + workspace + "/" + repoSlug, body);
    }
}
