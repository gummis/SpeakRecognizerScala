package sly.speakrecognizer.test.hmm.vector.algorithm

import sly.speakrecognizer.hmm.vector.algorithm.MatrixCommons
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import be.ac.ulg.montefiore.run.distributions.SimpleMatrix
import scala.util.Random
import java.util.Date
import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[JUnitRunner])
class MatrixCommonsTest extends FunSuite{

  val rand = new Random(new Date().getTime())
  private def random(min: Int, max: Int) = {
    min + rand.nextInt(max - min)
  }
  
  
  test("vectorZeros"){
    val dim = random(10,20)
    println("vectorZerosDim=" + dim)
    val matrix = MatrixCommons.vectorZeros(dim)
    assert(matrix.length === dim)
    List.fromArray(matrix).foreach(d => assert(d == 0.0))
  }
  
  test("matrixZeros"){
    val dim = random(10,20)
    println("matrixSqZerosDim=" + dim)
    val matrixsq = MatrixCommons.matrixZeros(dim)
    assert(matrixsq.length === dim)
    List.fromArray(matrixsq).foreach{ a =>
    	assert(a.length === dim)
        List.fromArray(a).foreach(d => assert(d == 0.0))
    }
    
    val w = random(10,20)
    val h = random(10,20)
    println("matrixZeros: Width=" + w + " Height=" + h)
    val matrix = MatrixCommons.matrixZeros(w,h)
    assert(matrix.length === w)
    List.fromArray(matrix).foreach{ a =>
      	assert(a.length === h)
        List.fromArray(a).foreach(d => assert(d == 0.0))
    }
  }
  
   test("matrixIdentity"){
     val dim = random(10,20)
     println("matrixIdentityDim=" + dim)
     val matrix = MatrixCommons.matrixIdentity(dim)
     assert(matrix.length === dim)
     var col = 0
     var row = -1
     List.fromArray(matrix).foreach{ a =>
       col = 0
       row = row + 1
       assert(a.length === dim)
        List.fromArray(a).foreach{d => 
          if(col == row) 
            assert(d === 1.0)
          else 
            assert(d === 0.0)
           
          col = col + 1
       }
     }
   }
  
}