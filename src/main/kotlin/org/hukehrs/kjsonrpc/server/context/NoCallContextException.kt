package org.hukehrs.kjsonrpc.server.context

class NoCallContextException(@Suppress("CanBeParameter") val threadName: String): Exception("No call context for thread $threadName")