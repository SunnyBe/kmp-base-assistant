package com.sunday.domain.service.formatter

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface DateFormatter {

    @OptIn(ExperimentalTime::class)
    fun format(instant: Instant): String
}