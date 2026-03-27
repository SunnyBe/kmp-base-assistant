package com.sunday.domain.model

data class Participant(
    val id: ParticipantId,
    val displayName: String,
    val name: String,
    val avatarUrl: String?
)
