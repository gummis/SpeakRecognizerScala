package sly.speakrecognizer.hmm.discrete.domain

case class ObservationDiscrete( val pos: Int )

object Rainy extends ObservationDiscrete(0)
object Cloudy extends ObservationDiscrete(1)
object Sunny extends ObservationDiscrete(2)

case class StateDiscrete( val pos: Int )

object S1 extends StateDiscrete(0)
object S2 extends StateDiscrete(1)
object S3 extends StateDiscrete(2)


