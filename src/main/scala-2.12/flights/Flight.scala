package flights

import java.time.LocalDate

import flights.Direction.Direction

/**
  * Created by kkolesnikov on 3/31/2017.
  */
class Flight (from: String, to: String, date: LocalDate, direction: Direction) {

    private val originAirport: String = from
    private val correspondentAirport: String = to
    private val flightDate: LocalDate = date
    private val flightDirection: Direction = direction

    def getFlightDate: LocalDate = flightDate
    def getOriginAirport: String = originAirport
    def isArrival: Boolean = flightDirection == Direction.Arrival
}
