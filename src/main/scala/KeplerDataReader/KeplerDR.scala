package KeplerDataReader

import scala.Console.{RESET => rt, MAGENTA => mg, CYAN => cy, YELLOW => yl}
import org.mongodb.scala.MongoClient
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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

  if (args.length == 0) {
    ui.logger(s"No arguments found. Run with '$rt$mg-help$rt' to see program usage.")
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
      case col => ui.logger(s"$col is not recognized. use '$rt$mg-h$rt' to see program usage.")
    }
  }
  val table = csv.buildNewCSVTable(planets, colNumbers.toArray)

  println("Generating CSV file... ")
  val csvFuture: Future[Unit] = Future {
    csv.writeCSVFile(table)
  }
  csvFuture.onComplete(_ => ui.logger(s"$rt${cy}CSV file ready$rt. "))

  println("Building new table to collection... ")
  val dbFuture: Future[Unit] = Future {
    db.createNewCollection(table)
  }
  dbFuture.onComplete(_=> ui.logger(s"\n$rt${yl}Dataset complete and ready to go$rt."))
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

    ui.logger(s"$rt${mg}Printing all planets found with$rt $constraintField $constraintType $constraintValue...")
    val printFuture: Future[Unit] = Future {
      db.printFilteredResults(constraintField, constraintType, constraintValue)
    }
    wrapUp(printFuture)
  } else {
    ui.logger(s"$rt${mg}Printing all planets$rt...")
    val printFuture: Future[Unit] = Future {
      db.printAll()
    }
    wrapUp(printFuture)
  }

  def wrapUp(future: Future[Unit]): Unit = {
    future.onComplete(_ => {
      ui.logger("Closing database connection... ", noNewLine = true)
      db.closeConnection()
      ui.logger(s"$rt${yl}done$rt.")
    })
  }

  sleep(5000)
  def sleep(time: Long): Unit = Thread.sleep(time)
}
