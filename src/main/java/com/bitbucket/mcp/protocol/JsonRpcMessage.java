package com.bitbucket.mcp.protocol;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * Base class for JSON-RPC 2.0 messages
 */
public class JsonRpcMessage {
    @SerializedName("jsonrpc")
    private String jsonrpc = "2.0";

    public JsonRpcMessage() {
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }
}
