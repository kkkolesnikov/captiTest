package helpers

import java.io.BufferedReader
import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.util.logging.Logger

import flights.FlightLogs

/**
  * Created by kkolesnikov on 3/31/2017.
  */

object CSVToFlightConverter{

  private val logger = Logger.getLogger("CSV Log Reader")

  private var columnsMapping: Array[String] = new Array[String](10)
  private val separator = ",|;|\\s+"

  private val origin: String = "ORIGIN"
  private val destination: String = "DEST"
  private val date: String = "FL_DATE"

  private def split(line: String):Array[String] = {
    val regex = "^\"|\"$".r
    line
      .split(separator)
      .map {_.trim}
      .map(l => regex.replaceAllIn(l, "")) //remove \" at the beginning and ending
      .map {_.trim}
  }

  private def setMapFromHeadLine(line: String): Unit = {
    columnsMapping = split(line)

  }

  private def getMapFromLine(line: String): Map[String, String] = {
    var mapped: Map[String, String] = Map[String, String]()
    val cols = split(line)
    for(i <- cols.indices){
      mapped += (columnsMapping(i) -> cols(i))
    }
    mapped
  }

  def convertToFlightLog(reader: BufferedReader): FlightLogs = {
      val flightLogs: FlightLogs = new FlightLogs
      var line: String = null
      //read head line and build column names map
      if({line = reader.readLine(); line != null}) setMapFromHeadLine(line)

      //read all lines
      while ({line = reader.readLine(); line != null}) {
          //build column name -> value map from line
          val flightLogMap = getMapFromLine(line)
          try {
              val localDate: LocalDate = LocalDate.parse(flightLogMap(date), DateTimeFormatter.ISO_LOCAL_DATE)
              //add flight to flightLog
              flightLogs.logFlight(flightLogMap(origin), flightLogMap(destination), localDate)
          } catch {
              case d: DateTimeParseException => logger.warning("Skipping line as cannot parse date at " + line)
              case e: NoSuchElementException =>
                logger.severe("Malformed CSV file. Must contain head line with columns: ORIGIN, DEST, FL_DATE")
                throw e
          }
        }
      flightLogs
  }
}
