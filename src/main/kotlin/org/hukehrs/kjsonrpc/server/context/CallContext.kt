package org.hukehrs.kjsonrpc.server.context

import org.hukehrs.kjsonrpc.authentication.JsonRpcUser
import org.hukehrs.kjsonrpc.server.IncomingMethodCall

data class CallContext(val user: JsonRpcUser?, val methodCall: IncomingMethodCall)