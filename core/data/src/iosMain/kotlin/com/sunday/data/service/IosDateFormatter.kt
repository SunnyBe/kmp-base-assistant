package com.sunday.data.service

import com.sunday.domain.service.formatter.DateFormatter
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.dateWithTimeIntervalSince1970
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class IosDateFormatter : DateFormatter {
    @OptIn(ExperimentalTime::class)
    override fun format(instant: Instant): String {
        val date = NSDate.dateWithTimeIntervalSince1970(instant.epochSeconds.toDouble())
        val formatter = NSDateFormatter().apply {
            dateStyle = NSDateFormatterShortStyle
            timeStyle = NSDateFormatterShortStyle
        }
        return formatter.stringFromDate(date)
    }
}