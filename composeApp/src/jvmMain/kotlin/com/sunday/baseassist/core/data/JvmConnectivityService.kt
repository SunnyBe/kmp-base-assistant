package com.sunday.baseassist.core.data

import com.sunday.baseassist.core.domain.service.connectivity.ConnectivityService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class JvmConnectivityService: ConnectivityService {
    override fun isOnline(): Flow<Boolean>  = callbackFlow{
        trySend(true)
    }
}