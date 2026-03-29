package com.sunday.data.fakes

import com.sunday.domain.model.ParticipantId
import com.sunday.domain.service.account.AccountService

class FakeAccountService : AccountService {
    override suspend fun getCurrentUserId(): ParticipantId? {
        TODO("Not yet implemented")
    }
}