package sly.speakrecognizer.test.commons

object Searcher {
	def findMaxDouble(values: Array[Double]) = {
	  var maxVal = values.head
	  var maxInd = 0
	  for(i <- 1 until values.length){
	    if(maxVal < values(i)){
	    	maxVal = values(i)
	    	maxInd = i
	    }
	  }
	  (maxVal,maxInd)
	}
	
	def findMinDouble(values: Array[Double]) = {
	  var minVal = values.head
	  var minInd = 0
	  for(i <- 1 until values.length){
	    if(minVal > values(i)){
	    	minVal = values(i)
	    	minInd = i
	    }
	  }
	  (minVal,minInd)
	}
}