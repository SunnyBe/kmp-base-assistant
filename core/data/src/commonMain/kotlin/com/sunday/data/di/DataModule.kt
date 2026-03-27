package com.sunday.data.di

import com.sunday.data.local.ChatDatabase
import com.sunday.data.local.ConversationEntity
import com.sunday.data.local.MessageEntity
import com.sunday.data.local.ParticipantEntity
import com.sunday.data.repository.ChatRepositoryImpl
import com.sunday.data.service.ChatAccountService
import com.sunday.data.service.StaticEncryptionService
import com.sunday.data.source.local.sqldelight.DatabaseDriverFactory
import com.sunday.data.source.local.sqldelight.conversationIdAdapter
import com.sunday.data.source.local.sqldelight.localIdAdapter
import com.sunday.data.source.local.sqldelight.participantIdAdapter
import com.sunday.data.source.local.sqldelight.senderIdAdapter
import com.sunday.data.source.local.sqldelight.serverIdAdapter
import com.sunday.data.source.local.sqldelight.statusAdapter
import com.sunday.domain.repository.ChatRepository
import com.sunday.domain.service.account.AccountService
import com.sunday.domain.service.encryption.EncryptionService
import com.sunday.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

internal val domainBusinessModule = module {
    factory { SendMessageUseCase(get(), get()) }
}

internal val adapterModule = module {
    single { statusAdapter }
    single { localIdAdapter }
    single { serverIdAdapter }
    single { senderIdAdapter }
    single { conversationIdAdapter }
    single { participantIdAdapter }
}

internal val dataImplementationModule = module {
    // Repository
    single<ChatRepository> {
        ChatRepositoryImpl(
            database = get(),
            network = get(),
            accountService = get(),
            connectivityService = get(),
            coroutineScope = get(),
            ioDispatcher = get(named("io"))
        )
    }

    // Services
    single<EncryptionService> { StaticEncryptionService() }
    single<AccountService> { ChatAccountService() }

    // Database
    single {
        val driver = get<DatabaseDriverFactory>().createDriver()
        ChatDatabase(
            driver = driver,
            MessageEntityAdapter = MessageEntity.Adapter(
                statusAdapter = get(),
                localIdAdapter = get(),
                serverIdAdapter = get(),
                senderIdAdapter = get(),
                conversationIdAdapter = get()
            ),
            ConversationEntityAdapter = ConversationEntity.Adapter(
                idAdapter = get(),
                peerIdAdapter = get()
            ),
            ParticipantEntityAdapter = ParticipantEntity.Adapter(idAdapter = get())
        )
    }
}

internal val coroutinesModule = module {
    single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
    single<CoroutineDispatcher>(named("main")) { Dispatchers.Main }
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named("io"))) }
}

val coreDataModule = module {
    includes(domainBusinessModule, dataImplementationModule, coroutinesModule, adapterModule)
}