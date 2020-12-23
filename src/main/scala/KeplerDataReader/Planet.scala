package KeplerDataReader

import scala.Console.{RESET => rt, BLUE => bu}
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

  override def toString: String =
    s"""Planet: $rt$bu$planet$rt
        Host Star: $host_star
        ${if (discovery_year != 0) s"Discovery Year: $discovery_year" else "Discovery Year: Unavailable"}
        ${if (orbital_period_days != 0) s"Orbital Period (days):$orbital_period_days" else "Orbital Period (days): Unavailable"}
        ${if (radius_earth != 0) s"Radius (earth): $radius_earth" else "Radius (earth): Unavailable"}
        ${if (mass_earth != 0) s"Mass (earth): $mass_earth" else "Mass (earth): Unavailable"}
        ${if (eq_temp_K != 0) s"Equilibrium Temp (K): $eq_temp_K" else "Equilibrium Temp (K): Unavailable"}
        ${if (stellar_mass_sol != 0) s"Stellar Mass (sol): $stellar_mass_sol" else "Stellar Mass (sol): Unavailable"}
        ${if (stellar_radius_sol != 0) s"Stellar Radius (sol): $stellar_radius_sol" else "Stellar Radius (sol): Unavailable"}
        ${if (distance_pc != 0) s"Distance (parsec): $distance_pc" else "Distance (parsec): Unavailable"}
     """.stripMargin
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

