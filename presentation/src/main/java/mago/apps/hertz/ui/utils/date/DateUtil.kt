package mago.apps.hertz.ui.utils.date

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getCurrentDate(): String {
        val dateNow: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault())
        return format.format(dateNow) + (getCurrentWeek()?.let { " (${it})" } ?: run { "" })
    }

    private fun getCurrentWeek(): String? {
        val currentDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        return try {
            val yoilList = listOf("일", "월", "화", "수", "목", "금", "토")
            yoilList[calendar[Calendar.DAY_OF_WEEK] - 1]
        } catch (e: Exception) {
            null
        }
    }
}