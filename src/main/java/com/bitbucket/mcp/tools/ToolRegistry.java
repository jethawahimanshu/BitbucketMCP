package com.bitbucket.mcp.tools;

import com.bitbucket.mcp.bitbucket.BitbucketClient;
import com.bitbucket.mcp.protocol.MCPTool;
import com.bitbucket.mcp.tools.impl.*;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Registry for all MCP tools
 */
public class ToolRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ToolRegistry.class);

    private final Map<String, ToolExecutor> tools = new HashMap<>();
    private final BitbucketClient client;

    public ToolRegistry(BitbucketClient client) {
        this.client = client;
        registerAllTools();
    }

    /**
     * Register all available tools
     */
    private void registerAllTools() {
        // Repository tools
        registerTool(new ListRepositoriesTool(client));
        registerTool(new GetRepositoryTool(client));
        registerTool(new CreateRepositoryTool(client));
        registerTool(new DeleteRepositoryTool(client));
        registerTool(new ForkRepositoryTool(client));

        // Pull Request tools
        registerTool(new ListPullRequestsTool(client));
        registerTool(new GetPullRequestTool(client));
        registerTool(new CreatePullRequestTool(client));
        registerTool(new UpdatePullRequestTool(client));
        registerTool(new MergePullRequestTool(client));
        registerTool(new DeclinePullRequestTool(client));
        registerTool(new ApprovePullRequestTool(client));
        registerTool(new UnapproveP ullRequestTool(client));
        registerTool(new ListPRCommentsTool(client));
        registerTool(new AddPRCommentTool(client));

        // Commit tools
        registerTool(new ListCommitsTool(client));
        registerTool(new GetCommitTool(client));
        registerTool(new ListCommitStatusesTool(client));
        registerTool(new CreateCommitStatusTool(client));

        // Branch tools
        registerTool(new ListBranchesTool(client));
        registerTool(new GetBranchTool(client));
        registerTool(new CreateBranchTool(client));
        registerTool(new DeleteBranchTool(client));

        // Pipeline tools
        registerTool(new ListPipelinesTool(client));
        registerTool(new GetPipelineTool(client));
        registerTool(new RunPipelineTool(client));
        registerTool(new StopPipelineTool(client));

        // Deployment tools
        registerTool(new ListDeploymentsTool(client));
        registerTool(new GetDeploymentTool(client));

        // Webhook tools
        registerTool(new ListWebhooksTool(client));
        registerTool(new CreateWebhookTool(client));
        registerTool(new DeleteWebhookTool(client));

        // Workspace & Project tools
        registerTool(new ListWorkspacesTool(client));
        registerTool(new GetWorkspaceTool(client));
        registerTool(new ListProjectsTool(client));
        registerTool(new CreateProjectTool(client));

        // File/Source tools
        registerTool(new GetFileContentTool(client));
        registerTool(new ListDirectoryTool(client));

        // Issue tools
        registerTool(new ListIssuesTool(client));
        registerTool(new GetIssueTool(client));
        registerTool(new CreateIssueTool(client));

        // Search tools
        registerTool(new SearchRepositoriesTool(client));
        registerTool(new SearchCodeTool(client));

        // Tag tools
        registerTool(new ListTagsTool(client));
        registerTool(new CreateTagTool(client));

        // User tools
        registerTool(new GetCurrentUserTool(client));
        registerTool(new GetUserTool(client));

        logger.info("Registered {} tools", tools.size());
    }

    /**
     * Register a single tool
     */
    private void registerTool(ToolExecutor executor) {
        tools.put(executor.getTool().getName(), executor);
    }

    /**
     * Get all registered tools
     */
    public List<MCPTool> getAllTools() {
        List<MCPTool> toolList = new ArrayList<>();
        for (ToolExecutor executor : tools.values()) {
            toolList.add(executor.getTool());
        }
        return toolList;
    }

    /**
     * Execute a tool by name
     */
    public JsonObject executeTool(String toolName, JsonObject arguments) throws Exception {
        ToolExecutor executor = tools.get(toolName);
        if (executor == null) {
            throw new IllegalArgumentException("Unknown tool: " + toolName);
        }

        logger.info("Executing tool: {}", toolName);
        return executor.execute(arguments);
    }
}
