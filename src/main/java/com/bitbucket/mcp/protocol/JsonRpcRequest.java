package com.bitbucket.mcp.protocol;

import com.google.gson.JsonElement;

/**
 * JSON-RPC 2.0 Request message
 */
public class JsonRpcRequest extends JsonRpcMessage {
    private String method;
    private JsonElement params;
    private Object id;

    public JsonRpcRequest() {
    }

    public JsonRpcRequest(String method, JsonElement params, Object id) {
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

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
