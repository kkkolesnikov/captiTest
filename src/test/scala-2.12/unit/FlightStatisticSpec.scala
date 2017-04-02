package unit

import java.time.LocalDate
import flights.{Flight, FlightLogs}
import org.specs2.mutable.{Before, Specification}

/**
  * Created by kkolesnikov on 4/1/2017.
  */
class FlightStatisticSpec extends Specification{

    protected def testCountDepartures(flights: Seq[Flight]): Int = {
      flights.count(!_.isArrival)
    }

    trait TestContext {
       protected var flightLog: FlightLogs = new FlightLogs
    }

    class AirportTestContext extends Before with TestContext{

      override def before: Any = {
        //given
        flightLog = new FlightLogs()
        flightLog.logFlight("KBP","CDG", LocalDate.now)
        flightLog.logFlight("KBP","CDG", LocalDate.now)
        flightLog.logFlight("CDG","LCY", LocalDate.now)
        flightLog.logFlight("CDG","LCY", LocalDate.now)

      }
    }

    "Analytics by airport" should {
          "calculate statistic correctly" in new AirportTestContext{
              //when
              val stat = flightLog.getStatisticsByAirport(testCountDepartures, filterZeros = false)

              //then
              stat.size must_== 3
              stat must contain(("CDG", 2))
              stat must contain(("LCY", 0))
              stat must contain(("KBP", 2))
          }

          "filter out zero values if filtering is specified" in new AirportTestContext {
              //when
              val stat = flightLog.getStatisticsByAirport(testCountDepartures, filterZeros = true)

              //then
              stat.size must_== 2
              stat must contain(("CDG", 2))
              stat must contain(("KBP", 2))
          }

          "calculates by-directional flights correctly" >> {
            val flightLog = new FlightLogs
            flightLog.logFlight("KBP","CDG", LocalDate.now)
            flightLog.logFlight("CDG","KBP", LocalDate.now)

            val stat = flightLog.getStatisticsByAirport(testCountDepartures, filterZeros = true)

            //then
            stat.size must_== 2
            stat must contain(("CDG", 1))
            stat must contain(("KBP", 1))
          }

          "does not fail when log is empty" >> {
            //given
            val flightLog = new FlightLogs

            //when
            val stat = flightLog.getStatisticsByAirport(testCountDepartures, filterZeros = false)
            //then
            stat.size must_== 0
          }
    }

    class PeriodTestContext extends Before with TestContext{

      protected def dayOfMonth(date: LocalDate): Int = {
        date.getDayOfMonth
      }

      override def before: Any = {
        //given
        flightLog = new FlightLogs
      }
    }

    "Analytics by period" should {
      "statistics for a given group period" in new PeriodTestContext {
        val mar1 = LocalDate.of(2017, 3, 1)
        val feb1 = LocalDate.of(2017, 2, 1)
        val jan2 = LocalDate.of(2017, 1, 2)

        //given
        flightLog.logFlight("KBP", "CDG", mar1)
        flightLog.logFlight("KBP", "CDG", feb1)
        flightLog.logFlight("KBP", "CDG", jan2)

        //when
        val stat = flightLog.getStatisticsByPeriod(dayOfMonth, testCountDepartures)

        //then
        stat.size must_== 2
        stat(1) must contain(("KBP", 2))
        stat(1) must contain(("CDG", 0))

        stat(2) must contain(("KBP", 1))
        stat(2) must contain(("CDG", 0))
      }

      "shows statistics for airports if there are flights within group period" in new PeriodTestContext {
          val mar1 = LocalDate.of(2017, 3, 1)
          val mar2 = LocalDate.of(2017, 3, 2)

          //given
          flightLog.logFlight("KBP", "CDG", mar1)
          flightLog.logFlight("ROW", "PLN", mar2)

          //when
          val stat = flightLog.getStatisticsByPeriod(dayOfMonth, testCountDepartures)

          //then
          stat.size must_== 2
          stat(1).size must_== 2
          stat(1) must contain(("KBP", 1))
          stat(1) must contain(("CDG", 0))

          stat(2).size must_== 2
          stat(2) must contain(("ROW", 1))
          stat(2) must contain(("PLN", 0))
      }

      "does not fail when log is empty" in new PeriodTestContext {
          //when
          val stat = flightLog.getStatisticsByPeriod(dayOfMonth, testCountDepartures)

          //then
          stat.size must_== 0
      }


    }

}
