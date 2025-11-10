package com.bitbucket.mcp.protocol;

import com.google.gson.JsonElement;

/**
 * JSON-RPC 2.0 Response message
 */
public class JsonRpcResponse extends JsonRpcMessage {
    private JsonElement id;  // Changed from Object to JsonElement to preserve type
    private JsonElement result;
    private JsonRpcError error;

    public JsonRpcResponse() {
    }

    public JsonRpcResponse(JsonElement id, JsonElement result) {
        this.id = id;
        this.result = result;
    }

    public JsonRpcResponse(JsonElement id, JsonRpcError error) {
        this.id = id;
        this.error = error;
    }

    public JsonElement getId() {
        return id;
    }

    public void setId(JsonElement id) {
        this.id = id;
    }

    public JsonElement getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }

    public JsonRpcError getError() {
        return error;
    }

    public void setError(JsonRpcError error) {
        this.error = error;
    }
}
