package com.sunday.data.service.formatter

import com.sunday.domain.service.formatter.DateFormatter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class AndroidDateFormatter: DateFormatter {
    @OptIn(ExperimentalTime::class)
    override fun format(instant: Instant): String {
        TODO("Not yet implemented")
    }
}