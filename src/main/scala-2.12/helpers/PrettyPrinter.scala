package helpers

import scala.collection.immutable.Iterable

/**
  * Created by kkolesnikov on 3/31/2017.
  */
object PrettyPrinter
{
    implicit class PrettyPrintMap[K, V](val map: Map[K, V]) {

        private var counter = 0
        def prettify: PrettyPrintMap[K, V] = this

        override def toString: String = {
          val valuesString = toStringLines.mkString("\n")
          valuesString
        }

        def toStringLines: Iterable[String] = {
          map
            .flatMap{ case (k, v) => keyValueToString(k, v)}
            .map("\t" + _)
        }

        def keyValueToString(key: K, value: V): Iterable[String] = {
          counter += 1
          value match {
            case v: Map[_, _] => Iterable(counter.toString + ". " + key + "") ++ v.prettify.toStringLines
            case x => Iterable(counter.toString + ". " + key + ": " + x.toString)
          }
        }
    }
}