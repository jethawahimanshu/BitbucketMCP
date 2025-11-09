package com.bitbucket.mcp.protocol;

/**
 * Server information for MCP initialization
 */
public class ServerInfo {
    private String name;
    private String version;
    private ServerCapabilities capabilities;

    public ServerInfo() {
    }

    public ServerInfo(String name, String version) {
        this.name = name;
        this.version = version;
        this.capabilities = new ServerCapabilities();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ServerCapabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(ServerCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public static class ServerCapabilities {
        private ToolsCapability tools;
        private ResourcesCapability resources;
        private PromptsCapability prompts;

        public ServerCapabilities() {
            this.tools = new ToolsCapability();
            this.resources = new ResourcesCapability();
            this.prompts = new PromptsCapability();
        }

        public ToolsCapability getTools() {
            return tools;
        }

        public void setTools(ToolsCapability tools) {
            this.tools = tools;
        }

        public ResourcesCapability getResources() {
            return resources;
        }

        public void setResources(ResourcesCapability resources) {
            this.resources = resources;
        }

        public PromptsCapability getPrompts() {
            return prompts;
        }

        public void setPrompts(PromptsCapability prompts) {
            this.prompts = prompts;
        }
    }

    public static class ToolsCapability {
        // Empty object to indicate tools are supported
    }

    public static class ResourcesCapability {
        private boolean subscribe = false;

        public boolean isSubscribe() {
            return subscribe;
        }

        public void setSubscribe(boolean subscribe) {
            this.subscribe = subscribe;
        }
    }

    public static class PromptsCapability {
        private boolean listChanged = false;

        public boolean isListChanged() {
            return listChanged;
        }

        public void setListChanged(boolean listChanged) {
            this.listChanged = listChanged;
        }
    }
}
