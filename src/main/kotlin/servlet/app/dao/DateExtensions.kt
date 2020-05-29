package servlet.app.dao

import java.sql.Date
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


object DateExtensions {
    fun Date.toLocalDateTime(zone: ZoneId): LocalDateTime {
        return Instant.ofEpochMilli(this.time)
            .atZone(zone)
            .toLocalDateTime()
    }
}