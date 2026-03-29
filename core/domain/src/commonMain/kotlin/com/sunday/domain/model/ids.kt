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

fun String.asConversionId(): ConversationId = ConversationId(this)
fun String.asMessageId(): MessageId = MessageId(this)
fun String.asServerId(): ServerId = ServerId(this)
fun String.asParticipantId(): ParticipantId = ParticipantId(this)