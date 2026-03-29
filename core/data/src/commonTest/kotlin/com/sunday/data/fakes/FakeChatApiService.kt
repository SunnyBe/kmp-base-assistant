package com.sunday.data.fakes

import com.sunday.data.source.network.ChatApiService
import com.sunday.data.source.network.MessageRequestDto
import com.sunday.data.source.network.MessageResponseDto

class FakeChatApiService : ChatApiService {
    override suspend fun pushMessage(messageDto: MessageRequestDto): MessageResponseDto {
        TODO("Not yet implemented")
    }
}