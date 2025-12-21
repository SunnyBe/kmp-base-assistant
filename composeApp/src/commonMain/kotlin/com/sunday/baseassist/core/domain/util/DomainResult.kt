package com.sunday.baseassist.core.domain.util

sealed interface DomainResult<out T, out E> {
    data class Success<T>(val data: T) : DomainResult<T, Nothing>
    data class Failed<E>(val cause: E) : DomainResult<Nothing, E>
}