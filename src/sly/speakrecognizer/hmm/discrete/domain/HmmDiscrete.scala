package sly.speakrecognizer.hmm.discrete.domain

object HmmDiscrete {
  def apply(
		pi: Array[Double],
		a: Array[Array[Double]],
		b: Array[Array[Double]] 
  ) = new HmmDiscrete(pi,a,b)

  def apply(N: Int, statesLength: Int) = new HmmDiscrete(initialPi(N),initialA(N),initialB(N,statesLength))

  def initialPi(N: Int) = Array.fill(N)(1.0 / N)
  def initialA(N: Int) = Array.fill(N,N)(1.0 / N)
  def initialB(N: Int, statesLength: Int) = Array.fill(N,statesLength)(1.0/statesLength)
}

class HmmDiscrete(
		val pi: Array[Double],
		val a: Array[Array[Double]],
		val b: Array[Array[Double]] 
) {
	def N = pi.length
	def stateLength = b.head.length
	def pib(n: Int,observation: ObservationDiscrete) = pi(n) * b(n)(observation.pos)
	def ab(from: Int,to: Int, observation: ObservationDiscrete) = a(from)(to) * b(to)(observation.pos)
	def ba(from: Int,to: Int, observation: ObservationDiscrete) = a(from)(to) * b(from)(observation.pos)
}