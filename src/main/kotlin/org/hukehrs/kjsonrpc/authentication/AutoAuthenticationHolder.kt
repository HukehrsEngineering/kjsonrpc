package org.hukehrs.kjsonrpc.authentication

import org.slf4j.LoggerFactory

class AutoAuthenticationHolder(val maxTries: Int, val provider: () -> String?) : IAuthenticationHolder {

    companion object {
        val log = LoggerFactory.getLogger(AutoAuthenticationHolder::class.java)
    }

    private var authentication: String? = null

    private var currentTries = 0

    override fun getAuthentication(): String? {
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