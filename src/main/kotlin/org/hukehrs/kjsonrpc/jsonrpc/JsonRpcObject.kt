package org.hukehrs.kjsonrpc.jsonrpc

abstract class JsonRpcObject {

    companion object {
        const val jsonRpcVersion = "2.0"
    }

    @Suppress("MemberVisibilityCanBePrivate")
    val jsonrpc = jsonRpcVersion

}