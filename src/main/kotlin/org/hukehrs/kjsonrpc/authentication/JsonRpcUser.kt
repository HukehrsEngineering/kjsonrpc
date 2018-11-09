package org.hukehrs.kjsonrpc.authentication

abstract class JsonRpcUser(var token: String?, val roles: List<String>)