package sly.speakrecognizer.audio

trait Data {
	def size: Int
	def iterator: Iterator[Data]
	def asArrayDouble(index: Int) : Array[Double]
	def isArrayDouble : Boolean 
	def isMultiData : Boolean
}