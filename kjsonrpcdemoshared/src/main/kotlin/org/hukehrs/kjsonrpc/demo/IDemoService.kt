package org.hukehrs.kjsonrpc.demo

import org.hukehrs.kjsonrpc.annotations.NeedsAuthentication

interface IDemoService {

    @NeedsAuthentication("USER")
    fun methodWithEnum(rpcEnum: RpcEnum): SystemState


    @NeedsAuthentication("USER")
    fun methodWithComplexParam(rpcParameterObject: RpcParameterObject): SystemState
}