package sly.speakrecognizer.commons

case class SRException(val code: Int, message: Option[String] = None) extends Throwable

object SRException {
  val NO_DATA_FROM_MODULE = 1
}