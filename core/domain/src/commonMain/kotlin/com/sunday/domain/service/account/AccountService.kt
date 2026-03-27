package com.sunday.domain.service.account

import com.sunday.domain.model.ParticipantId

interface AccountService {

    // Authenticate and provide the user's Id
    suspend fun getCurrentUserId(): ParticipantId?
}