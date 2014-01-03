package sly.speakrecognizer.hmm.discrete.domain

class SequenceDiscrete(val observations: Array[ObservationDiscrete]){
	def length = observations.length
}

class SequencesDiscrete(val sequences: Array[SequenceDiscrete]){
	def length = sequences.length
}
