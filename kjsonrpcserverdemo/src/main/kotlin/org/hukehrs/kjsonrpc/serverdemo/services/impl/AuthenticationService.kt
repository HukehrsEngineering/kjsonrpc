package org.hukehrs.kjsonrpc.serverdemo.services.impl

import org.hukehrs.kjsonrpc.demo.Authentication
import org.hukehrs.kjsonrpc.demo.IAuthenticationService
import java.util.*

class AuthenticationService(private val authenticationChecker: InMemoryAuthenticationChecker): IAuthenticationService {

    private val users = mutableMapOf(
        "player1" to "password1", "player2" to "password2"
    )

    override fun authenticate(user: String, password: String): Authentication? {
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