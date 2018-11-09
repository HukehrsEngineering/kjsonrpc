package org.hukehrs.kjsonrpc.authentication

import org.hukehrs.kjsonrpc.annotations.NeedsAuthentication
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import kotlin.reflect.KClass

class AuthenticationChecker {

    companion object {
        val log = LoggerFactory.getLogger(AuthenticationChecker::class.java)

        const val NO_AUTHENTICATION = "NO_AUTHENTICATION"
    }

    fun testAuthentication(user: JsonRpcUser?, method: Method, interfaceClass: KClass<*>): Boolean {
        val needsRole = needsRole(method, interfaceClass)
        log.trace("method {} needs role {}, have user {}", method.name, needsRole, user)
        return if(needsRole == null)
        {
            true
        }
        else if(user == null)
        {
            false
        }
        else
        {
            return userHasRole(user, needsRole)
        }
    }

    fun needsRole(method: Method, interfaceClass: KClass<*>): String?
    {
        val ifaceMethod = interfaceClass.java.methods.first {
            it.name == method.name && it.parameterTypes?.contentEquals(method.parameterTypes) == true
        }

        val needsAuthentication = ifaceMethod
                .annotations
                .filterIsInstance(NeedsAuthentication::class.java)
                .firstOrNull()

        return if(needsAuthentication == null || needsAuthentication.authenticationRole == NO_AUTHENTICATION)
        {
            null
        }
        else {
            needsAuthentication.authenticationRole
        }
    }

    private fun userHasRole(user: JsonRpcUser, needsRole: String?): Boolean {
        return user.roles.any { role -> role == needsRole }
    }
}