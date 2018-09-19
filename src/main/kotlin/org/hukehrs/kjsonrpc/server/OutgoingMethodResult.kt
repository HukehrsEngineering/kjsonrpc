package org.hukehrs.kjsonrpc.server

import org.hukehrs.kjsonrpc.jsonrpc.JsonRpcObject
import org.hukehrs.kjsonrpc.jsonrpc.MethodError

data class OutgoingMethodResult(val id: Int, val result: Any?, val error: MethodError?): JsonRpcObject()
{
    fun hasError(): Boolean
    {
        return error != null
    }
}