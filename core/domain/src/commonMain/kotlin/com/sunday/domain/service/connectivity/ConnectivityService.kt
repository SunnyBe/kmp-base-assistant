package com.sunday.domain.service.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityService {
    fun isOnline(): Flow<Boolean>
}