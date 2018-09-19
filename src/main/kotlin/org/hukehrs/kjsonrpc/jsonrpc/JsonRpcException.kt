package org.hukehrs.kjsonrpc.jsonrpc

class JsonRpcException(message: String, val code: Int): RuntimeException(message)
{
    companion object {
        const val errorMethodNotFound = -32601
        const val errorInvalidParams = -32602
        const val errorInternal = -32603
        const val errorAuthenticationFailed = -32604
        const val errorAuthenticationMissing = -32605
        const val errorInvalidRequest = -32600
        const val errorServer = -32000
        const val errorClientServerConnection = -32001
    }
}