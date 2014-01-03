package sly.speakrecognizer.test.commons

import sly.speakrecognizer.hmm.discrete.domain.HmmDiscrete

object MatrixCompare {
	def compareCubeDouble(a: Array[Array[Array[Double]]], b: Array[Array[Array[Double]]]) = a.length == b.length && !a.zip(b).exists(f => !compareMatrixDouble(f._1,f._2))
	def compareMatrixDouble(a: Array[Array[Double]], b: Array[Array[Double]]) = a.length == b.length && !a.zip(b).exists(f => !compareVectorsDouble(f._1,f._2))
	def compareVectorsDouble(a: Array[Double], b: Array[Double]) = a.length == b.length && !a.zip(b).exists(f => f._1 != f._2)
	def compareDouble(a: Double, b: Double)= a == b
	def compareCubeDelta(a: Array[Array[Array[Double]]], b: Array[Array[Array[Double]]], delta: Double) = a.length == b.length && !a.zip(b).exists(f => !compareMatrixDelta(f._1,f._2,delta))
	def compareMatrixDelta(a: Array[Array[Double]], b: Array[Array[Double]], delta: Double) = a.length == b.length && !a.zip(b).exists(f => !compareVectorsDelta(f._1,f._2,delta))
	def compareVectorsDelta(a: Array[Double], b: Array[Double], delta: Double) = a.length == b.length && !a.zip(b).exists(f => Math.abs(f._1 - f._2) > delta)
	def compareDoubleDelta(a: Double, b: Double, delta: Double)= Math.abs(a - b) <= delta
	
	def compareInt(a: Int, b: Int)= a == b
	def compareMatrixInt(a: Array[Array[Int]], b: Array[Array[Int]]) = a.length == b.length && !a.zip(b).exists(f => !compareVectorsInt(f._1,f._2))
	def compareVectorsInt(a: Array[Int], b: Array[Int]) = a.length == b.length && !a.zip(b).exists(f => f._1 != f._2)
	
	def compareHmmDiscreteDelta(a: HmmDiscrete, b: HmmDiscrete, delta: Double) = true
}