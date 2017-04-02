package unit

import java.time.LocalDate

import helpers.DateUtils
import org.specs2.mutable.Specification

/**
  * Created by kkolesnikov on 4/1/2017.
  */
class DateUtilsSpec extends Specification{
  "Weeks calculation" should {
      "calculation when week starts on Jan,1 is correct - Jan,1" >> {
          val jan1 = LocalDate.of(2014,1,1)
          DateUtils.getWeekOfYearStartingJanFirst(jan1) must_== 1
      }

      "calculation when week starts on Jan,1 is correct - Jan,7" >> {
        val jan7 = LocalDate.of(2014,1,7)
        DateUtils.getWeekOfYearStartingJanFirst(jan7) must_== 1
      }

      "calculation when week starts on Jan,1 is correct, Jan,8" >> {
        val jan8 = LocalDate.of(2014,1,8)
        DateUtils.getWeekOfYearStartingJanFirst(jan8) must_== 2
      }

      "calculation when week starts on Jan,1 is correct - Dec,31" >> {
        val dec31 = LocalDate.of(2014,12,31)
        DateUtils.getWeekOfYearStartingJanFirst(dec31) must_== 53
      }

      "calculation of week number in ISO8601" >> {
        val jan1 = LocalDate.of(2014,1,1)
        val jan5 = LocalDate.of(2014,1,5)
        val dec27 = LocalDate.of(2014,12,27)
        val dec31 = LocalDate.of(2014,12,31)

        DateUtils.getWeekOfYearISO8601(jan1) must_== 1
        DateUtils.getWeekOfYearISO8601(jan5) must_== 2
        DateUtils.getWeekOfYearISO8601(dec27) must_== 52
        DateUtils.getWeekOfYearISO8601(dec31) must_== 1
      }
  }
}
