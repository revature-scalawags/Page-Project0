package KeplerDataReader

import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.{Duration, SECONDS}
import org.mongodb.scala.model.Filters.equal
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry

class PlanetDAO(mongoClient: MongoClient) {
  protected val codecRegistry: CodecRegistry = fromRegistries(
    fromProviders(classOf[Planet]),
    MongoClient.DEFAULT_CODEC_REGISTRY
  )
  val db: MongoDatabase = mongoClient.getDatabase("kepler_db").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Planet] = db.getCollection("exoplanets")

  private def getResults[T](obs: Observable[T]): Seq[T] = {
    Await.result(obs.toFuture(), Duration(10, SECONDS))
  }

  def getAll: Seq[Any] = getResults(collection.find())

  def getByName(name: String): Seq[Any] = {
    getResults(collection.find(equal("name", name)))
  }

  def createNewCollection(table: List[String]): Unit = {
    val header = table.head.split(",")
    val docs = table.tail.map(row => (header zip row.split(",")).toMap)

    try {
      docs.foreach(m => {
        val p = Planet()
        for ((k, v) <- m) {getColumnValues(k, v, p)}
        getResults(collection.insertOne(p))
      })
    } catch {
      case e: Throwable => println(e)
    }
  }

  private def getColumnValues(key: String, value: String, p: Planet): Unit = key match {
    case "planet" => p.name = value
    case "host_star" => p.hostStar = value
    case "discovery_year" => p.yearDiscovered = if (value == "") return else value.toInt
    case "orbital_Period_(days)" => p.orbitalPeriod = if (value == "") return else value.toDouble
    case "radius_(earth r)" => p.radius = if (value == "") return else value.toDouble
    case "mass_(earth m)" => p.mass = if (value == "") return else value.toDouble
    case "eq_temp_(K)" => p.eqTemp = if (value == "") return else value.toFloat
    case "stellar_radius_(solar r)" => p.stellarRadius = if (value == "") return else value.toDouble
    case "stellar_mass_(solar m)" => p.stellarMass = if (value == "") return else value.toDouble
    case "distance_(pc)" => p.distance = if (value == "") return else value.toDouble
    case _  =>
  }
}
