package com.sunday.baseassist.core.domain.model

import kotlin.jvm.JvmInline

data class Conversation(
    val id: ConversationId,
    val participants: List<ParticipantId>,
    val preview: String
)

@JvmInline
value class ConversationId(val value: String)