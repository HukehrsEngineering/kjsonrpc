package org.hukehrs.kjsonrpc.serverdemo.services.impl

import com.fasterxml.jackson.databind.node.ObjectNode
import org.hukehrs.kjsonrpc.authentication.AuthenticatedUser
import org.hukehrs.kjsonrpc.authentication.IAuthenticationChecker
import org.hukehrs.kjsonrpc.authentication.IAuthenticator
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import kotlin.reflect.KClass

class InMemoryAuthenticationChecker: IAuthenticationChecker, IAuthenticator {

    companion object {
        val log = LoggerFactory.getLogger(InMemoryAuthenticationChecker::class.java)
    }

    val users = mutableListOf<AuthenticatedUser>()

    override fun authenticate(token: String, vararg roles: String)
    {
        users.add(AuthenticatedUser(token, roles.toList()))
    }

    override fun testAuthentication(authentication: Any?, method: Method, interfaceClass: KClass<*>): Boolean {
        val needsRole = needsRole(method.name, interfaceClass)
        log.trace("method {} needs role {}, have authentication {}", method.name, needsRole, authentication)
        return if(needsRole == null)
        {
            true
        }
        else if(authentication == null || authentication !is ObjectNode)
        {
            false
        }
        else
        {
            return hasUserWithTokenAndRole(authentication.get("token").asText(), needsRole)
        }
    }

    private fun hasUserWithTokenAndRole(token: String, needsRole: String?): Boolean {
        return users.firstOrNull {
            it.token == token &&
                    it.roles.any { role -> role == needsRole }
        } != null
    }
}