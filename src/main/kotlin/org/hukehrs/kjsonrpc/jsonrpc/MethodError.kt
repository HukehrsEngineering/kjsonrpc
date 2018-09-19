package org.hukehrs.kjsonrpc.jsonrpc

data class MethodError(val code: Int, val message: String, val data: Any?)