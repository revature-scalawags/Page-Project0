import org.scalatest.funsuite.AnyFunSuite
import KeplerDataReader.{Planet, PlanetDAO, UI}
import org.mongodb.scala.MongoClient

class KeplerDRSpecs extends AnyFunSuite {
  test("instantiate new Planet") {

    val p = Planet.apply()
    assert(p.planet == "")
    assert(p.orbital_period_days == 0)
  }
  test("returns a string for the query type from user, numerical input") {
    val ui = new UI
    val res1 = ui.getQueryType(3)
    val res2 = ui.getQueryType(1)
    assert(res1 == "LESS THAN")
    assert(res2 == "EQUAL TO")
  }
  test("assigns values to a planet object from data set and returns and returns it") {
    val p = Planet.apply()
    val db = new PlanetDAO(MongoClient())
    val (key1, value1) = ("discovery_year", "1492")
    val (key2, value2) = ("radius_earths", "1.234")
    val (key3, value3) = ("stellar_mass_sol", "3.333")
    val (key4, value4) = ("eq_temp_K", "4.568")

    p.getColumnValues(key1, value1)
      assert(p.discovery_year == 1492)

    p.getColumnValues(key2, value2)
          assert(p.radius_earth == 1.234d)

    p.getColumnValues(key3, value3)
      assert(p.stellar_mass_sol == 3.333d)

    p.getColumnValues(key4, value4)
      assert(p.eq_temp_K == 4.568f)
  }
}