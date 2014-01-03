package sly.speakrecognizer.hmm.vector.algorithm

import sun.security.util.Length

object MatrixCommons {

  def vectorZeros(dimension: Int) = Array.ofDim[Double](dimension)
  def matrixZeros(dimension: Int) = Array.ofDim[Double](dimension,dimension)
  def matrixZeros(width: Int,height: Int) = Array.ofDim[Double](width,height)

  def matrixIdentity(dimension: Int) = {
	  val res = Array.ofDim[Double](dimension,dimension)
	  for (i <- 0 until dimension)
	    res(i)(i) = 1.0
	  res
	}
  
  	def decomposeCholesky(matrix: Array[Array[Double]]):Array[Array[Double]] = {
  	  def res = Array.ofDim[Double](matrix.length,matrix.head.length)
  	  for(j <- 0 until res.length){
  	    val lj = res(j)
  	    var d = 0.0
  	    for(k <- 0 until j){
  	      val lk = res(k)
  	      var s = 0.0
  	      for( i <- 0 until k)
  	        s += lk(i) * lj(i)
  	      s = (matrix(j)(j) - s) / res(k)(k)
  	      lj(k) = s
  	      d += s*s
  	    }
  	    res(j)(j) = math.sqrt(d)
  	    for(k <- j+1 until res.length)
  	      res(j)(k) = 0
  	  }
  	  
  	  res
  	}
  	
  	def determinantCholesky(matrix: Array[Array[Double]]):Double = {
	  var d = 1.0
	  for (i <- 0 until matrix.length)
		d *= matrix(i)(i)
	  d * d
	}
  	
  	def inverseCholesky(matrix: Array[Array[Double]]): Array[Array[Double]] = {
		
		val li = lowerTriangularInverse(matrix)
		val ic = matrixZeros(matrix.length)
		
		for (r <- 0 until matrix.length ; c <- 0 until matrix.length ; i <- 0 until matrix.length)
			ic(r)(c) += li(i)(r) * li(i)(c)
		
		ic
	}
  	
  	def lowerTriangularInverse(matrix: Array[Array[Double]]): Array[Array[Double]] = {
		val lti = Array.ofDim[Double](matrix.length,matrix.length)
		
		for (j <- 0 until matrix.length) {
			
			lti(j)(j) = 1.0 / matrix(j)(j)
			
			for (i <- j + 1 until matrix.length) {
				var sum = 0.0
				
				for (k <- j until i)
					sum -= matrix(i)(k) * lti(k)(j)
				
				lti(i)(j)= sum / matrix(i)(i)
			}
		}
		
		lti
	}
	
  	def transpose(matrix: Array[Array[Double]]): Array[Array[Double]] = {
		val t = Array.ofDim[Double](matrix.head.length,matrix.length)
		
		for (r <- 0 until matrix.length ; c <- 0 until t.length)
				t(c)(r) = matrix(r)(c)
		
		t
	}
  	
  	
  	def times(matrix1: Array[Array[Double]], matrix2: Array[Array[Double]]): Array[Array[Double]] = {

	  val p = matrixZeros(matrix1.length,matrix2.head.length)
		
		for (r <- 0 until matrix1.length ; c <- 0 until matrix2.head.length ; i <- 0 until  matrix2.length )
			p(r)(c) += matrix1(r)(i) * matrix2(i)(c)
		
		p
	}
	
	
	def timesWithVector(matrix: Array[Array[Double]], vector: Array[Double]): Array[Double] = { 
		val p = vectorZeros(matrix.length)
		
		for (r <- 0 until matrix.length ; i <- 0 until matrix.head.length)
			p(r) += matrix(r)(i) * vector(i)
		
		p
	}
	
}