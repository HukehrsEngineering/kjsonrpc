package org.hukehrs.kjsonrpc.clientdemo

import org.hukehrs.kjsonrpc.authentication.AutoAuthenticationHolder
import org.hukehrs.kjsonrpc.client.HttpJsonRpcClient
import org.hukehrs.kjsonrpc.demo.IAuthenticationService
import org.hukehrs.kjsonrpc.demo.IDemoService
import org.hukehrs.kjsonrpc.demo.RpcEnum
import org.hukehrs.kjsonrpc.demo.RpcParameterObject

fun main(args: Array<String>)
{
    var authenticationService: IAuthenticationService? = null
    val holder = AutoAuthenticationHolder(3) {
        authenticationService?.authenticate("player1", "password1")
    }
    val demoService = HttpJsonRpcClient
            .getForInterface(IDemoService::class, "http://localhost:61200/jsonrpc", holder)
    authenticationService = HttpJsonRpcClient
            .getForInterface(IAuthenticationService::class, "http://localhost:61200/jsonrpc", holder)

    try {
        println(demoService.methodWithEnum(RpcEnum.SOMEOTHERVALUE))
    }
    catch (e: Throwable)
    {
        println(e)
        println(e.stackTrace.map { "${it.className}.${it.methodName} (${it.fileName}:${it.lineNumber})" })
    }
    println(demoService.methodWithEnum(RpcEnum.SOMEOTHERVALUE))
    println(demoService.methodWithComplexParam(RpcParameterObject("OH GO AWAY")))
}