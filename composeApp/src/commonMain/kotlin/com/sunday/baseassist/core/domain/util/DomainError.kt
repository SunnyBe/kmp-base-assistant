package com.sunday.baseassist.core.domain.util

sealed interface DomainError {
    data object NetworkTimeout : DomainError
    data object Unauthorized : DomainError
    data class Unknown(val cause: String) : DomainError
}