package sly.speakrecognizer.audio

/** Moduł przyjmuje dane, przetwarza, i zwraca Some(data) lub nie None */
trait Module {
  def process(input: Data): Option[Data]
}