package com.bitbucket.mcp.bitbucket;

import com.bitbucket.mcp.config.BitbucketConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;

/**
 * HTTP client for Bitbucket API
 */
public class BitbucketClient {
    private static final Logger logger = LoggerFactory.getLogger(BitbucketClient.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final BitbucketConfig config;
    private final Gson gson;
    private final String authHeader;

    public BitbucketClient(BitbucketConfig config) {
        this.config = config;
        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // Create basic auth header
        String credentials = config.getUsername() + ":" + config.getAppPassword();
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    /**
     * Perform GET request
     */
    public JsonObject get(String path) throws IOException {
        String url = buildUrl(path);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authHeader)
                .header("Accept", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return handleResponse(response);
        }
    }

    /**
     * Perform POST request
     */
    public JsonObject post(String path, JsonObject body) throws IOException {
        String url = buildUrl(path);
        RequestBody requestBody = RequestBody.create(gson.toJson(body), JSON);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authHeader)
                .header("Accept", "application/json")
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return handleResponse(response);
        }
    }

    /**
     * Perform PUT request
     */
    public JsonObject put(String path, JsonObject body) throws IOException {
        String url = buildUrl(path);
        RequestBody requestBody = RequestBody.create(gson.toJson(body), JSON);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authHeader)
                .header("Accept", "application/json")
                .put(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return handleResponse(response);
        }
    }

    /**
     * Perform DELETE request
     */
    public JsonObject delete(String path) throws IOException {
        String url = buildUrl(path);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authHeader)
                .header("Accept", "application/json")
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 204) {
                // No content response
                JsonObject result = new JsonObject();
                result.addProperty("success", true);
                return result;
            }
            return handleResponse(response);
        }
    }

    /**
     * Build full URL from path
     */
    private String buildUrl(String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        String baseUrl = config.getBaseUrl();
        if (path.startsWith("/")) {
            return baseUrl + path;
        }
        return baseUrl + "/" + path;
    }

    /**
     * Handle HTTP response
     */
    private JsonObject handleResponse(Response response) throws IOException {
        String bodyString = response.body() != null ? response.body().string() : "{}";

        if (!response.isSuccessful()) {
            JsonObject error = new JsonObject();
            error.addProperty("error", true);
            error.addProperty("status", response.code());
            error.addProperty("message", response.message());

            try {
                JsonObject errorBody = gson.fromJson(bodyString, JsonObject.class);
                error.add("details", errorBody);
            } catch (Exception e) {
                error.addProperty("body", bodyString);
            }

            logger.error("HTTP request failed: {} - {}", response.code(), bodyString);
            return error;
        }

        if (bodyString.isEmpty() || bodyString.equals("{}")) {
            JsonObject result = new JsonObject();
            result.addProperty("success", true);
            return result;
        }

        return gson.fromJson(bodyString, JsonObject.class);
    }

    public BitbucketConfig getConfig() {
        return config;
    }

    /**
     * Logging interceptor for debugging
     */
    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            logger.debug("HTTP {} {}", request.method(), request.url());
            return chain.proceed(request);
        }
    }
}
