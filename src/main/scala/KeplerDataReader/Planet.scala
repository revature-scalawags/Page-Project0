package KeplerDataReader

import org.bson.types.ObjectId

case class Planet(
  _id: ObjectId,
  var name: String = "",
  var hostStar: String = "",
  var yearDiscovered: Int = 0,
  var orbitalPeriod: Double = 0,
  var radius: Double = 0,
  var mass: Double = 0,
  var eqTemp: Float = 0,
  var stellarMass: Double = 0,
  var stellarRadius: Double = 0,
  var distance: Double = 0
) {

  override def toString: String = {
    s"Planet: $name | Host Star: $hostStar | Discovery Year $yearDiscovered | Orbital Period: $orbitalPeriod | Radius: $radius |" +
      s" Mass: $mass | Temperature: $eqTemp | Stellar Mass: $stellarMass | Stellar Radius: $stellarRadius | Distance: $distance"
  }
}

object Planet {
  def apply(): Planet = new Planet(
    new ObjectId(),
    name = "",
    hostStar = "",
    yearDiscovered = 0,
    orbitalPeriod = 0d,
    radius = 0d,
    mass = 0d,
    eqTemp = 0f,
    stellarMass = 0d,
    stellarRadius = 0d,
    distance = 0d
  )
}

