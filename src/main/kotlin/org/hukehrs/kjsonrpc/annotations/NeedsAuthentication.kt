package org.hukehrs.kjsonrpc.annotations

import org.hukehrs.kjsonrpc.authentication.AuthenticationChecker

@Target(AnnotationTarget.FUNCTION)
annotation class NeedsAuthentication(val authenticationRole: String = AuthenticationChecker.NO_AUTHENTICATION)