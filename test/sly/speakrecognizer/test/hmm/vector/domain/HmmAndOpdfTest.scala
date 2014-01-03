package sly.speakrecognizer.test.hmm.vector.domain

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import sly.speakrecognizer.hmm.vector.domain.Hmm
import sly.speakrecognizer.hmm.vector.domain.Opdf

@RunWith(classOf[JUnitRunner])
class HmmAndOpdfTest extends FunSuite {

  test("Badanie metod Hmm") {
    val hmm = Hmm(
      pi = Array(0.5, 0.5),
      a = Array(Array(0.8, 0.2), Array(0.3, 0.7)),
      b = Array(Opdf(4), Opdf(4)))
    assert(hmm.N === 2, "Metoda N niedziała")
    assert(hmm. vectorDimension === 4, "Metoda vectorDimension nie działa")
    
    val list = Seq(1,2,3,4,56,223,45,3,4,5)
    
    val resFold = list.fold(0){ (b,a) => println(s"fold: $a $b"); a+b}
    val resFoldLeft = list.foldLeft(0){ (b,a) => println(s"foldLeft: $a $b"); a+b}
    val resFoldRight = list.foldRight(0){ (b,a) => println(s"foldRight: $a $b"); a+b}
    
    println(s"resFold: $resFold")
    println(s"resFoldLeft: $resFoldLeft")
    println(s"resFoldRight: $resFoldRight")
  }

  test("Badanie metod Opdf") {
    val opdf = Opdf(Array(1.0,-0.5,0.5,-1.0),
    Array(
        Array(0.20,0.10,0.30,0.40),
        Array(0.10,0.25,0.35,0.25),
        Array(0.30,0.35,0.05,0.20),
        Array(0.40,0.25,0.20,0.35)
    ));
   val obs1 = Array(1.0,2.0,3.0,4.0)
   val obs2 = Array(-1.0,2.0,3.0,-4.0)
   val obs3 = Array(0.0,0.5,-0.3,4.0)
   val obs4 = Array(100.0,250.0,-13003.0,0.01)
   
   val template = new HmmAndOpdfTemplate();
   
   val prob1 = opdf.probability(obs1)
   val prob2 = opdf.probability(obs2)
   val prob3 = opdf.probability(obs3)
   val prob4 = opdf.probability(obs4)
   
   val prob1Pat = template.calcOpdfProbability(obs1, opdf.mean, opdf.covariance);
   val prob2Pat = template.calcOpdfProbability(obs2, opdf.mean, opdf.covariance);
   val prob3Pat = template.calcOpdfProbability(obs3, opdf.mean, opdf.covariance);
   val prob4Pat = template.calcOpdfProbability(obs4, opdf.mean, opdf.covariance);
   
   assert(prob1 === prob1Pat, "Opdf wyliczyło źle prawodopodobieństwo")
   assert(prob2 === prob2Pat, "Opdf wyliczyło źle prawodopodobieństwo")
   assert(prob3 === prob3Pat, "Opdf wyliczyło źle prawodopodobieństwo")
   assert(prob4 === prob4Pat, "Opdf wyliczyło źle prawodopodobieństwo")
   
  }

  
  /*
  test("Inicjalny stan HMM") {
    val compareHmm = Hmm(
      pi = Array(0.25, 0.25, 0.25, 0.25),
      a = Array(
    		  Array(0.25, 0.25, 0.25, 0.25),
    		  Array(0.25, 0.25, 0.25, 0.25),
    		  Array(0.25, 0.25, 0.25, 0.25),
    		  Array(0.25, 0.25, 0.25, 0.25)
      ),
      b = Array(
          Opdf(), 
          Opdf(),
          Opdf(),
          Opdf()
          ))
    val iniHmm = Hmm(4)
    
    assert(compareHmm.pi === iniHmm.pi, "Wektor PI inicjalnego HMM nieprawidłowy")
    assert(compareHmm.a === iniHmm.a, "Macierz A inicjalnego HMM nieprawidłowy")
    assert(compareHmm.b.length === iniHmm.b.length, "Wektor B inicjalnego HMM nieprawidłowy: Rózna ilość")
  }
  */
}