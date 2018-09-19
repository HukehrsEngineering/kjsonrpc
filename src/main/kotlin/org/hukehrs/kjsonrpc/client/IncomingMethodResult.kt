package org.hukehrs.kjsonrpc.client

import com.fasterxml.jackson.databind.JsonNode
import org.hukehrs.kjsonrpc.jsonrpc.JsonRpcObject
import org.hukehrs.kjsonrpc.jsonrpc.MethodError

data class IncomingMethodResult(val id: Int, val result: JsonNode?, val error: MethodError?): JsonRpcObject()
{
    fun hasError(): Boolean
    {
        return error != null
    }
}