package KeplerDataReader

trait Column {
  val name: String
  val value: Any
}

class PlanetName(override val value: String) extends Column {
  override val name: String = "yearDiscovered"
}

class HostStar(override val value: String) extends Column {
  override val name: String = "yearDiscovered"
}

class DiscoverYear(override val value: Int) extends Column {
  override val name: String = "yearDiscovered"
}

class OrbitalPeriod(override val value: Double) extends Column {
  override val name: String = "orbitalPeriod"
}

class Radius(override val value: Double) extends Column {
  override val name: String = "radius"
}

class Mass(override val value: Double) extends Column {
  override val name: String = "mass"
}

class Temperature(override val value: Double) extends Column {
  override val name: String = "temperature"
}

class StellarMass(override val value: Double) extends Column {
  override val name: String = "stellarMass"
}

class StellarRadius(override val value: Double) extends Column {
  override val name: String = "stellarRadius"
}

class Distance(override val value: Double) extends Column {
  override val name: String = "distance"
}