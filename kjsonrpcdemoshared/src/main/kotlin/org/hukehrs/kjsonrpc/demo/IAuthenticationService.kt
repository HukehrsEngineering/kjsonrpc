package org.hukehrs.kjsonrpc.demo

interface IAuthenticationService {
    fun authenticate(user: String, password: String): Authentication?
}