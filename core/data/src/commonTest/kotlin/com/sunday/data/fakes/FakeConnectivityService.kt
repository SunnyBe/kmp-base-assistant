package com.sunday.data.fakes

import com.sunday.domain.service.connectivity.ConnectivityService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeConnectivityService : ConnectivityService {
    override fun isOnline(): Flow<Boolean> {
        return flowOf(true)
    }
}