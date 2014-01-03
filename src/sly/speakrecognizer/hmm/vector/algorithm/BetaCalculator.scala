package sly.speakrecognizer.hmm.vector.algorithm

import sly.speakrecognizer.hmm.vector.domain.Hmm
import sly.speakrecognizer.hmm.vector.domain.Sequence
import sly.speakrecognizer.hmm.vector.domain.Observation

class BetaCalculator {

  def calcBeta(hmm: Hmm, sequence: Sequence): Array[Array[Double]] = calcBeta(hmm, sequence.length, sequence.getVectorsReverseIterator)
  def calcBeta(hmm: Hmm, sequence: Array[Observation]): Array[Array[Double]] = calcBeta(hmm, sequence.length, sequence.reverseIterator.map(_.vector))
  def calcBeta(hmm: Hmm, sequence: Array[Array[Double]]): Array[Array[Double]] = calcBeta(hmm, sequence.length, sequence.reverseIterator)

  private def calcBeta(hmm: Hmm, observationsLength: Int, sequenceIterator: Iterator[Array[Double]]): Array[Array[Double]] = {

    val beta = Array.ofDim[Double](observationsLength, hmm.N)

    //1. Inicialization
    for (i <- 0 until hmm.N)
      beta.last(i) = 1.0

    //2. Induction
    for (t <- observationsLength - 2 to 0) {
      val obs = sequenceIterator.next;
      for (i <- 0 until hmm.N) {
        var sum = 0.0;
        for (j <- 0 until hmm.N)
          sum += hmm.ab(i,j,obs) * beta(t+1)(j)
        beta(t)(i) = sum
      }
    }
    return beta;
  }
  
}
