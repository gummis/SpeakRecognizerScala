package sly.speakrecognizer.hmm.vector.domain

class Observation(val vector: Array[Double]){
  def L = vector.length
}