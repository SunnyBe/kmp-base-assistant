package com.sunday.data.utils

import com.sunday.data.local.ConversationEntity
import com.sunday.data.local.MessageEntity
import com.sunday.data.local.ParticipantEntity
import com.sunday.data.source.local.sqldelight.conversationIdAdapter
import com.sunday.data.source.local.sqldelight.localIdAdapter
import com.sunday.data.source.local.sqldelight.participantIdAdapter
import com.sunday.data.source.local.sqldelight.senderIdAdapter
import com.sunday.data.source.local.sqldelight.serverIdAdapter
import com.sunday.data.source.local.sqldelight.statusAdapter

internal val messageEntityAdapter = MessageEntity.Adapter(
    localIdAdapter = localIdAdapter,
    serverIdAdapter = serverIdAdapter,
    senderIdAdapter = senderIdAdapter,
    conversationIdAdapter = conversationIdAdapter,
    statusAdapter = statusAdapter,
)

internal val conversationEntityAdapter = ConversationEntity.Adapter(
    idAdapter = conversationIdAdapter,
    peerIdAdapter = participantIdAdapter
)

internal val participantEntityAdapter = ParticipantEntity.Adapter(
    idAdapter = participantIdAdapter
)