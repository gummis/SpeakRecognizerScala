package sly.speakrecognizer.hmm.discrete.algorithm

import sly.speakrecognizer.hmm.discrete.domain.ObservationDiscrete
import sly.speakrecognizer.hmm.discrete.domain.SequenceDiscrete
import sly.speakrecognizer.hmm.discrete.domain.HmmDiscrete

object BetaDiscreteCalculator{

  def calcBeta(hmm: HmmDiscrete, sequence: SequenceDiscrete):Array[Array[Double]] = calcBeta(hmm, sequence.length, sequence.observations.reverseIterator)
  def calcBeta(hmm: HmmDiscrete, observations: Array[ObservationDiscrete]):Array[Array[Double]] = calcBeta(hmm, observations.length, observations.reverseIterator)

  private def calcBeta(hmm: HmmDiscrete, observationsLength: Int, sequenceReverseIterator: Iterator[ObservationDiscrete]) = {

    val beta = Array.ofDim[Double](observationsLength, hmm.N)

    //1. Inicialization
    for (i <- 0 until hmm.N)
      beta.last(i) = 1.0

    //2. Induction
    for (t <- observationsLength - 2 to 0 by -1) {
      val obs = sequenceReverseIterator.next;
      for (i <- 0 until hmm.N) {
        var sum = 0.0;
        for (j <- 0 until hmm.N)
          sum += beta(t+1)(j) * hmm.ab(i,j,obs)
        beta(t)(i) = sum
      }
    }
    beta
  }

}