package org.hukehrs.kjsonrpc.demo

interface IAuthenticationService {
    fun authenticate(username: String, password: String): Authentication?
}