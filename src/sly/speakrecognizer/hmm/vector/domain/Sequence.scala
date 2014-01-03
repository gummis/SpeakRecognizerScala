package sly.speakrecognizer.hmm.vector.domain

trait Sequence {
	def getVectorsArray: Array[Array[Double]]
	def getVectorsIterator: Iterator[Array[Double]]
	def getVectorsReverseIterator: Iterator[Array[Double]]
	def length:Int
}

class DoubleSequence(val vectors:Array[Array[Double]]) extends Sequence{
	override def getVectorsArray: Array[Array[Double]] = vectors
	override def getVectorsIterator: Iterator[Array[Double]] = vectors.iterator
	override def getVectorsReverseIterator: Iterator[Array[Double]] = vectors.reverseIterator
	override def length:Int = vectors.length
}

class ObservationSequence(val observations:Array[Observation]) extends Sequence{
	override def getVectorsArray = observations.map(_.vector)
	override def getVectorsIterator: Iterator[Array[Double]] = observations.iterator.map(_.vector)
	override def getVectorsReverseIterator: Iterator[Array[Double]] = observations.reverseIterator.map(_.vector)
	override def length = observations.length
}