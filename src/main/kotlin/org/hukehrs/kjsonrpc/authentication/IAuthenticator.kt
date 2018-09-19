package org.hukehrs.kjsonrpc.authentication

interface IAuthenticator {
    fun authenticate(token: String, vararg roles: String)
}