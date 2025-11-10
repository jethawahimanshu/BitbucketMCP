package com.bitbucket.mcp.protocol;

import com.google.gson.JsonElement;

/**
 * JSON-RPC 2.0 Request message
 */
public class JsonRpcRequest extends JsonRpcMessage {
    private String method;
    private JsonElement params;
    private JsonElement id;  // Changed from Object to JsonElement to preserve type

    public JsonRpcRequest() {
    }

    public JsonRpcRequest(String method, JsonElement params, JsonElement id) {
        this.method = method;
        this.params = params;
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JsonElement getParams() {
        return params;
    }

    public void setParams(JsonElement params) {
        this.params = params;
    }

    public JsonElement getId() {
        return id;
    }

    public void setId(JsonElement id) {
        this.id = id;
    }
}
