package org.hukehrs.kjsonrpc.server

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.authorization
import io.ktor.request.receive
import io.ktor.response.respond
import org.hukehrs.kjsonrpc.authentication.IAuthenticationChecker
import org.hukehrs.kjsonrpc.jsonrpc.JsonRpcException
import org.hukehrs.kjsonrpc.jsonrpc.MethodError
import org.hukehrs.kjsonrpc.server.context.CallContextHolder
import org.slf4j.LoggerFactory

class HttpJsonRpcServer(private val services: Array<Any>, private val authenticationChecker: IAuthenticationChecker?) {

    companion object {
        val log = LoggerFactory.getLogger(HttpJsonRpcServer::class.java)

        val mapper = jacksonObjectMapper().configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
    }

    private fun callMethod(authorization: String?, call: IncomingMethodCall): OutgoingMethodResult
    {
        try {
            val method = MethodIdentifier.createFromFullName(call.method)
            val impl = findAndVerifyImplementation(method)

            if(authenticationChecker != null &&
                    !authenticationChecker.testAuthentication(authorization, impl.method, impl.iface))
            {
                throw JsonRpcException("authentication failed", JsonRpcException.errorAuthenticationFailed)
            }

            if(call.params.size != impl.method.parameterCount)
            {
                return createInvalidParamsError(call.id,
                        "given parameter count (${call.params.size} != required (${impl.method.parameterCount}")
            }

            CallContextHolder.storeCallContext(null, call)

            log.trace("starting invoke on proxy")
            val result = impl.method.invoke(impl.impl,
                    *impl.method.parameterTypes.mapIndexed {
                        index, cls -> mapper.treeToValue(call.params[index], cls)
                    }.toTypedArray())
            log.trace("proxy invoke success")

            return OutgoingMethodResult(call.id, result, null)
        }
        catch (throwable: Throwable) {
            return when(throwable) {
                is JsonRpcException -> OutgoingMethodResult(call.id, null, MethodError(throwable.code, throwable.message!!, null))
                is IllegalArgumentException -> createInvalidParamsError(call.id, throwable.message!!)
                is JsonProcessingException -> createInvalidParamsError(call.id, throwable.message!!)
                else -> OutgoingMethodResult(call.id, null, MethodError(JsonRpcException.errorServer, throwable.message
                        ?: "internal server error", null))
            }
        }
    }

    private fun createInvalidParamsError(id: Int, message: String): OutgoingMethodResult = OutgoingMethodResult(id, null, MethodError(JsonRpcException.errorInvalidParams, message, null))

    private fun findAndVerifyImplementation(method: MethodIdentifier): CallableMethod {
        val impl = services
                .filter { service -> service.javaClass.interfaces.any { it.simpleName == method.iface } &&
                        service.javaClass.methods.any { it.name == method.methodName }}
                .map { service ->
                    CallableMethod(
                            service.javaClass.methods.first { it.name == method.methodName },
                            service,
                            service.javaClass.interfaces.first { it.simpleName == method.iface }.kotlin) }
                .firstOrNull()

        if(impl != null)
        {
            log.debug("found implementation for $method: ${impl.impl} - ${impl.method}")
            return impl
        }
        else
        {
            throw JsonRpcException("no implementation found for $method", JsonRpcException.errorMethodNotFound)
        }
    }

    suspend fun handle(call: ApplicationCall)
    {
        val methodCall = call.receive<IncomingMethodCall>()

        val result = callMethod(call.request.authorization(), methodCall)

        val status = if(result.hasError() && isInternalError(result.error!!.code))
        {
            HttpStatusCode.BadRequest
        }
        else if(result.hasError())
        {
            HttpStatusCode.BadRequest
        }
        else
        {
            HttpStatusCode.OK
        }

        call.respond(status, result)
    }

    private fun isInternalError(code: Int) = code < 0
}