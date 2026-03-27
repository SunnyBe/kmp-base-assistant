package com.sunday.domain.util

sealed interface DomainError {
    data object NetworkTimeout : DomainError
    data object Unauthorized : DomainError
    data object MessageUnavailable : DomainError
    data class Unknown(val cause: String) : DomainError
}