package com.sunday.baseassist.core.data

import com.sunday.baseassist.core.domain.service.formatter.DateFormatter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class JvmDateFormatter: DateFormatter {
    @OptIn(ExperimentalTime::class)
    override fun format(instant: Instant): String {
        TODO("Not yet implemented")
    }
}