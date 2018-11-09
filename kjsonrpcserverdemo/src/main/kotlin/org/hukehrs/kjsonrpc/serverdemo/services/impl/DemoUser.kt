package org.hukehrs.kjsonrpc.serverdemo.services.impl

import org.hukehrs.kjsonrpc.authentication.JsonRpcUser

class DemoUser(val username: String, val password: String, roles: List<String>, token: String? = null): JsonRpcUser(token, roles)