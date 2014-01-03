package sly.speakrecognizer.hmm.vector.domain

object Hmm {
  def apply(
		pi: Array[Double],
		a: Array[Array[Double]],
		b: Array[Opdf] 
  ) = new Hmm(pi,a,b)

  def apply(N: Int, vectorDimension: Int) = new Hmm(initialPi(N),initialA(N),initialB(N,vectorDimension))

  def initialPi(N: Int) = Array.fill(N)(1.0 / N)
  def initialA(N: Int) = Array.fill(N,N)(1.0 / N)
  def initialB(N: Int, vectorDimension: Int) = Array.fill(N)(Opdf(vectorDimension))
}

class Hmm(
		val pi: Array[Double],
		val a: Array[Array[Double]],
		val b: Array[Opdf] 
) {
	def N = pi.length
	def vectorDimension = b.head.dimension
	def pib(n: Int,observation: Array[Double]) = pi(n) * b(n).probability(observation)
	def ab(from: Int,to: Int,observation: Array[Double]) = a(from)(to) * b(to).probability(observation)
	def ba(from: Int,to: Int,observation: Array[Double]) = a(from)(to) * b(from).probability(observation)
}