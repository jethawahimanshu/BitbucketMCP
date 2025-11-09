package com.bitbucket.mcp.protocol;

import com.google.gson.JsonElement;

/**
 * JSON-RPC 2.0 Error object
 */
public class JsonRpcError {
    private int code;
    private String message;
    private JsonElement data;

    public JsonRpcError() {
    }

    public JsonRpcError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonRpcError(int code, String message, JsonElement data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    // Standard error codes
    public static final int PARSE_ERROR = -32700;
    public static final int INVALID_REQUEST = -32600;
    public static final int METHOD_NOT_FOUND = -32601;
    public static final int INVALID_PARAMS = -32602;
    public static final int INTERNAL_ERROR = -32603;
}
