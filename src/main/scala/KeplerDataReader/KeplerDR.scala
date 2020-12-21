package KeplerDataReader

import java.io.{BufferedWriter, File, FileWriter}

import org.mongodb.scala.MongoClient

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/** KeplerDataReader
 *
 *
 */
object KeplerDR extends App {
  val terminal = new Terminal
  terminal.welcomeMessage()

  val bufferedSource = io.Source.fromFile("cumulative_kepler2020.csv")
  val csv = CSVHandler()
  val planets = csv.getAllPlanets(bufferedSource)
  bufferedSource.close
  terminal.logger("Done.")

  if (args.length == 0) {
    terminal.logger("No arguments found. Run with \"-help\" to see usage.")
    System.exit(0)
  }

  val colNumbers = ArrayBuffer[Int]()
  val columns = args.filter(_.startsWith("--"))
  columns.foreach {
    case "--yr" => colNumbers += 6
    case "--op" => colNumbers += 11
    case "--r" => colNumbers += 13
    case "--m" => colNumbers += 15
    case "--t" => colNumbers += 20
    case "--d" => colNumbers += 35
    case "--sm" => colNumbers += 26
    case "--sr" => colNumbers += 25
    case col => println(s"$col is not recognized.\n use -help to see usage.")
  }
  val table = csv.buildNewCSVTable(planets, colNumbers.toArray)
  csv.writeCSVFile(table)

  val planetsDAO = new PlanetDAO(MongoClient())
  planetsDAO.createNewCollection(table)

  sleep(5000)

  def sleep(time: Long): Unit = Thread.sleep(time)
}
