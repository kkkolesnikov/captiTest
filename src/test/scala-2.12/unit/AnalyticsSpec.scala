package unit

import java.time.LocalDate

import flights.{Direction, Flight}
import helpers.Analytics
import org.specs2.mutable.{Before, Specification}

/**
  * Created by kkolesnikov on 4/1/2017.
  */
class AnalyticsSpec extends Specification{
  "Analytic functions works well on Seq" should {

      class ArrivalsAndDepartures extends Before {
        protected var flights: Seq[Flight]= Seq[Flight]()

        override def before: Any = {
          val date: LocalDate = LocalDate.now
          flights = Seq[Flight](
            new Flight("KBP", "CDG", date, Direction.Arrival),
            new Flight("ROW", "CIT", date, Direction.Departure),
            new Flight("NYC", "MSK", date, Direction.Arrival)
          )
        }
      }


      "calculate arrivals correctly if there are both arrivals and departures" in new ArrivalsAndDepartures {
          Analytics.getArrivalsCount(flights) must_== 2
      }

      "calculate difference correctly if there are both arrivals and departures" in new ArrivalsAndDepartures {
        Analytics.getArrivalsDeparturesDiff(flights) must_== 1
      }

      class NoArrivals extends Before {
        protected var flights: Seq[Flight]= Seq[Flight]()

        override def before: Any = {
          val date: LocalDate = LocalDate.now
          flights = Seq[Flight](
            new Flight("KBP", "CDG", date, Direction.Departure),
            new Flight("ROW", "CIT", date, Direction.Departure),
            new Flight("NYC", "MSK", date, Direction.Departure)
          )
        }
      }


      "calculate arrivals correctly if there are no arrivals" in new NoArrivals {
        Analytics.getArrivalsCount(flights) must_== 0
      }

      "calculate difference correctly if there are no arrivals" in new NoArrivals {
        Analytics.getArrivalsDeparturesDiff(flights) must_== -3
      }

      class NoDepartures extends Before {
        protected var flights: Seq[Flight]= Seq[Flight]()

        override def before: Any = {
          val date: LocalDate = LocalDate.now
          flights = Seq[Flight](
            new Flight("KBP", "CDG", date, Direction.Arrival),
            new Flight("ROW", "CIT", date, Direction.Arrival),
            new Flight("NYC", "MSK", date, Direction.Arrival)
          )
        }
      }

      "calcualate arrivals correctly if there are no departures" in new NoDepartures {
        Analytics.getArrivalsCount(flights) must_== 3
      }

      "calculate difference correctly if there are no departures" in new NoDepartures {
        Analytics.getArrivalsDeparturesDiff(flights) must_== 3
      }


      "calculate arrivals correcly for empty seq"   >> {
          Analytics.getArrivalsCount(Seq[Flight]()) must_== 0
      }

      "calculate difference correctly for empty seq"   >> {
        Analytics.getArrivalsDeparturesDiff(Seq[Flight]()) must_== 0
      }
  }

}
