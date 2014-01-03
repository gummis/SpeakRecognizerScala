package sly.speakrecognizer.test.audio

import org.apache.commons.math3.distribution.MultivariateNormalDistribution

object MaximumLikehoodFitter {
	
	def fit(data: Array[Array[Double]])	= {

		val dimension = data.head.length
		val length = data.length

		// Compute mean
		val mean = Array.ofDim[Double](dimension)
		for (r <- 0 until dimension ; i <- 0 until length)
			mean(r) = mean(r) + data(i)(r)
		
		val doubleLength = length.toDouble
		for (r <- 0 until dimension) {
			mean(r) = mean(r) / doubleLength
		}
		
		// Compute covariance
		val covariance = Array.ofDim[Double](dimension,dimension)
		for (obs <- data) {
			val omm = Array.ofDim[Double](obs.length)
			
			for (j <- 0 until obs.length)
				omm(j) = obs(j) - mean(j)
			
			for (r <- 0 until dimension ; c <- 0 until dimension)
				covariance(r)(c) += omm(r) * omm(c) / doubleLength
		}
		
		new MultivariateNormalDistribution(mean, covariance);

	}
	
}
