package org.hukehrs.kjsonrpc.serverdemo

import com.fasterxml.jackson.annotation.JsonInclude
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.hukehrs.kjsonrpc.serverdemo.services.impl.InMemoryAuthenticationChecker
import org.hukehrs.kjsonrpc.server.HttpJsonRpcServer
import org.hukehrs.kjsonrpc.serverdemo.services.impl.AuthenticationService
import org.hukehrs.kjsonrpc.serverdemo.services.impl.ServerDemoService

fun main(args: Array<String>)
{
    val authenticationChecker = InMemoryAuthenticationChecker()
    val jsonRpcServer = HttpJsonRpcServer(
            arrayOf(ServerDemoService(), AuthenticationService(authenticationChecker)),
            authenticationChecker)

    val server = embeddedServer(Netty, port = 61200) {
        install(ContentNegotiation) {
            jackson {
                setSerializationInclusion(JsonInclude.Include.NON_NULL)
            }
        }
        install(CallLogging)
        routing {
            post("/jsonrpc") {
                jsonRpcServer.handle(call)
            }
        }
    }
    server.start(wait = true)
}