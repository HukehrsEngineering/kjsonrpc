package org.hukehrs.kjsonrpc.serverdemo.services.impl

import org.hukehrs.kjsonrpc.authentication.IUserService
import org.hukehrs.kjsonrpc.demo.Authentication
import org.hukehrs.kjsonrpc.demo.IAuthenticationService
import org.hukehrs.kjsonrpc.server.context.CallContextHolder
import org.slf4j.LoggerFactory
import java.util.*

class AuthenticationService(private val userService: IUserService): IAuthenticationService {

    companion object {
        val log = LoggerFactory.getLogger(AuthenticationService::class.java)
    }

    override fun authenticate(username: String, password: String): Authentication? {
        log.trace("isOnThread: {}", Thread.currentThread().name)
        log.trace("call context {}", CallContextHolder.getCallContext())

        val user = userService.findUserByUsernameOrNull(username) as DemoUser?

        return if(user != null && user.password == password)
        {
            user.token = UUID.randomUUID().toString()
            Authentication(user.token!!)
        }
        else
        {
            null
        }
    }

}