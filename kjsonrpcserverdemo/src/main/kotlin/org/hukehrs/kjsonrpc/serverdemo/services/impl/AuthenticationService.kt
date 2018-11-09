package org.hukehrs.kjsonrpc.serverdemo.services.impl

import org.hukehrs.kjsonrpc.demo.Authentication
import org.hukehrs.kjsonrpc.demo.IAuthenticationService
import org.hukehrs.kjsonrpc.server.context.CallContextHolder
import org.slf4j.LoggerFactory
import java.util.*

class AuthenticationService(private val authenticationChecker: InMemoryAuthenticationChecker): IAuthenticationService {

    companion object {
        val log = LoggerFactory.getLogger(AuthenticationService::class.java)
    }

    private val users = mutableMapOf(
        "player1" to "password1", "player2" to "password2"
    )

    override fun authenticate(user: String, password: String): Authentication? {
        log.trace("isOnThread: {}", Thread.currentThread().name)
        log.trace("call context {}", CallContextHolder.getCallContext())
        return if(users.any { it.key == user && it.value == password})
        {
            val token = UUID.randomUUID().toString()
            authenticationChecker.authenticate(token, "USER")
            Authentication(token)
        }
        else
        {
            null
        }
    }

}