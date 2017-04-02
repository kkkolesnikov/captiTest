# captiTest

Application reads flights log from gzipped resource file named planes_log.csv.gz.
CSV file must have a head line with at least ORIGIN, DEST and FL_DATE columns

Gets statistics of:
- arrivals by airport
- difference between arrivals and departures by airport
- arrivals by period and airport
and saves gzipped files to resource folder

"sbt run" to run application; "sbt test" to run tests
