package unit

import java.time.LocalDate

import flights.FlightLogs
import org.specs2.mutable._

/**
  * Created by kkolesnikov on 4/1/2017.
  */

class FlightLogSpec extends Specification {
  "Flight log" should {

      "store flight twice both as arrival and departure" >> {
        val flightLogs = new FlightLogs()
        flightLogs.logFlight("KBP", "ROW", LocalDate.now)
        flightLogs.length must_== 2
      }

      "regard several flights with same origin and dest as different flights" >> {
          val flightLogs = new FlightLogs()
          val flDate = LocalDate.now
          flightLogs.logFlight("KBP", "CDG", flDate)
          flightLogs.logFlight("KBP", "CDG", flDate)
          flightLogs.length must_== 4
      }
  }
}
