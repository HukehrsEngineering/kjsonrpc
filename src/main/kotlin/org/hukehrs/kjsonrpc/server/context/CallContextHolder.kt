package org.hukehrs.kjsonrpc.server.context

import org.hukehrs.kjsonrpc.authentication.AuthenticatedUser
import org.hukehrs.kjsonrpc.server.IncomingMethodCall

object CallContextHolder {

    private val contexts = mutableMapOf<String, CallContext>()

    fun getCallContext(): CallContext
    {
        val name = getContextNameForThread()

        return contexts[name] ?: throw NoCallContextException(name)
    }

    fun storeCallContext(authenticatedUser: AuthenticatedUser?, methodCall: IncomingMethodCall)
    {
        val name = getContextNameForThread()

        contexts[name] = CallContext(authenticatedUser, methodCall)
    }

    private fun getContextNameForThread(): String {
        return Thread.currentThread().name
    }

}