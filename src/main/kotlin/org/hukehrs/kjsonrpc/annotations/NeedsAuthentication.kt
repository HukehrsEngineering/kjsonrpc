package org.hukehrs.kjsonrpc.annotations

import org.hukehrs.kjsonrpc.authentication.IAuthenticationChecker.Companion.NO_AUTHENTICATION

@Target(AnnotationTarget.FUNCTION)
annotation class NeedsAuthentication(val authenticationRole: String = NO_AUTHENTICATION)