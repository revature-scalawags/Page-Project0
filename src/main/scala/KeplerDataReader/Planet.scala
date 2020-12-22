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
    s"Planet: $planet | Host Star: $host_star | " +
      s"${if(discovery_year != 0) s"Discovery Year: $discovery_year"} | " +
      s"${if(orbital_period_days != 0) s"Orbital Period (days):$orbital_period_days"} | " +
      s"${if(radius_earth != 0) s"Radius (earth): $radius_earth"} | " +
      s"${if(mass_earth != 0) s"Mass (earth): $mass_earth"} | " +
      s"${if(eq_temp_K != 0) s"Equilibrium Temp (K): $eq_temp_K"} | " +
      s"${if(stellar_mass_sol != 0) s"Stellar Mass (sol): $stellar_mass_sol"} | " +
      s"${if(stellar_radius_sol != 0) s"Stellar Radius (sol): $stellar_radius_sol"} | " +
      s"${if(distance_pc != 0) s"Distance (parsec): $distance_pc"} | "
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

