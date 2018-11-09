package org.hukehrs.kjsonrpc.server.context

import org.hukehrs.kjsonrpc.authentication.AuthenticatedUser
import org.hukehrs.kjsonrpc.server.IncomingMethodCall

data class CallContext(val user: AuthenticatedUser?, val methodCall: IncomingMethodCall)