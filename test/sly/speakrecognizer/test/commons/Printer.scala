package sly.speakrecognizer.test.commons

import grizzled.slf4j.Logger

object Printer {
	def anyToString(name: String, any: Any) = s"Wartość '${name}' = ${any.toString} |"

	def arrayIntToString(name: String, array: Array[Int]) = {
	  val sb = new StringBuilder
	  sb ++= "Tablica typów Int, wartość '" + name + "' :"
	  sb ++= array.mkString("\n","\t","\n")
	  sb ++= "Koniec tablicy typów Int, wartość '" + name + "'"
	  sb.toString
	}
	
	def matrixIntToString(name: String, matrix: Array[Array[Int]]) = {
	  val sb = new StringBuilder
	  sb ++= "Matryca typów Int, wartość '" + name + "' :\n"
	  matrix.foreach(sb ++= _.mkString("","\t","\n"))
	  sb ++= "Koniec matrycy typów Int, wartość '" + name + "'"
	  sb.toString
	}

	def arrayDoubleToString(name: String, array: Array[Double]) = {
	  val sb = new StringBuilder
	  sb ++= "Tablica typów Double, wartość '" + name + "' :"
	  sb ++= array.mkString("\n","\t","\n")
	  sb ++= "Koniec tablicy typów Double, wartość '" + name + "'"
	  sb.toString
	}
	
	def matrixDoubleToString(name: String, matrix: Array[Array[Double]]) = {
	  val sb = new StringBuilder
	  sb ++= "Matryca typów Double, wartość '" + name + "' :\n"
	  matrix.foreach(sb ++= _.mkString("","\t","\n"))
	  sb ++= "Koniec matrycy typów Double, wartość '" + name + "'"
	  sb.toString
	}
	
	def cubeDoubleToString(name: String, cube: Array[Array[Array[Double]]]) = {
	  val sb = new StringBuilder
	  sb ++= "Sześcian typów Double, wartość '" + name + "' :\n"
	  var wc = 0
	  cube.foreach{ w =>
	    sb ++= "Matryca " + wc + "\n"
	    wc += 1
	    w.foreach(sb ++= _.mkString("","\t","\n"))
	  }
	  sb ++= "Koniec sześcianu typów Double, wartość '" + name + "'"
	  sb.toString
	}
	
	def anyToConsole(name: String, any: Any) { println(anyToString(name: String, any: Any)) }
	def arrayIntToConsole(name: String, array: Array[Int]) { println(arrayIntToString(name, array)) }
	def matrixIntToConsole(name: String, matrix: Array[Array[Int]]) { println(matrixIntToString(name, matrix)) }
	def arrayDoubleToConsole(name: String, array: Array[Double]) { println(arrayDoubleToString(name, array)) }
	def matrixDoubleToConsole(name: String, matrix: Array[Array[Double]]) { println(matrixDoubleToString(name, matrix)) }
	def cubeDoubleToConsole(name: String, cube: Array[Array[Array[Double]]]) { println(cubeDoubleToString(name, cube)) }

	def anyToLog(name: String, any: Any, log: Logger) { log.debug(anyToString(name: String, any: Any)) }
	def arrayIntToLog(name: String, array: Array[Int], log: Logger) { log.debug(arrayIntToString(name, array)) }
	def matrixIntToLog(name: String, matrix: Array[Array[Int]], log: Logger) { log.debug(matrixIntToString(name, matrix)) }
	def arrayDoubleToLog(name: String, array: Array[Double], log: Logger) { log.debug(arrayDoubleToString(name, array)) }
	def matrixDoubleToLog(name: String, matrix: Array[Array[Double]], log: Logger) { log.debug(matrixDoubleToString(name, matrix)) }
	def cubeDoubleToLog(name: String, cube: Array[Array[Array[Double]]], log: Logger) { log.debug(cubeDoubleToString(name, cube)) }

}