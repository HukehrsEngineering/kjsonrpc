package org.hukehrs.kjsonrpc.serverdemo.services.impl

import org.hukehrs.kjsonrpc.authentication.IUserService
import org.hukehrs.kjsonrpc.authentication.JsonRpcUser

class InMemoryUserService: IUserService {

    private val users = mutableListOf(
            DemoUser("player1", "password1", listOf("USER")), DemoUser("player2", "password2", listOf("USER"))
    )

    override fun findUserByUsernameOrNull(username: String): JsonRpcUser? {
        return users.firstOrNull { it.username == username }
    }

    override fun findUserByTokenOrNull(token: String): JsonRpcUser? {
        return users.firstOrNull { it.token == token }
    }
}