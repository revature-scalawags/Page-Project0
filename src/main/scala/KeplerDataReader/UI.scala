package KeplerDataReader

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
        |
        |Options:
        |  -help, -h                             render aid
        |
        |  -Columns:
        |   -yr                                   Discovery Year
        |   -op                                   Orbital Period
        |   -m                                    Planetary Mass (earth mass)
        |   -r                                    Radius (earth radius)
        |   -t                                    Equilibrium Temperature (Kelvin)
        |   -d                                    Distance (parsecs) i.e. 1pc = 3.26 light years
        |   -sm                                   Stellar Mass (solar mass)
        |   -sr                                   Stellar Radius (solar radius)
        |
        |   --a                                   Generate a table with all possible columns.
        |
        |
              """.stripMargin)
    System.exit(0)
  }

/*  def promptForColumns(column: String): Boolean = {

  }*/
}
