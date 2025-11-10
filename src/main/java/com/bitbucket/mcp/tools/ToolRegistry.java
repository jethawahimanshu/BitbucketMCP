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
     * Register all available tools - Complete exhaustive Bitbucket API coverage
     */
    private void registerAllTools() {
        // Repository tools (6)
        registerTool(new ListRepositoriesTool(client));
        registerTool(new GetRepositoryTool(client));
        registerTool(new CreateRepositoryTool(client));
        registerTool(new DeleteRepositoryTool(client));
        registerTool(new ForkRepositoryTool(client));
        registerTool(new UpdateRepositoryTool(client));

        // Pull Request tools (24)
        registerTool(new ListPullRequestsTool(client));
        registerTool(new GetPullRequestTool(client));
        registerTool(new CreatePullRequestTool(client));
        registerTool(new UpdatePullRequestTool(client));
        registerTool(new MergePullRequestTool(client));
        registerTool(new DeclinePullRequestTool(client));
        registerTool(new ApprovePullRequestTool(client));
        registerTool(new UnapprovePullRequestTool(client));
        registerTool(new ListPRCommentsTool(client));
        registerTool(new AddPRCommentTool(client));
        registerTool(new AddPRInlineCommentTool(client));
        registerTool(new GetPRCommentTool(client));
        registerTool(new UpdatePRCommentTool(client));
        registerTool(new DeletePRCommentTool(client));
        registerTool(new GetPRDiffTool(client));
        registerTool(new GetPRDiffStatTool(client));
        registerTool(new GetPRCommitsTool(client));
        registerTool(new GetPRActivityTool(client));
        registerTool(new GetPRStatusesTool(client));
        registerTool(new AddPRReviewersTool(client));
        registerTool(new ListPRTasksTool(client));
        registerTool(new CreatePRTaskTool(client));
        registerTool(new UpdatePRTaskTool(client));
        registerTool(new DeletePRTaskTool(client));

        // Commit tools (9)
        registerTool(new ListCommitsTool(client));
        registerTool(new GetCommitTool(client));
        registerTool(new ListCommitStatusesTool(client));
        registerTool(new CreateCommitStatusTool(client));
        registerTool(new GetCommitDiffTool(client));
        registerTool(new ListCommitCommentsTool(client));
        registerTool(new AddCommitCommentTool(client));
        registerTool(new GetCommitPatchTool(client));
        registerTool(new GetPRPatchTool(client));

        // Branch tools (7)
        registerTool(new ListBranchesTool(client));
        registerTool(new GetBranchTool(client));
        registerTool(new CreateBranchTool(client));
        registerTool(new DeleteBranchTool(client));
        registerTool(new ListBranchRestrictionsTool(client));
        registerTool(new CreateBranchRestrictionTool(client));
        registerTool(new DeleteBranchRestrictionTool(client));

        // Pipeline tools (13)
        registerTool(new ListPipelinesTool(client));
        registerTool(new GetPipelineTool(client));
        registerTool(new RunPipelineTool(client));
        registerTool(new StopPipelineTool(client));
        registerTool(new GetPipelineStepsTool(client));
        registerTool(new GetPipelineStepLogTool(client));
        registerTool(new ListPipelineVariablesTool(client));
        registerTool(new CreatePipelineVariableTool(client));
        registerTool(new ListPipelineSchedulesTool(client));
        registerTool(new GetPipelineScheduleTool(client));
        registerTool(new CreatePipelineScheduleTool(client));
        registerTool(new UpdatePipelineScheduleTool(client));
        registerTool(new DeletePipelineScheduleTool(client));

        // Deployment tools (6)
        registerTool(new ListDeploymentsTool(client));
        registerTool(new GetDeploymentTool(client));
        registerTool(new ListEnvironmentsTool(client));
        registerTool(new GetEnvironmentTool(client));
        registerTool(new CreateEnvironmentTool(client));
        registerTool(new DeleteEnvironmentTool(client));

        // Webhook tools (3)
        registerTool(new ListWebhooksTool(client));
        registerTool(new CreateWebhookTool(client));
        registerTool(new DeleteWebhookTool(client));

        // Workspace & Project tools (9)
        registerTool(new ListWorkspacesTool(client));
        registerTool(new GetWorkspaceTool(client));
        registerTool(new ListProjectsTool(client));
        registerTool(new CreateProjectTool(client));
        registerTool(new GetProjectTool(client));
        registerTool(new UpdateProjectTool(client));
        registerTool(new DeleteProjectTool(client));
        registerTool(new ListWorkspaceMembersTool(client));

        // File/Source tools (5)
        registerTool(new GetFileContentTool(client));
        registerTool(new ListDirectoryTool(client));
        registerTool(new CreateFileTool(client));
        registerTool(new UpdateFileTool(client));
        registerTool(new GetFileHistoryTool(client));

        // Issue tools (14)
        registerTool(new ListIssuesTool(client));
        registerTool(new GetIssueTool(client));
        registerTool(new CreateIssueTool(client));
        registerTool(new UpdateIssueTool(client));
        registerTool(new DeleteIssueTool(client));
        registerTool(new ListIssueCommentsTool(client));
        registerTool(new AddIssueCommentTool(client));
        registerTool(new ListIssueWatchersTool(client));
        registerTool(new WatchIssueTool(client));
        registerTool(new UnwatchIssueTool(client));
        registerTool(new VoteIssueTool(client));
        registerTool(new UnvoteIssueTool(client));
        registerTool(new ListIssueVotersTool(client));

        // Search tools (2)
        registerTool(new SearchRepositoriesTool(client));
        registerTool(new SearchCodeTool(client));

        // Tag tools (4)
        registerTool(new ListTagsTool(client));
        registerTool(new CreateTagTool(client));
        registerTool(new GetTagTool(client));
        registerTool(new DeleteTagTool(client));

        // User tools (2)
        registerTool(new GetCurrentUserTool(client));
        registerTool(new GetUserTool(client));

        // Watchers & Forks tools (4)
        registerTool(new ListWatchersTool(client));
        registerTool(new ListForksTool(client));
        registerTool(new WatchRepositoryTool(client));
        registerTool(new UnwatchRepositoryTool(client));

        // Default Reviewers tools (3)
        registerTool(new ListDefaultReviewersTool(client));
        registerTool(new AddDefaultReviewerTool(client));
        registerTool(new RemoveDefaultReviewerTool(client));

        // SSH Keys tools (3)
        registerTool(new ListSSHKeysTool(client));
        registerTool(new CreateSSHKeyTool(client));
        registerTool(new DeleteSSHKeyTool(client));

        // Deploy Keys tools (3)
        registerTool(new ListDeployKeysTool(client));
        registerTool(new CreateDeployKeyTool(client));
        registerTool(new DeleteDeployKeyTool(client));

        // Downloads tools (2)
        registerTool(new ListDownloadsTool(client));
        registerTool(new DeleteDownloadTool(client));

        // Snippets tools (5)
        registerTool(new ListSnippetsTool(client));
        registerTool(new GetSnippetTool(client));
        registerTool(new CreateSnippetTool(client));
        registerTool(new UpdateSnippetTool(client));
        registerTool(new DeleteSnippetTool(client));

        // Repository Variables tools (4)
        registerTool(new ListRepositoryVariablesTool(client));
        registerTool(new CreateRepositoryVariableTool(client));
        registerTool(new UpdateRepositoryVariableTool(client));
        registerTool(new DeleteRepositoryVariableTool(client));

        // Milestones tools (4)
        registerTool(new ListMilestonesTool(client));
        registerTool(new GetMilestoneTool(client));
        registerTool(new CreateMilestoneTool(client));
        registerTool(new DeleteMilestoneTool(client));

        // Components tools (4)
        registerTool(new ListComponentsTool(client));
        registerTool(new GetComponentTool(client));
        registerTool(new CreateComponentTool(client));
        registerTool(new DeleteComponentTool(client));

        // Versions tools (4)
        registerTool(new ListVersionsTool(client));
        registerTool(new GetVersionTool(client));
        registerTool(new CreateVersionTool(client));
        registerTool(new DeleteVersionTool(client));

        logger.info("Registered {} exhaustive Bitbucket tools", tools.size());
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
