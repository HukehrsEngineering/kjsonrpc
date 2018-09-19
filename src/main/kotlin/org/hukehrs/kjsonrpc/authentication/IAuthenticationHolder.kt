package org.hukehrs.kjsonrpc.authentication

interface IAuthenticationHolder {
    fun getAuthentication(): Any?
}