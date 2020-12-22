package KeplerDataReader

import scala.io.StdIn.readInt
import scala.util.control.Breaks._

case class UI() {

  def welcomeMessage(): Unit = {
    println("Welcome to Kepler Data Reader.\n")
    print("Loading exoplanet data file...")
  }

  def logger(message: String): Unit = {
    println(message)
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
        |  -help, -h                              render aid
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
              """.stripMargin)
    System.exit(0)
  }

  def promptUsrConstraintField(header: String): (String, Boolean) = {
    println("\nYou have created a database with the following columns:")
    val cols =  header.split(",")

    println("1.)   None")
    for ((col, i) <- cols.zipWithIndex) {
      println(s"${i+2}.)   $col")
    }
    print("\nEnter the number for the criteria you'd like to filter by.\nSelect \"None\" to fetch all data: ")

    var input = -1
    try {
      breakable(
      while(true) {
        input = readInt()
        if (input >= 1 && input <= cols.length + 1) {
          break
        }
       print(s"Please select a valid choice from 1 to ${cols.length + 1}: ")
      }
      )
    } catch {
      case _: NumberFormatException => println(s"\nYou MUST select an integer value between 1 and ${cols.length + 1}.")
        return (cols(input - 2), false)
    }
    (cols(input - 2), true)
  }

  def promptUsrConstraintType(col: String): (String, Boolean) = {
    println(s"\nYou have selected the $col column:\n")
    println("1.) All planets with some value EQUAL TO.")
    println("2.) All planets with some value GREATER THAN.")
    println("3.) All planets with some value LESS THAN.\n")

    print("\nEnter the number for the type of filter you would like to use: ")

    var input = -1
    try {
      breakable(
        while(true) {
          input = readInt()
          if (input >= 1 && input <= 3) {
            break
          }
          print(s"Please select a valid choice from 1 to 3: ")
        }
      )
    } catch {
      case _: NumberFormatException => println(s"\nYou MUST select an integer value between 1 and 3.")
        return (getQueryType(input), false)
    }
    (getQueryType(input), true)
  }

  def getQueryType(choice: Int): String = choice match {
    case 1 => "EQUAL TO"
    case 2 => "GREATER THAN"
    case 3 => "LESS THAN"
    case _ => "Goes with the exception"
  }
}
