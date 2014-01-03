package sly.speakrecognizer.hmm.discrete.algorithm

import sly.speakrecognizer.hmm.discrete.domain.HmmDiscrete
import sly.speakrecognizer.hmm.discrete.domain.ObservationDiscrete
import sly.speakrecognizer.hmm.discrete.domain.SequenceDiscrete

object DeltaPsiDiscreteCalculator {

  def calcDeltaPsi(hmm: HmmDiscrete, sequence: SequenceDiscrete):Pair[Array[Array[Double]],Array[Array[Int]]] = calcDeltaPsi(hmm, sequence.length, sequence.observations.iterator)
  def calcDeltaPsi(hmm: HmmDiscrete, observations: Array[ObservationDiscrete]):Pair[Array[Array[Double]],Array[Array[Int]]] = calcDeltaPsi(hmm, observations.length, observations.iterator)

  
  private def calcDeltaPsi(hmm: HmmDiscrete, observationsLength: Int, sequenceIterator: Iterator[ObservationDiscrete]) = {
    
    val delta = Array.ofDim[Double](observationsLength,hmm.N)
    val psi = Array.ofDim[Int](observationsLength,hmm.N)
    
    //inicialization
    val iniObservation = sequenceIterator.next
    for(i <- 0 until hmm.N){
      delta.head(i) = hmm.pib(i, iniObservation)
      psi.head(i) = 0
    }
    for(t <- 1 until observationsLength){
      val observation = sequenceIterator.next
      val backDelta = delta(t-1)
      val curDelta = delta(t)
      val curPsi = psi(t)
      for(j <- 0 until hmm.N){
        var maxDelta = backDelta(0) * hmm.a(0)(j)
		var maxPsi = 0
		for(i <- 1 until hmm.N){
		  val thisDelta = backDelta(i) * hmm.a(i)(j)
		  if(maxDelta < thisDelta){
				maxDelta = thisDelta;
				maxPsi = i;
		  }
		}

		curDelta(j) = maxDelta * hmm.b(j)(observation.pos)
		curPsi(j) = maxPsi
      }
    }
    (delta,psi)
  }
}