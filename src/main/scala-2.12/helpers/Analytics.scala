package helpers

import flights.Flight
import scala.collection.mutable.ArrayBuffer

/**
  * Created by kkolesnikov on 3/31/2017.
  */
object Analytics {

  /**
    * counts arrivals for given flights
    * @param flights Array of flights to get statistics for
    * @return count of arrivals
    */
    def getArrivalsCount(flights: Seq[Flight]):Int = {
      flights.count(_.isArrival)
    }

  /**
    * calculates difference between arrivals and departures for given flights
    * @param flights Array of flights to get statistics for
    * @return difference between arrivals and departures
    */
    def getArrivalsDeparturesDiff(flights: (Seq[Flight])):Int = {
      val arrivals = flights.partition(_.isArrival)
      arrivals._1.length - arrivals._2.length
    }
}
