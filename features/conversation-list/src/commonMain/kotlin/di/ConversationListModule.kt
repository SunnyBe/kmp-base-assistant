package di

import com.sunday.conversation_list.ConversationListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val conversationListModule = module {
    factoryOf(::ConversationListViewModel)
    viewModel { ConversationListViewModel(get(), get()) }
}