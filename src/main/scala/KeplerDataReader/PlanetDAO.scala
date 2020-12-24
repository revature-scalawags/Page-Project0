package KeplerDataReader

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters.{equal, gt, lt, and}
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

  def closeConnection(): Unit = {
    try {
      mongoClient.close()
    } catch {
      case e: Throwable => println(e)
    }
  }

  def createNewCollection(table: List[String]): Unit = {
    val header = table.head.split(",")
    val docs = table.tail.map(row => (header zip row.split(",")).toMap)

    try {
      docs.foreach(m => {
        val p = Planet()
        for ((k, v) <- m) p.getColumnValues(k, v)
        getResults(collection.insertOne(p))
      })
    } catch {
      case e: Throwable => println(e)
    }
  }

  def printAll(): Unit = collection.find().printResults()

  def printFilteredResults(field: String, constraint: String, value: Any): Unit = constraint match {
    case "EQUAL TO" => try {
      collection.find(equal(field, value)).printResults()
    } catch {
      case e: Throwable => println(e)
    }
    case "GREATER THAN" => try {
      collection.find(gt(field, value)).printResults()
    } catch {
      case e: Throwable => println(e)
    }
    case "LESS THAN" => try {
      collection.find(and(gt(field, 0), lt(field, value))).printResults()
    } catch {
      case e: Throwable => println(e)
    }
  }
}
