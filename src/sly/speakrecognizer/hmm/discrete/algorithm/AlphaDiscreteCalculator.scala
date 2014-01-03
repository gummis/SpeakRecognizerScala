package sly.speakrecognizer.hmm.discrete.algorithm

import sly.speakrecognizer.hmm.discrete.domain.ObservationDiscrete
import sly.speakrecognizer.hmm.discrete.domain.SequenceDiscrete
import sly.speakrecognizer.hmm.discrete.domain.HmmDiscrete

object AlphaDiscreteCalculator{

  def calcAlpha(hmm: HmmDiscrete, sequence: SequenceDiscrete):Array[Array[Double]] = calcAlpha(hmm, sequence.length, sequence.observations.iterator)
  def calcAlpha(hmm: HmmDiscrete, observations: Array[ObservationDiscrete]):Array[Array[Double]] = calcAlpha(hmm, observations.length, observations.iterator)

  private def calcAlpha(hmm: HmmDiscrete, observationsLength: Int, sequenceIterator: Iterator[ObservationDiscrete]) = {

    val alpha = Array.ofDim[Double](observationsLength, hmm.N)

    //1. Inicialization
    val firstObservation = sequenceIterator.next
    for (i <- 0 until hmm.N)
      alpha.head(i) = hmm.pib(i, firstObservation)

    //2. Induction
    for (t <- 1 until observationsLength) {
      val obs = sequenceIterator.next;
      val prev = alpha(t - 1)
      val cur = alpha(t);
      for (j <- 0 until hmm.N) {
        var sum = 0.0;
        for (i <- 0 until hmm.N)
          sum += prev(i) * hmm.a(i)(j)
        cur(j) = sum * hmm.b(j)(obs.pos)
      }
    }
    alpha
  }
  
  def probabilityFromAlpha(alpha: Array[Array[Double]]) = alpha.last.foldLeft(0.0)(_+_)
}