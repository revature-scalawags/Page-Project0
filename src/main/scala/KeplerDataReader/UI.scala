package KeplerDataReader

import java.io.BufferedWriter

import scala.Console.{BLUE => bu, CYAN => cy, GREEN => gr, MAGENTA => mg, RED => rd, RESET => rt, YELLOW => yl}
import scala.io.StdIn.{readInt, readLine}
import scala.util.control.Breaks._
case class UI() {

  def welcomeMessage(): Unit = {
      println(
        s"""
        ##########################################################################################################
        #-----------------------------------$rt${cy}Welcome the Kepler Data Reader$rt---------------------------------------#
        #The program that imports the NASA provided data file for the exoplanets found with the Kepler project.  #
        #                                                                                                        #
        #The user provides command line arguments to select whichever columns from the data source they'd like   #
        #  to have in their generated .csv file and loaded to a database.                                        #
        #                                                                                                        #
        #* Note: $rt${bu}All generated tables will at least include columns for the planet names and its host star name$rt. #
        #                                                                                                        #
        #Example (sbt):                                                                                          #
        #  $rt${mg}use 'run --help' to view program usage$rt.                                                               #
        ##########################################################################################################
    """.stripMargin
   )
  }

  def usage(): Unit = {
    println(
      """
        |Imports the NASA provided data file for the exoplanets found with the Kepler project.
        |
        |The user provides command line arguments to select whichever columns from the data source they'd like
        |  to have in their generated .csv file and loaded to a database.
        |
        |* Note: All generated tables will at least include columns for the planet names and its host star name.
        |
        |Example (sbt):
        |  run -t -m -d -r
        |_______________________________________________________________________________________________________
        |
        |Options:
        |  -help, -h                              Render Aid
        |  Columns:
        |   -yr                                   Discovery Year
        |   -op                                   Orbital Period
        |   -m                                    Planetary Mass (earth mass)
        |   -r                                    Radius (earth radius)
        |   -t                                    Equilibrium Temperature (Kelvin)
        |   -d                                    Distance (parsecs) i.e. 1pc = 3.26 light years
        |   -sm                                   Stellar Mass (solar mass)
        |   -sr                                   Stellar Radius (solar radius)
        |
        |   --all, --a                            Generate a table with all possible columns.
        |
        |________________________________________________________________________________________________________
              """.stripMargin
    )
    System.exit(0)
  }

  def logger(message: String, bw: BufferedWriter): Unit = {
    val now = getTimeStamp
    try {
      bw.newLine()
      bw.write(now + ": " + message)
      bw.newLine()
    } catch {case _: Throwable => println(s"$rt${rd}Logging Failure$rt")}
    println(message)
  }

  def getTimeStamp: String = java.time.LocalDateTime.now().toString

  def promptUsrConstraintField(header: String): (String, Boolean) = {
    println("\nYou have created a database with the following columns:")
    val cols =  header.split(",")

    println(s"$rt${gr}1$rt.)   None")
    for ((col, i) <- cols.zipWithIndex) {
      println(s"$rt$gr${i+2}$rt.)   $col")
    }
    print(s"\nEnter the number for the criteria you'd like to filter by.\nSelect $rt$cy'None'$rt to fetch all data: \n")

    var input = -1
    try {
      breakable(
    while(true) {
      input = readInt()
      if (input >= 1 && input <= cols.length + 1) {
        break
      }
      print(s"Please select a valid choice from $rt${rd}1$rt to $rt$cy${cols.length + 1}$rt: ")
    }
   )
    } catch {
      case _: NumberFormatException | _: ArrayIndexOutOfBoundsException =>
        println(s"\n$rt${rd}You MUST select an integer value between 1 and ${cols.length + 1}$rt.")
        return (cols(input - 2), false)
    }
    try {
      (cols(input - 2), true)
    } catch { case _: ArrayIndexOutOfBoundsException => ("None", true) }
  }

  def promptUsrConstraintType(col: String): (String, Boolean) = {
    println(s"\nYou have selected the $rt$cy$col$rt column.\n")
    println(s"$rt${gr}1$rt.) All planets with some value EQUAL TO.")
    println(s"$rt${gr}2$rt.) All planets with some value GREATER THAN.")
    println(s"$rt${gr}3$rt.) All planets with some value LESS THAN.\n")

    print("\nEnter a number for the type of filter you would like to use: ")

    var input = -1
    try {
      breakable(
        while (true) {
          input = readInt()
          if (input >= 1 && input <= 3) {
            break
          }
          print(s"Please select a valid choice from $rt${rd}1$rt to $rt${rd}3$rt: ")
        }
      )
    } catch {
      case _: NumberFormatException | _: ArrayIndexOutOfBoundsException =>
        println(s"$rt$rd\nYou MUST select an integer value between 1 and 3$rt.")
        return (getQueryType(input), false)
    }
    (getQueryType(input), true)
  }

  def promptUsrConstraintValue(col: String, filter: String): (Any, Boolean) = {
    println(s"\nYou are searching for planets with $rt$cy$col $filter$rt some value.\n")

    print("Please enter that value: ")

    var input = if (col == "planet" || col == "host_star") "" else -1
    try {
      if (col == "planet" || col == "host_star") {
        breakable(
          while (true) {
            input = readLine()
            if (input.toString.length > 5) {
              break
            }
            print(s"$rt$yl$input$rt doesn't seem right. You can do a better search than that: ")
          }
        )
      } else {
        input = readInt()
      }
    } catch {
      case _: NumberFormatException | _: ArrayIndexOutOfBoundsException =>
        println(s"$rt$rd\nYou MUST select an integer value between$rt.")
        return (input, false)
    }
    (input, true)
  }

  def getQueryType(choice: Int): String = choice match {
    case 1 => "EQUAL TO"
    case 2 => "GREATER THAN"
      case 3 => "LESS THAN"
    case _ => "Goes with the exception"
  }
}
