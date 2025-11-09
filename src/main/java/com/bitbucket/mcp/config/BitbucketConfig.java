package com.bitbucket.mcp.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration for Bitbucket MCP Server
 */
public class BitbucketConfig {
    private static final Logger logger = LoggerFactory.getLogger(BitbucketConfig.class);
    private static final String CONFIG_FILE = ".bitbucket-mcp.json";

    private String workspace;
    private String username;
    private String appPassword;
    private String accessToken;
    private String baseUrl = "https://api.bitbucket.org/2.0";

    public BitbucketConfig() {
    }

    public BitbucketConfig(String workspace, String username, String appPassword) {
        this.workspace = workspace;
        this.username = username;
        this.appPassword = appPassword;
    }

    public BitbucketConfig(String workspace, String accessToken) {
        this.workspace = workspace;
        this.accessToken = accessToken;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Check if using access token authentication
     */
    public boolean hasAccessToken() {
        return accessToken != null && !accessToken.isEmpty();
    }

    /**
     * Check if using username/password authentication
     */
    public boolean hasUsernamePassword() {
        return username != null && !username.isEmpty() &&
               appPassword != null && !appPassword.isEmpty();
    }

    /**
     * Load configuration from environment variables or config file
     */
    public static BitbucketConfig load() {
        BitbucketConfig config = new BitbucketConfig();

        // Try environment variables first
        String workspace = System.getenv("BITBUCKET_WORKSPACE");
        String username = System.getenv("BITBUCKET_USERNAME");
        String appPassword = System.getenv("BITBUCKET_APP_PASSWORD");
        String accessToken = System.getenv("BITBUCKET_ACCESS_TOKEN");
        String baseUrl = System.getenv("BITBUCKET_BASE_URL");

        if (workspace != null) config.setWorkspace(workspace);
        if (username != null) config.setUsername(username);
        if (appPassword != null) config.setAppPassword(appPassword);
        if (accessToken != null) config.setAccessToken(accessToken);
        if (baseUrl != null) config.setBaseUrl(baseUrl);

        // Try config file if no valid auth method found
        if (!config.hasAccessToken() && !config.hasUsernamePassword()) {
            config = loadFromFile(config);
        }

        return config;
    }

    /**
     * Load configuration from file
     */
    private static BitbucketConfig loadFromFile(BitbucketConfig config) {
        Path configPath = Paths.get(System.getProperty("user.home"), CONFIG_FILE);

        if (Files.exists(configPath)) {
            try (FileReader reader = new FileReader(configPath.toFile())) {
                Gson gson = new Gson();
                JsonObject json = gson.fromJson(reader, JsonObject.class);

                if (config.getWorkspace() == null && json.has("workspace")) {
                    config.setWorkspace(json.get("workspace").getAsString());
                }
                if (config.getAccessToken() == null && json.has("accessToken")) {
                    config.setAccessToken(json.get("accessToken").getAsString());
                }
                if (config.getUsername() == null && json.has("username")) {
                    config.setUsername(json.get("username").getAsString());
                }
                if (config.getAppPassword() == null && json.has("appPassword")) {
                    config.setAppPassword(json.get("appPassword").getAsString());
                }
                if (json.has("baseUrl")) {
                    config.setBaseUrl(json.get("baseUrl").getAsString());
                }

                logger.info("Loaded configuration from {}", configPath);
            } catch (IOException e) {
                logger.warn("Failed to load config file: {}", e.getMessage());
            }
        }

        return config;
    }

    public boolean isValid() {
        return hasAccessToken() || hasUsernamePassword();
    }

    /**
     * Get authentication type for logging purposes
     */
    public String getAuthType() {
        if (hasAccessToken()) {
            return "Access Token";
        } else if (hasUsernamePassword()) {
            return "Username/App Password";
        }
        return "None";
    }
}
