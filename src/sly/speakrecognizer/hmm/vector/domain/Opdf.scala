package sly.speakrecognizer.hmm.vector.domain

import sly.speakrecognizer.hmm.vector.algorithm.MatrixCommons

object Opdf {

  def apply(mean: Array[Double], covariance: Array[Array[Double]]) = new Opdf(mean, covariance)

  def apply(dimension: Int) = new Opdf(initialMean(dimension), initialCovariance(dimension))

  private def initialMean(dimension: Int) = Array.ofDim[Double](dimension)
  private def initialCovariance(dimension: Int) = MatrixCommons.matrixIdentity(dimension)
}

class Opdf(
  val mean: Array[Double],
  val covariance: Array[Array[Double]]) {

  def dimension = mean.length

  lazy val covarianceL: Array[Array[Double]] = MatrixCommons.decomposeCholesky(covariance)
  lazy val covarianceInv: Array[Array[Double]] = MatrixCommons.inverseCholesky(covarianceL)
  lazy val covarianceDet: Double = MatrixCommons.determinantCholesky(covarianceL)

  def probability(observation: Array[Double]): Double = {

    val vvmm = Array.ofDim[Double](dimension)
    for (i <- 0 until dimension)
      vvmm(i) = observation(i) - mean(i)

    val vmm = vvmm.map(d => Array.fill(1)(d))

    //transponowanie vmm
    val transponseVmm = MatrixCommons.transpose(vmm)
    val covIVmm = MatrixCommons.times(covarianceInv, vmm)
    val transponseVmmcovIVmm = MatrixCommons.times(transponseVmm, covIVmm)
    val expArg = transponseVmmcovIVmm.head.head * -0.5

    val expP = math.exp(expArg)
    val probability = expP / (math.pow(2. * Math.PI, (dimension.toDouble) / 2.0) *
      math.pow(covarianceDet, 0.5))
    probability
  }
}

