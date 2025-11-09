package com.bitbucket.mcp.protocol;

import com.google.gson.JsonElement;

/**
 * JSON-RPC 2.0 Response message
 */
public class JsonRpcResponse extends JsonRpcMessage {
    private Object id;
    private JsonElement result;
    private JsonRpcError error;

    public JsonRpcResponse() {
    }

    public JsonRpcResponse(Object id, JsonElement result) {
        this.id = id;
        this.result = result;
    }

    public JsonRpcResponse(Object id, JsonRpcError error) {
        this.id = id;
        this.error = error;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
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
