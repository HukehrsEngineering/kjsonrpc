package org.hukehrs.kjsonrpc.authentication

import org.slf4j.LoggerFactory

class AutoAuthenticationHolder<T>(val maxTries: Int, val provider: () -> T?): IAuthenticationHolder {

    companion object {
        val log = LoggerFactory.getLogger(AutoAuthenticationHolder::class.java)
    }

    private var authentication: T? = null

    private var currentTries = 0

    override fun getAuthentication(): Any? {
        if(authentication == null && currentTries < maxTries)
        {
            log.debug("authentication is null, getting from provider (try {})", currentTries+1)
            authentication = provider()
            if(authentication == null)
            {
                log.error("authentication still null")
                currentTries++
            }
            else
            {
                log.trace("got authentication {}", authentication)
            }
        }
        return authentication
    }
}