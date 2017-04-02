import java.io.{BufferedReader, File}

import flights.FlightLogs
import helpers.{Analytics, CSVToFlightConverter, DateUtils, GzipHelper}
import helpers.PrettyPrinter._

/**
  * Created by kkolesnikov on 3/30/2017.
  */

object Run extends App{

  override def main(args: Array[String]): Unit = {

    val gzipLogFile = ".\\src\\main\\resources\\planes_log.csv.gz"
    val arrivalsFile = ".\\src\\main\\resources\\arrivalsByAirport.txt.gz"
    val differenceFile = ".\\src\\main\\resources\\difference.txt.gz"
    val arrivalsByPeriodFile = ".\\src\\main\\resources\\arrivalsByPeriod.txt.gz"

    //read gzipped csv to flightLog
    val reader: BufferedReader = GzipHelper.getBufferedReaderFromGzip(new java.io.File(gzipLogFile))
    val flightLog: FlightLogs = CSVToFlightConverter.convertToFlightLog(reader)

    //build arrivals statistics
    val arrivalStat = flightLog.getStatisticsByAirport(Analytics.getArrivalsDeparturesDiff, filterZeros = false)
    GzipHelper.gzipString(arrivalStat.prettify.toString, new File(arrivalsFile))

    //build arrivals - departures statistics
    val differenceStat = flightLog.getStatisticsByAirport(Analytics.getArrivalsDeparturesDiff, filterZeros = true)
    GzipHelper.gzipString(differenceStat.prettify.toString, new File(differenceFile))

    //build arrivals by period statistics
    val periodStat = flightLog.getStatisticsByPeriod(DateUtils.getWeekOfYearStartingJanFirst, Analytics.getArrivalsCount)
    GzipHelper.gzipString(periodStat.prettify.toString, new File(arrivalsByPeriodFile))
  }
}


