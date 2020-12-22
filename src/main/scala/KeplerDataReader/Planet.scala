package KeplerDataReader

import org.bson.types.ObjectId

case class Planet(
                   _id: ObjectId,
                   var planet: String = "",
                   var host_star: String = "",
                   var discovery_year: Int = 0,
                   var orbital_period_days: Double = 0,
                   var radius_earth: Double = 0,
                   var mass_earth: Double = 0,
                   var eq_temp_K: Float = 0,
                   var stellar_mass_sol: Double = 0,
                   var stellar_radius_sol: Double = 0,
                   var distance_pc: Double = 0
) {

  override def toString: String = {
    s"Planet: $planet | Host Star: $host_star | Discovery Year $discovery_year | Orbital Period: $orbital_period_days | Radius: $radius_earth |" +
      s" Mass: $mass_earth | Temperature: $eq_temp_K | Stellar Mass: $stellar_mass_sol | Stellar Radius: $stellar_radius_sol | Distance: $distance_pc"
  }
}

object Planet {
  def apply(): Planet = new Planet(
    new ObjectId(),
    planet = "",
    host_star = "",
    discovery_year = 0,
    orbital_period_days = 0d,
    radius_earth = 0d,
    mass_earth = 0d,
    eq_temp_K = 0f,
    stellar_mass_sol = 0d,
    stellar_radius_sol = 0d,
    distance_pc = 0d
  )
}

