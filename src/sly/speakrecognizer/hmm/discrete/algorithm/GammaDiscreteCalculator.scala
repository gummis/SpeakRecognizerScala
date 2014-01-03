package sly.speakrecognizer.hmm.discrete.algorithm

object GammaDiscreteCalculator {

  def calcGamma(xi: Array[Array[Array[Double]]]) = {
    val intLength = xi(0).length
    val gamma = Array.fill(xi.length + 1, intLength)(0.0)
    for(t <- 0 until xi.length ; i <- 0 until intLength ; j <- 0 until intLength)
    	gamma(t)(i) += xi(t)(i)(j)
    
    for( j <- 0 until intLength ; i <- 0 until intLength)
    	gamma(xi.length)(j) += xi(xi.length - 1)(i)(j)
		
	gamma
  }
  
  def calcGamma(alpha: Array[Array[Double]], beta: Array[Array[Double]]) = {
    val statesLength = alpha.head.length
    val gamma = Array.ofDim[Double](alpha.length,statesLength)
    for(t <- 0 until alpha.length){
      var sum = 0.0
      for(i <- 0 until statesLength){
        sum += alpha(t)(i) * beta(t)(i)
      }
      for(i <- 0 until statesLength){
        gamma(t)(i) = alpha(t)(i) * beta(t)(i) / sum
      }
    }
    gamma
  }
  
}