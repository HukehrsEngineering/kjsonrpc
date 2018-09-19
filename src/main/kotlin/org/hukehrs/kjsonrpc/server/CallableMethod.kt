package org.hukehrs.kjsonrpc.server

import java.lang.reflect.Method
import kotlin.reflect.KClass

data class CallableMethod(val method: Method, val impl: Any, val iface: KClass<*>)