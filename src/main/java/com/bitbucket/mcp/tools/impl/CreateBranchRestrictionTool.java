package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CreateBranchRestrictionTool extends BaseToolExecutor {
    public CreateBranchRestrictionTool(BitbucketClient client) {
        super(client, "create_branch_restriction",
              "Create a branch restriction rule (e.g., require PR, prevent deletion, enforce merge checks).");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        addStringProperty(schema, "repo_slug", "Repository slug", true);
        addStringProperty(schema, "kind", "Restriction kind: push, force, delete, restrict_merges, require_approvals_to_merge", true);
        addStringProperty(schema, "pattern", "Branch name pattern (e.g., master, develop, release/*)", true);
        addNumberProperty(schema, "value", "Value for restriction (e.g., number of approvals required)", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        String repoSlug = getRequiredString(arguments, "repo_slug");
        String kind = getRequiredString(arguments, "kind");
        String pattern = getRequiredString(arguments, "pattern");
        Integer value = getOptionalInt(arguments, "value", null);

        JsonObject body = new JsonObject();
        body.addProperty("kind", kind);
        body.addProperty("pattern", pattern);
        if (value != null) {
            body.addProperty("value", value);
        }

        return client.post("/repositories/" + workspace + "/" + repoSlug + "/branch-restrictions", body);
    }
}
