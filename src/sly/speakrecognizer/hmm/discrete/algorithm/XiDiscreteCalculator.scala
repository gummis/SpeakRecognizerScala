package sly.speakrecognizer.hmm.discrete.algorithm

import sly.speakrecognizer.hmm.discrete.domain.ObservationDiscrete
import sly.speakrecognizer.hmm.discrete.domain.HmmDiscrete
import sly.speakrecognizer.hmm.discrete.domain.SequenceDiscrete

object XiDiscreteCalculator {
  def calcXi(hmm: HmmDiscrete, sequence: SequenceDiscrete,alpha: Array[Array[Double]], beta: Array[Array[Double]]):
	  Array[Array[Array[Double]]] = calcXi(hmm, sequence.length, sequence.observations.iterator,alpha,beta)

  def calcXi(hmm: HmmDiscrete, observations: Array[ObservationDiscrete],alpha: Array[Array[Double]], beta: Array[Array[Double]]):
  Array[Array[Array[Double]]] = calcXi(hmm, observations.length, observations.iterator,alpha,beta)

  private def calcXi(hmm: HmmDiscrete, observationsLength: Int, sequenceIterator: Iterator[ObservationDiscrete], 
      alpha: Array[Array[Double]], beta: Array[Array[Double]]) = {

	  val probability = AlphaDiscreteCalculator.probabilityFromAlpha(alpha);
	  val truncLength = observationsLength -1
	  val xi = Array.ofDim[Double](truncLength, hmm.N, hmm.N)

	  sequenceIterator.next
		
	  for (t <- 0 until truncLength){
		  val obs = sequenceIterator.next
		  for(i <- 0 until hmm.N ; j <- 0 until hmm.N)
				xi(t)(i)(j) = alpha(t)(i) * hmm.ab(i,j,obs) * beta(t+1)(j) / probability
	  }
	  xi
  }
  
}