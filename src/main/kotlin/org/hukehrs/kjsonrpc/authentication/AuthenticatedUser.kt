package org.hukehrs.kjsonrpc.authentication

class AuthenticatedUser(val token: String, val roles: List<String>)