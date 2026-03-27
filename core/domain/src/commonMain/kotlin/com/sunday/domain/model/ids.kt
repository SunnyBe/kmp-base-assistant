package com.sunday.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class MessageId(val value: String)

@JvmInline
value class ServerId(val value: String)

@JvmInline
value class ConversationId(val value: String)

@JvmInline
value class ParticipantId(val value: String)