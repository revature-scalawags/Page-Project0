package KeplerDataReader

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters.{equal, gt, lt}
import tour.Helpers._

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}

class PlanetDAO(mongoClient: MongoClient) {
  protected val codecRegistry: CodecRegistry = fromRegistries(
    fromProviders(classOf[Planet]),
    MongoClient.DEFAULT_CODEC_REGISTRY
  )
  val db: MongoDatabase = mongoClient.getDatabase("kepler_db").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Planet] = db.getCollection("exoplanets")

  private def getResults[T](obs: Observable[T]): Seq[T] = {
    Await.result(obs.toFuture(), Duration(20, SECONDS))
  }

  def createNewCollection(table: List[String]): Unit = {
    val header = table.head.split(",")
    val docs = table.tail.map(row => (header zip row.split(",")).toMap)

    try {
      docs.foreach(m => {
        val p = Planet()
        for ((k, v) <- m) getColumnValues(k, v, p)
        getResults(collection.insertOne(p))
      })
    } catch {
      case e: Throwable => println(e)
    }
  }

  def printAll: Unit = collection.find().printResults()

  def printFilteredResults(field: String, constraint: String, value: Any): Unit = constraint match {
    case "EQUAL TO" => collection.find(equal(field, value)).printResults()
    case "GREATER THAN" => collection.find(gt(field, value)).printResults()
    case "LESS THAN" => collection.find(lt(field, value)).printResults()
  }

  private def getColumnValues(key: String, value: String, p: Planet): Unit = key match {
    case "planet" => p.planet = value
    case "host_star" => p.host_star = value
    case "discovery_year" => p.discovery_year = if (value == "") return else value.toInt
    case "orbital_Period_(days)" => p.orbital_period_days = if (value == "") return else value.toDouble
    case "radius_(earth r)" => p.radius_earth = if (value == "") return else value.toDouble
    case "mass_(earth m)" => p.mass_earth = if (value == "") return else value.toDouble
    case "eq_temp_(K)" => p.eq_temp_K = if (value == "") return else value.toFloat
    case "stellar_radius_(solar r)" => p.stellar_radius_sol = if (value == "") return else value.toDouble
    case "stellar_mass_(solar m)" => p.stellar_mass_sol = if (value == "") return else value.toDouble
    case "distance_(pc)" => p.distance_pc = if (value == "") return else value.toDouble
    case _  =>
  }
}
