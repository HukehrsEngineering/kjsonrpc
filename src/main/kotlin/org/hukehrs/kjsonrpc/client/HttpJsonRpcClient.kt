package org.hukehrs.kjsonrpc.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpPost
import org.hukehrs.kjsonrpc.annotations.NeedsAuthentication
import org.hukehrs.kjsonrpc.authentication.IAuthenticationHolder
import org.hukehrs.kjsonrpc.jsonrpc.JsonRpcException
import org.hukehrs.kjsonrpc.server.HttpJsonRpcServer
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

class HttpJsonRpcClient<T: Any> private constructor(private val iface: KClass<T>, private val uri: String, val authenticationHolder: IAuthenticationHolder) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {
        //some methods will be local, like toString()
        if(!Modifier.isAbstract(method.modifiers))
        {
            log.trace("skipping method {} since it is not an interface method", method.name)
            return method.invoke(proxy, args)
        }

        //check if authentication is required
        //retrieve if required and throw exception if required but not present
        val authentication = if(method.annotations.any { it is NeedsAuthentication })
        {
            val auth = authenticationHolder.getAuthentication()
                ?: throw JsonRpcException("authentication required for method ${iface.simpleName}.${method.name}, but none provided",
                        JsonRpcException.errorAuthenticationMissing)
            auth
        }
        else
        {
            null
        }

        //prepare outgoing jsonrpc call
        val methodCall = mapper.writeValueAsBytes(OutgoingMethodCall(
            id = getNextCallId(),
            method = "${iface.simpleName}.${method.name}",
            params = args ?: emptyArray(),
            authentication = authentication
        ))

        //prepare request
        val requestPreparation = uri.httpPost().body(methodCall)
        requestPreparation.headers.clear()
        requestPreparation.header(HeaderContentType to MediaTypeJson, HeaderAccept to MediaTypeJson)

        val start = System.currentTimeMillis()
        try {
            //send request
            val (_, response, result) = requestPreparation.responseString()

            val responseString = result.component1() ?: String(response.data)

            log.debug("jsonrpc request finished in {}ms: {}", System.currentTimeMillis() - start, responseString)

            //map json result
            val methodResult = mapper.readValue<IncomingMethodResult>(responseString, IncomingMethodResult::class.java)

            //if deserialization is okay and jsonrpc result object has its own error, throw this
            if (methodResult.hasError()) {
                throw JsonRpcException(methodResult.error!!.message, methodResult.error.code)
            }
            //some unexpected transport error
            if(result.component2() != null)
            {
                throw JsonRpcException((result.component2() as FuelError).message!!, JsonRpcException.errorClientServerConnection)
            }

            //map value
            return mapper.treeToValue(methodResult.result, method.returnType)
        }
        catch(e: Throwable)
        {
            throw JsonRpcException("${e.message}\n${e.stackTrace.map { it.toString()+'\n' }}", JsonRpcException.errorInternal)
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(HttpJsonRpcServer::class.java)

        val mapper = jacksonObjectMapper()

        private var callId = 0

        fun getNextCallId() = ++callId

        fun <T: Any> getForInterface(iface: KClass<T>, uri: String, authenticationHolder: IAuthenticationHolder): T
        {
            if(!iface.java.isInterface)
            {
                throw IllegalArgumentException("KClass<T> must be interface!")
            }
            log.debug("creating jsonrpc client proxy for interface {} and uri {}", iface.qualifiedName, uri)
            @Suppress("UNCHECKED_CAST")
            return Proxy.newProxyInstance(iface.java.classLoader,
                    arrayOf<Class<*>>(iface.java),
                    HttpJsonRpcClient(iface, uri, authenticationHolder)) as T
        }

        val localMethods = arrayOf(
                "toString", "equals", "hashcode"
        )

        const val MediaTypeJson = "application/json"

        const val HeaderAccept = "Accept"

        const val HeaderContentType = "Content-Type"
    }

}