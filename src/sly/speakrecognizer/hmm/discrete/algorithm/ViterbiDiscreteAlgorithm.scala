package sly.speakrecognizer.hmm.discrete.algorithm

import sly.speakrecognizer.hmm.discrete.domain.SequenceDiscrete
import sly.speakrecognizer.hmm.discrete.domain.HmmDiscrete
import sly.speakrecognizer.hmm.discrete.domain.ObservationDiscrete
import sly.speakrecognizer.hmm.discrete.domain.SequenceDiscrete
import sly.speakrecognizer.test.commons.Searcher

object ViterbiDiscreteAlgorithm {
  
  def calcViterbi(hmm: HmmDiscrete, sequence: SequenceDiscrete) = {
    val (delta,psi) = DeltaPsiDiscreteCalculator.calcDeltaPsi(hmm, sequence)
    val (maxProbability,index) = Searcher.findMaxDouble(delta.last)
    val stateSequence = Array.ofDim[Int](sequence.length)
    stateSequence(stateSequence.length - 1) = index
    for(t <- stateSequence.length - 2 to 0 by -1){
      stateSequence(t) = psi(t+1)(stateSequence(t+1))
    }
    (maxProbability,stateSequence)
  }
}