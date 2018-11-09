package org.hukehrs.kjsonrpc.authentication

interface IUserService {

    fun findUserByUsernameOrNull(username: String): JsonRpcUser?

    fun findUserByTokenOrNull(token: String): JsonRpcUser?

}