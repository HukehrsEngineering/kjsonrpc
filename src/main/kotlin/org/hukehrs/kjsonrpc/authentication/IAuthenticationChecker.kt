package org.hukehrs.kjsonrpc.authentication

import org.hukehrs.kjsonrpc.annotations.NeedsAuthentication
import java.lang.reflect.Method
import kotlin.reflect.KClass

interface IAuthenticationChecker {

    companion object {
        const val NO_AUTHENTICATION = "NO_AUTHENTICATION"
    }

    fun testAuthentication(authentication: Any?, method: Method, interfaceClass: KClass<*>): Boolean

    fun needsRole(methodName: String, interfaceClass: KClass<*>): String?
    {
        val ifaceMethod = interfaceClass.java.methods.first { it.name == methodName }

        val needsAuthentication = ifaceMethod.annotations.firstOrNull { it is NeedsAuthentication }
                as NeedsAuthentication?

        return if(needsAuthentication == null || needsAuthentication.authenticationRole == NO_AUTHENTICATION)
        {
            null
        }
        else {
            needsAuthentication.authenticationRole
        }
    }
}