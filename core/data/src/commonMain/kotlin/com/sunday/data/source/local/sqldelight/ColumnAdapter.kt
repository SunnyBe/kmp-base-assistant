package com.sunday.data.source.local.sqldelight

import app.cash.sqldelight.ColumnAdapter
import com.sunday.domain.model.ConversationId
import com.sunday.domain.model.MessageId
import com.sunday.domain.model.MessageStatus
import com.sunday.domain.model.ParticipantId
import com.sunday.domain.model.ServerId
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
val statusAdapter = object : ColumnAdapter<MessageStatus, String> {
    override fun decode(databaseValue: String): MessageStatus {
        return when (databaseValue) {
            "SENDING" -> MessageStatus.Sending
            "SENT" -> MessageStatus.Sent
            "DELIVERED" -> MessageStatus.Delivered(Instant.fromEpochMilliseconds(0))
            "READ" -> MessageStatus.Read(Instant.fromEpochMilliseconds(0))
            "FAILED" -> MessageStatus.Failed("Unknown error")
            else -> MessageStatus.Sending
        }
    }

    override fun encode(value: MessageStatus): String {
        return when (value) {
            is MessageStatus.Sending -> "SENDING"
            is MessageStatus.Sent -> "SENT"
            is MessageStatus.Read -> "READ"
            is MessageStatus.Delivered -> "DELIVERED"
            is MessageStatus.Failed -> "FAILED"
        }
    }
}

val conversationIdAdapter = object : ColumnAdapter<ConversationId, String> {
    override fun decode(databaseValue: String) = ConversationId(databaseValue)
    override fun encode(value: ConversationId) = value.value
}

val localIdAdapter = object : ColumnAdapter<MessageId, String> {
    override fun decode(databaseValue: String) = MessageId(databaseValue)
    override fun encode(value: MessageId) = value.value
}

val serverIdAdapter = object : ColumnAdapter<ServerId, String> {
    override fun decode(databaseValue: String) = ServerId(databaseValue)
    override fun encode(value: ServerId) = value.value
}

val senderIdAdapter = object : ColumnAdapter<ParticipantId, String> {
    override fun decode(databaseValue: String) = ParticipantId(databaseValue)
    override fun encode(value: ParticipantId) = value.value
}

val participantIdAdapter = object : ColumnAdapter<ParticipantId, String> {
    override fun decode(databaseValue: String) = ParticipantId(databaseValue)
    override fun encode(value: ParticipantId) = value.value
}