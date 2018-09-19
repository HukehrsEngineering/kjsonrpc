package org.hukehrs.kjsonrpc.serverdemo.services.impl

import org.hukehrs.kjsonrpc.demo.RpcParameterObject
import org.hukehrs.kjsonrpc.demo.SystemState
import org.hukehrs.kjsonrpc.demo.IDemoService
import org.hukehrs.kjsonrpc.demo.RpcEnum
import org.slf4j.LoggerFactory

class ServerDemoService: IDemoService {

    companion object {
        val log = LoggerFactory.getLogger(ServerDemoService::class.java)
    }

    override fun methodWithEnum(rpcEnum: RpcEnum): SystemState {
        log.trace("ServerDemoService.methodWithEnum($rpcEnum)")
        return SystemState().apply {
            lastAction = rpcEnum
        }
    }

    override fun methodWithComplexParam(rpcParameterObject: RpcParameterObject): SystemState {
        log.trace("ServerDemoService.methodWithComplexParam($rpcParameterObject)")
        return SystemState().apply {
            lastAction = rpcParameterObject
        }
    }

}