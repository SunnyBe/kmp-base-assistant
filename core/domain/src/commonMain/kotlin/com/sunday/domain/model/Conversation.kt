package com.sunday.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Conversation @OptIn(ExperimentalTime::class) constructor(
    val id: ConversationId,
    val participantId: ParticipantId,
    val participantName: String,
    val participantAvatar: String?,
    val lastMessageSnippet: String?,
    val unreadCount: Long,
    val updatedAt: Instant,
)