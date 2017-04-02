package flights

import java.time.LocalDate

import scala.collection.immutable.ListMap
import scala.collection.mutable.{Buffer, ListBuffer}


/**
  * Created by kkolesnikov on 3/30/2017.
  */
class FlightLogs {


  /** flightLog holds Buffer of flights
  *  flights are duplicated: both arrival and departure events are stored
  */
  private var flightLog: Buffer[Flight] = ListBuffer[Flight]()

  private def logArrival(from: String, to: String, date: LocalDate) = {
      flightLog += new Flight(to, from, date, Direction.Arrival)
  }

  private def logDeparture(from: String, to: String, date: LocalDate) = {
      flightLog += new Flight(from, to, date, Direction.Departure)
  }

  /**
    * logs flight event. event is logged twice, as Arrival and Departure
    *
    * @param from origin airport
    * @param to   destination airprot
    * @param date flight date
    */
  def logFlight(from: String, to: String, date: LocalDate):Unit = {
      logArrival(from, to, date)
      logDeparture(from, to, date)
  }

  def length: Int = {
    flightLog.length
  }

    /**
    * delegates getStatisticsByAirport to StatisticsHelper
    * does not sort result
    */
  def getStatisticsByAirport(statistic: (Seq[Flight]) => (Int), filterZeros: Boolean): Map[String, Int] = {
     flightLog.getStatisticsByAirport(statistic, filterZeros)
  }

  /**
    * delegates getStatisticsByPeriod to StatisticsHelper
    * and sorts result by period ascending
    */
  def getStatisticsByPeriod(period: (LocalDate) => Int, statistic: (Seq[Flight]) => (Int)): Map[Int, Map[String, Int]] = {
      ListMap(flightLog.getStatisticsByPeriod(period, statistic).toSeq.sortBy(_._1):_*)
  }

  /**
    * extends Seq[Flight] with statistics methods
    * @param flights ArrayBuffer[Flight] to wrap
    */
  implicit private class StatisticsHelper(flights: Seq[Flight]) {

    /** returns statistics grouped by airport
      *   @param statistic: function, Seq[Flight]=>Int, describes how to calculate statistic
      *   @param filterZeros: specifies whether to filter out zero values from result
      *   @return Map of (airport, calculated statistics)
      */
    def getStatisticsByAirport(statistic: (Seq[Flight]) => (Int), filterZeros: Boolean): Map[String, Int] = {
      val noRaces: Map[String,Int] = flightLog.groupBy(_.getOriginAirport).mapValues(_ => 0)
      val results: Map[String, Int] = flights.groupBy(_.getOriginAirport)
          .mapValues(flights => statistic(flights))
      val includingNoRaces = results ++ (noRaces -- results.keySet)
      if(filterZeros) includingNoRaces.filterNot(_._2 == 0) else includingNoRaces
    }


    /** returns statistics grouped by airport
      *   @param period: function, Seq[Flight]=>Int, that specifies period calculation
      *   @param statistic: function, that describes how to calculate statistic
      *   @return Map of period (like year or week) -> Map(airport, statistic)
      */
    def getStatisticsByPeriod(period: (LocalDate) => Int, statistic: (Seq[Flight]) => (Int)): Map[Int, Map[String, Int]] = {
      flights.groupBy(flight => period(flight.getFlightDate))
        .mapValues(_.getStatisticsByAirport(statistic, filterZeros = false))
    }
  }
}
