package sly.speakrecognizer.hmm.vector.algorithm

import sly.speakrecognizer.hmm.vector.domain.Observation
import sly.speakrecognizer.hmm.vector.domain.Sequence
import sly.speakrecognizer.hmm.vector.domain.Hmm

object AlphaCalculator {

  def calcAlpha(hmm: Hmm, sequence: Sequence): Array[Array[Double]] = calcAlpha(hmm, sequence.length, sequence.getVectorsIterator)
  def calcAlpha(hmm: Hmm, sequence: Array[Observation]): Array[Array[Double]] = calcAlpha(hmm, sequence.length, sequence.iterator.map(_.vector))
  def calcAlpha(hmm: Hmm, sequence: Array[Array[Double]]): Array[Array[Double]] = calcAlpha(hmm, sequence.length, sequence.iterator)

  private def calcAlpha(hmm: Hmm, observationsLength: Int, sequenceIterator: Iterator[Array[Double]]): Array[Array[Double]] = {

    val alpha = Array.ofDim[Double](observationsLength, hmm.N)

    //1. Inicialization
    for (i <- 0 until hmm.N)
      alpha.head(i) = hmm.pib(i, sequenceIterator.next)

    //2. Induction
    for (t <- 1 until observationsLength) {
      val obs = sequenceIterator.next;
      val prev = alpha(t - 1)
      val cur = alpha(t);
      for (j <- 0 until hmm.N) {
        var sum = 0.0;
        for (i <- 0 until hmm.N)
          sum += prev(i) * hmm.a(i)(j)
        cur(j) = sum * hmm.b(j).probability(obs)
      }
    }
    return alpha;
  }

}