package com.sunday.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

sealed interface MessageStatus {
    data object Sending : MessageStatus
    data object Sent : MessageStatus
    data class Delivered @OptIn(ExperimentalTime::class) constructor(val at: Instant) :
        MessageStatus

    data class Read @OptIn(ExperimentalTime::class) constructor(val at: Instant) : MessageStatus
    data class Failed(val cause: String) : MessageStatus
}