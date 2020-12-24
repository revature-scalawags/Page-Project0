package KeplerDataReader

import java.io.{BufferedWriter, File, FileWriter}
import scala.Console.{CYAN => cy, MAGENTA => mg, RESET => rt, YELLOW => yl}
import scala.concurrent.ExecutionContext.Implicits.global
import org.mongodb.scala.MongoClient
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import java.nio.file.{Paths, Files}

/** KeplerDataReader
 *
 *
 */
object KeplerDR extends App {
  val ui = new UI
  ui.welcomeMessage()
  val db = new PlanetDAO(MongoClient())

  val bufferedSource = io.Source.fromFile("cumulative_kepler2020.csv")
  val csv = CSVHandler()
  val planets = csv.getAllPlanets(bufferedSource)
  bufferedSource.close

  val file = new File("log.txt")
  val hasLogFile: Boolean = Files.exists(Paths.get("log.txt"))
  val bw = new BufferedWriter(new FileWriter(file, hasLogFile))

  if (args.length == 0) {
    ui.logger(s"No arguments found. Run with '$rt$mg-help$rt' to see program usage.", bw)
    System.exit(0)
  }
  val colNumbers = ArrayBuffer[Int]()

  if (args.contains("--all") || args.contains("--a")) {
    colNumbers += (6,11,13,15,20,35,26,25)
  } else {
    args.foreach {
      case "-yr" => colNumbers += 6
      case "-op" => colNumbers += 11
      case "-m" => colNumbers += 15
      case "-t" => colNumbers += 20
      case "-r" => colNumbers += 13
      case "-d" => colNumbers += 35
      case "-sm" => colNumbers += 26
      case "-sr" => colNumbers += 25
      case "-help" | "-h" => ui.usage()
      case col => ui.logger(s"$col is not recognized. use '$rt$mg-h$rt' to see program usage.", bw)
    }
  }
  val table = csv.buildNewCSVTable(planets, colNumbers.toArray)

  println("Generating CSV file... ")
  val csvFuture: Future[Unit] = Future {
    csv.writeCSVFile(table)
  }
  csvFuture.onComplete(_ => ui.logger(s"$rt${cy}CSV file ready$rt. ", bw))

  println("Adding new table to the database... ")
  val dbFuture: Future[Unit] = Future {
    db.createNewCollection(table)
  }
  dbFuture.onComplete(_=> {
    ui.logger(s"\n$rt${mg}Dataset complete.$rt.", bw)
    ui.logger(s"Processed $rt$yl${table.tail.length}$rt entries.", bw)

  })
  sleep(1000)

  var constraintField: String = _
  while ({
    val (res, isValid) = ui.promptUsrConstraintField(table.head)
    constraintField = res
    !isValid
  })()

  if (constraintField != "None") {
    var constraintType: String = null
    while ( {
      val (res, isValid) = ui.promptUsrConstraintType(constraintField)
      constraintType = res
      !isValid
    }) ()

    var constraintValue: Any = null
    while ( {
      val (res, isValid) = ui.promptUsrConstraintValue(constraintField, constraintType)
      constraintValue = res
      !isValid
    }) ()

    ui.logger(s"$rt${mg}Printing all planets found with$rt $constraintField $constraintType $constraintValue...", bw)
    val printFuture: Future[Unit] = Future {
      db.printFilteredResults(constraintField, constraintType, constraintValue)
    }
    wrapUp(printFuture)
  } else {
    ui.logger(s"$rt${mg}Printing all planets$rt...", bw)
    val printFuture: Future[Unit] = Future {
      db.printAll()
    }
    wrapUp(printFuture)
  }

  def wrapUp(future: Future[Unit]): Unit = {
    future.onComplete(_ => {
      ui.logger("Writing log..", bw)
      ui.logger("Closing database connection... ", bw)
      db.closeConnection()
      bw.close()
      println(s"$rt${yl}done$rt.")
    })
  }

  sleep(3000)
  def sleep(time: Long): Unit = Thread.sleep(time)
}
