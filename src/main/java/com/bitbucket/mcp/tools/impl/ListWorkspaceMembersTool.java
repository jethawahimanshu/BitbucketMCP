package com.bitbucket.mcp.tools.impl;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.tools.BaseToolExecutor;
import com.google.gson.JsonObject;

public class ListWorkspaceMembersTool extends BaseToolExecutor {
    public ListWorkspaceMembersTool(BitbucketClient client) {
        super(client, "list_workspace_members",
              "List all members of a workspace with their permissions.");
    }

    @Override
    protected JsonObject createInputSchema() {
        JsonObject schema = createSchema();
        addStringProperty(schema, "workspace", "Workspace ID", false);
        return schema;
    }

    @Override
    public JsonObject execute(JsonObject arguments) throws Exception {
        String workspace = getOptionalString(arguments, "workspace", client.getConfig().getWorkspace());
        return client.get("/workspaces/" + workspace + "/members");
    }
}
