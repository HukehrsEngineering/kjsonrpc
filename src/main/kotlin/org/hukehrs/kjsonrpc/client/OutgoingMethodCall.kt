package org.hukehrs.kjsonrpc.client

import org.hukehrs.kjsonrpc.jsonrpc.JsonRpcObject
import java.util.*

data class OutgoingMethodCall(val id: Int, val method: String, val params: Array<Any>, val authentication: Any? = null): JsonRpcObject() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OutgoingMethodCall

        if (id != other.id) return false
        if (method != other.method) return false
        if (!Arrays.equals(params, other.params)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + method.hashCode()
        result = 31 * result + Arrays.hashCode(params)
        return result
    }
}