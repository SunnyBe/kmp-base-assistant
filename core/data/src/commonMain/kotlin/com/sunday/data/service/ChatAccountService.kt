package com.sunday.data.service

import com.sunday.domain.model.ParticipantId
import com.sunday.domain.service.account.AccountService

class ChatAccountService : AccountService {
    override suspend fun getCurrentUserId(): ParticipantId? {
        try {
            // Check user session
            return ParticipantId("hard-coded-user")
        } catch (cause: Exception) {
            // Log cause
            cause.printStackTrace()
            return null
        }
    }
}