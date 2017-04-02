package helpers

import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

/**
  * Created by kkolesnikov on 3/31/2017.
  */
object DateUtils {

    def getYear(date: LocalDate): Int = date.getYear

    def getQuarter(date: LocalDate): Int = (date.getMonthValue - 1) / 4

    def getMonth(date: LocalDate): Int = date.getMonthValue

  /**
    * gets week of year according to ISO8601
    * @param date  date
    * @return week of year
    */
  def getWeekOfYearISO8601(date: LocalDate): Int = {
      val weekFields: WeekFields = WeekFields.of(Locale.ROOT)
      date.get(weekFields.weekOfWeekBasedYear())
    }

  /**
    * week number of year considering first week starts on Jan, 1
    * @param date  date
    * @return week number
    */
    def getWeekOfYearStartingJanFirst(date: LocalDate): Int = {
        val dayOfYear = date.getDayOfYear
      (dayOfYear - 1)/ 7 + 1
    }
}