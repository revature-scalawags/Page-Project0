package KeplerDataReader

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
  ui.logger(" Done.")

  if (args.length == 0) {
    ui.logger("No arguments found. Run with \"-help\" to see program usage.")
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
      case col => println(s"$col is not recognized. use '-h' to see program usage.")
    }
  }
  val table = csv.buildNewCSVTable(planets, colNumbers.toArray)

  print("Generating CSV file... ")
  val csvFuture: Future[Unit] = Future {
    csv.writeCSVFile(table)
  }
  csvFuture.onComplete(_ => println("done. "))

  println("Adding new table to database... ")
  val dbFuture: Future[Unit] = Future {
    db.createNewCollection(table)
  }
  dbFuture.onComplete(_=> println("Database complete and ready."))

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

    println(s"Printing all planets found with $constraintField $constraintType $constraintValue...")
    val printFuture: Future[Unit] = Future {
      db.printFilteredResults(constraintField, constraintType, constraintValue)
    }
    printFuture.onComplete(_ => {
      print("Closing database connection... ")
      db.closeConnection()
      println("done.")
    })
  } else {
    println("Printing all planets...")
    val printFuture: Future[Unit] = Future {
      db.printAll
    }
    printFuture.onComplete(_ => {
      print("Closing database connection... ")
      db.closeConnection()
      println("done.")
    })
  }

  sleep(7000)

  def sleep(time: Long): Unit = Thread.sleep(time)
}
