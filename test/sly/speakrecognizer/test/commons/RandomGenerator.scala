package sly.speakrecognizer.test.commons

import scala.util.Random
import java.util.Date
import sly.speakrecognizer.hmm.discrete.domain.HmmDiscrete


/**
 * Generator losowych zmiennych, tablic, macierzy
 */
class RandomGenerator {
  
  private val random = new Random(new Date().getTime());
  
  /** generuje losową liczbę typu double */
  def randomDouble = random.nextDouble
  
  /** generuje losowa liczbę całkowitą typu Int */
  def randomInt = random.nextInt
  
  /** generuje losową liczbę całkowią typu Int
   *  max: maksymalna wartość z wyłączeniem
   */
  def randomInt(max: Int) = random.nextInt(max)
  
  /**generuje losową liczbę całkowitą od do 
   * min: minimalna wartość
   * max maksymalna wartość
   */
  def randomInt(min: Int, max: Int) = if(min == max) min else random.nextInt(max-min)+min
  
  /**
   * generuje losową tablicę jednowymiarową które rozmiar też jest losowany
   * rozkład wartości w tej tablicy jest zbieżny do wartości 1
   * minWidth: minimalna szerokość tablicy
   * maxWidth: maksymlna szerokość tablicy
   */
  def randomConvergentVectorDouble(minWidth: Int = 1, maxWidth: Int = 100) = {
	  val width = randomInt(minWidth,maxWidth)
	  val array = Array.fill(width)(randomDouble)
	  val sum = array.fold(0.0)((sum,value) => sum + value)
	  array.map(_/sum)
  }
  
  /**
   * generuje losową tablicę dwywymiarową,której szerokść i wysokość jest losowana 
   * rozkład wartości po rzędach jest zbieżny do 1,
   * minWidth: minimalna szerokość
   * maxWidth: minimalna szerokosc
   */
  def randomConvergentRowMatrixDouble(minWidth: Int = 1, maxWidth: Int = 100, minHeight: Int = 1, maxHeight: Int = 100) = {
    val width = randomInt(minWidth,maxWidth)
    val height = randomInt(minHeight,maxHeight)
    Array.fill(height)(randomConvergentVectorDouble(width,width))
  }
  
  /**
   * Generuje losowy HMM dyskretny, gdzie liczba stanów jest losowana
   * minStates: minimalna liczba stanów
   * maxStates: maksymalna liczba stanów
   */
  def randomHMMDiscrete(minStates: Int, maxStates: Int, observationsSetSize: Int) = {
	  val states = randomInt(minStates, maxStates)
	  val pi = randomConvergentVectorDouble(states,states)
	  val a = randomConvergentRowMatrixDouble(states,states,states,states)
	  val b = randomConvergentRowMatrixDouble(observationsSetSize,observationsSetSize,states,states)
	  new HmmDiscrete(pi,a,b)
  }
  
  /**
   * Generuje losowy wektor wartości typu int gdzie rozmiary wektora są losowane oraz wartość int tez są losowane w okreslonych granicach
   * minWidth: minimalna szerokośc wektora
   * maxObservations: maksymalna szerokośc wektora
   * minValue: minimalna możliwa wartość
   * maxValue: maksymalna możliwa wartość
   */
  def randomVectorIntegers(minWidth: Int, maxWidth: Int, minValue: Int, maxValue: Int) = Array.fill(randomInt(minWidth, maxWidth))(randomInt(minValue, maxValue))
  
  
}