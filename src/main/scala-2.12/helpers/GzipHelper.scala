package helpers

import java.io._
import java.util.logging.Logger
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

/**
  * Created by kkolesnikov on 3/30/2017.
  */
object GzipHelper{
  private val logger = Logger.getLogger("GzipHelper")

  def getBufferedReaderFromGzip(file: java.io.File): BufferedReader = {
    try {
      io.Source.createBufferedSource(
        new GZIPInputStream(
          new FileInputStream(file)))
        .bufferedReader()
    } catch {
        case f: FileNotFoundException => throw new RuntimeException(f.getMessage)
        case i: IOException => throw new RuntimeException(i.getMessage)
    }

  }

  def gzipString(source: String, file: java.io.File): Unit = {
    val reader: ByteArrayInputStream = new ByteArrayInputStream(source.getBytes("UTF-8"))
    gzipInputStream(reader, file)
  }

  def gzipInputStream(source: InputStream, outputFile: java.io.File): Unit = {
    try{
        val buffer = new Array[Byte](1024)
        val gzout = new GZIPOutputStream(new FileOutputStream(outputFile))
        var len: Int = 0
        while({len = source.read(buffer); len > 0}){
          gzout.write(buffer, 0, len)
        }
        gzout.close()
        logger.info(String.format("statistics was successfully saved to %s", outputFile.getName))
    } catch {
        case fnf: FileNotFoundException => logger.severe(String.format("Cannot save to file %s. %s", outputFile.getName, fnf.getMessage))
        case io: IOException => logger.severe(io.toString)
    } finally {
        source.close()
    }

  }
}
