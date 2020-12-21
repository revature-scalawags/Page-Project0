package KeplerDataReader

case class UI() {

  def welcomeMessage(): Unit = {
    println("Welcome to Kepler Data Reader.\n")
    print("Loading exoplanet data file...")
  }

  def logger(message: String): Unit = {
    println(message)
  }

/*  def promptForColumns(column: String): Boolean = {

  }*/
}
