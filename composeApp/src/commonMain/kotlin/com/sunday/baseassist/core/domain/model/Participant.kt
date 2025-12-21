package com.sunday.baseassist.core.domain.model

import kotlin.jvm.JvmInline

data class Participant(
    val id: ParticipantId,
    val displayName: String,
    val name: String,
    val avatarUrl: String?
)

@JvmInline
value class ParticipantId(val value: String)
