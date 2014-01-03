package sly.speakrecognizer.test.hmm.discrete.algorithm

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import sly.speakrecognizer.test.commons.RandomGenerator
import sly.speakrecognizer.hmm.discrete.algorithm.AlphaDiscreteCalculator
import sly.speakrecognizer.hmm.discrete.domain.ObservationDiscrete
import sly.speakrecognizer.test.commons.MatrixCompare
import sly.speakrecognizer.hmm.discrete.algorithm.BetaDiscreteCalculator
import sly.speakrecognizer.hmm.discrete.algorithm.XiDiscreteCalculator
import sly.speakrecognizer.hmm.discrete.algorithm.GammaDiscreteCalculator
import sly.speakrecognizer.hmm.discrete.algorithm.ViterbiDiscreteAlgorithm
import sly.speakrecognizer.hmm.discrete.domain.SequenceDiscrete
import sly.speakrecognizer.test.commons.Printer
import sly.speakrecognizer.test.hmm.discrete.algorithm.template.DiscreteAlghoritmsTemplateJava
import sly.speakrecognizer.hmm.discrete.domain.HmmDiscrete
import sly.speakrecognizer.hmm.discrete.algorithm.DeltaPsiDiscreteCalculator
import sly.speakrecognizer.hmm.discrete.algorithm.BaumWelchDiscreteAlgorithm

@RunWith(classOf[JUnitRunner])
class DiscreteAlghoritmsVariablesTest extends FunSuite {

  private val minStates = 5
  private val maxStates = 10
  private val minObservationsSetSize = 3
  private val maxObservationsSetSize = 6
  private val minObservations = 5
  private val maxObservations = 10
  private val random = new RandomGenerator
  private val javaAlgorithmPattern = new DiscreteAlghoritmsTemplateJava

  private def printAll(anys: Any*){
    val iter = anys.iterator
    
    while(iter.hasNext){
      val name = iter.next.toString
      val value = iter.next
      value match {
        case array: Array[Int] => Printer.arrayIntToConsole(name,array)
        case matrix: Array[Array[Int]] => Printer.matrixIntToConsole(name,matrix)
        case array: Array[Double] => Printer.arrayDoubleToConsole(name,array)
        case matrix: Array[Array[Double]] => Printer.matrixDoubleToConsole(name,matrix)
        case cube: Array[Array[Array[Double]]] => Printer.cubeDoubleToConsole(name,cube)
        case hmm: HmmDiscrete => printAll("HMM.PI",hmm.pi,"HMM.A",hmm.a,"HMM.B",hmm.b)
        case _ => Printer.anyToConsole(name,value)
      }
      
    }
  }
  
  test("Test generacji zmiennej alpha dla dyskretnego HMM"){
	  
  	  println("Test kalkulatora zmiennej Alpha")
	  //10 krotna iteracja
	  for(x <- 0 until 10){
	    //losowanie liczby mozliwych stanow obserwacji
		val observationsSetSize = random.randomInt(minObservationsSetSize,maxObservationsSetSize)
	    //losowanie HMM
	    val hmm = random.randomHMMDiscrete(minStates, maxStates, observationsSetSize)
	    //losowanie sekwencji obserwacji
	    val seq = random.randomVectorIntegers(minObservations,maxObservations,0,observationsSetSize)
	    //wygenerowanie wzorca
	    val expected = javaAlgorithmPattern.getAlphaOrBeta(false, hmm.pi, hmm.a, hmm.b, seq)
	    //wygenerowanie wyniku
	    val actual = AlphaDiscreteCalculator.calcAlpha(hmm,seq.map(new ObservationDiscrete(_)))
	    //porównanie wyników
	    val compareResult = MatrixCompare.compareMatrixDouble(expected, actual)
	    if(!compareResult){
	      printAll("Hmm",hmm,"Sekwencja",seq,"ExpectedAlpha",expected,"ActualAlpha",actual)
	    }
	    assert(compareResult,"Zmienna alpha niezgodna z wzorcową")
	  }
	}

  test("Test generacji zmiennej beta dla dyskretnego HMM"){
	  
	  println("Test kalkulatora zmiennej beta")
	  //10 krotna iteracja
	  for(x <- 0 until 10){
	    //losowanie liczby mozliwych stanow obserwacji
		val observationsSetSize = random.randomInt(minObservationsSetSize,maxObservationsSetSize)
	    //losowanie HMM
	    val hmm = random.randomHMMDiscrete(minStates, maxStates, observationsSetSize)
	    //losowanie sekwencji obserwacji
	    val seq = random.randomVectorIntegers(minObservations,maxObservations,0,observationsSetSize)
	    //wygenerowanie wzorca
	    val expected = javaAlgorithmPattern.getAlphaOrBeta(true,hmm.pi,hmm.a,hmm.b, seq)
	    //wygenerowanie wyniku
	    val actual = BetaDiscreteCalculator.calcBeta(hmm,seq.map(new ObservationDiscrete(_)))
	    //porównanie wyników
	    val compareResult = MatrixCompare.compareMatrixDelta(expected, actual, 0.0000000000000001)
	    if(!compareResult){
	      printAll("Hmm",hmm,"Sekwencja",seq,"ExpectedBeta",expected,"ActualBeta",actual)
	    }
	    assert(compareResult,"Zmienna beta niezgodna z wzorcową")
	  }
	}
  
  
    test("Test generacji zmiennej XI dla dyskretnego HMM"){
	  
	  println("Test kalkulatora zmiennej XI")
	  //10 krotna iteracja
	  for(x <- 0 until 10){
	    //losowanie liczby mozliwych stanow obserwacji
		val observationsSetSize = random.randomInt(minObservationsSetSize,maxObservationsSetSize)
	    //losowanie HMM
	    val hmm = random.randomHMMDiscrete(minStates, maxStates, observationsSetSize)
	    //losowanie sekwencji obserwacji
	    val seq = random.randomVectorIntegers(minObservations,maxObservations,0,observationsSetSize)
	    //wygenerowanie wzorca
	    val expected = javaAlgorithmPattern.getXi(hmm.pi,hmm.a,hmm.b, seq)
	    //wygenerowanie wyniku
	    val alpha = AlphaDiscreteCalculator.calcAlpha(hmm,seq.map(new ObservationDiscrete(_)))
	    val beta = BetaDiscreteCalculator.calcBeta(hmm,seq.map(new ObservationDiscrete(_)))
	    val actual = XiDiscreteCalculator.calcXi(hmm,seq.map(new ObservationDiscrete(_)),alpha,beta)
	    //porównanie wyników
	    val compareResult = MatrixCompare.compareCubeDelta(expected, actual, 0.000000000000001)
	    if(!compareResult){
	      	 printAll("Hmm",hmm,"Sekwencja",seq,"Alpha",alpha,"Beta",beta,"ExpectedXi",expected,"ActualXi",actual)
	    }
	    assert(compareResult,"Zmienna XI niezgodna z wzorcową")
	  }
	}
    
    test("Test generacji zmiennej Gamma dla dyskretnego HMM"){
	  
	  println("Test kalkulatora zmiennej Gamma")
	  //10 krotna iteracja
	  for(x <- 0 until 10){
	    //losowanie liczby mozliwych stanow obserwacji
		val observationsSetSize = random.randomInt(minObservationsSetSize,maxObservationsSetSize)
	    //losowanie HMM
	    val hmm = random.randomHMMDiscrete(minStates, maxStates, observationsSetSize)
	    //losowanie sekwencji obserwacji
	    val seq = random.randomVectorIntegers(minObservations,maxObservations,0,observationsSetSize)
	    //wygenerowanie wzorca
	    val expected = javaAlgorithmPattern.getGamma(hmm.pi,hmm.a,hmm.b, seq)
	    //wygenerowanie wyniku
	    val alpha = AlphaDiscreteCalculator.calcAlpha(hmm,seq.map(new ObservationDiscrete(_)))
	    val beta = BetaDiscreteCalculator.calcBeta(hmm,seq.map(new ObservationDiscrete(_)))
	    val xi = XiDiscreteCalculator.calcXi(hmm,seq.map(new ObservationDiscrete(_)),alpha,beta);
	    val actualFromXi = GammaDiscreteCalculator.calcGamma(xi)
	    val actualFromAlphaBeta = GammaDiscreteCalculator.calcGamma(alpha,beta)
	    
	    val compareResultFromXi = MatrixCompare.compareMatrixDelta(expected, actualFromXi, 0.000000000000001)
	    val compareResultFromAlphaBeta = MatrixCompare.compareMatrixDelta(expected, actualFromAlphaBeta, 0.000000000000001)
	    if(!compareResultFromXi || !compareResultFromAlphaBeta){
	      	 printAll("Hmm",hmm,"Sekwencja",seq,"Alpha",alpha,"Beta",beta,"XI",xi,"ExpectedGamma",expected,"ActualBetaFromXi",actualFromXi,"ActualBetaFromAlphaBeta",actualFromAlphaBeta)
	    }
	    
	    //porównanie wyników
	    assert(compareResultFromXi,"Zmienna Gamma wyliczona ze zmiennej XI niezgodna z wzorcową")
	    assert(compareResultFromAlphaBeta,"Zmienna Gamma wyliczona ze zmiennych alpha,beta niezgodna z wzorcową")
	  }
	}
    
    test("Test generacji zmiennych delta i psi dla dyskretnego HMM"){
	  
	  println("Test generacji zmiennych delta i psi dla dyskretnego HMM")
	  //10 krotna iteracja
	  for(x <- 0 until 10){
	    //losowanie liczby mozliwych stanow obserwacji
		val observationsSetSize = random.randomInt(minObservationsSetSize,maxObservationsSetSize)
	    //losowanie HMM
	    val hmm = random.randomHMMDiscrete(minStates, maxStates, observationsSetSize)
	    //losowanie sekwencji obserwacji
	    val seq = random.randomVectorIntegers(minObservations,maxObservations,0,observationsSetSize)
	    //wygenerowanie wzorca
	    
	    
		//val expected0 = javaAlgorithmPattern.calcDeltaPsiWithLog(hmm.pi,hmm.a,hmm.b, seq)
		val expected1 = javaAlgorithmPattern.calcDeltaPsiWithoutLog(hmm.pi,hmm.a,hmm.b, seq)
		val expectedDelta = expected1.getFirst
		val expectedPsi = expected1.getSecond
	    
	    //wygenerowanie wyniku
	    val (actualDelta,actualPsi) = DeltaPsiDiscreteCalculator.calcDeltaPsi(hmm,new SequenceDiscrete(seq.map(new ObservationDiscrete(_))))

	    val compareResultDelta = MatrixCompare.compareMatrixDelta(expectedDelta,actualDelta,0.000000000000001)
	    val compareResultPsi = MatrixCompare.compareMatrixInt(expectedPsi,actualPsi)
	    
	    if(!compareResultDelta || !compareResultPsi){
	      	 printAll("Hmm",hmm,"Sekwencja",seq,
	      	     "ExpectedDelta",expectedDelta,"ActualDelta",actualDelta,
	      	     "ExpectedPsi",expectedPsi,"ActualPsi",actualPsi)
	    }
	    //porównanie wyników
	    assert(compareResultDelta,"Zmienne Delta nie zgadzają się")
	    assert(compareResultPsi,"Zmienne Psi nie zgadzają się")
	  }
    }
    
    test("Test generacji zmiennych delta i psi dla dyskretnego HMM dla konkretnych wartości wejściowych"){
	  
	  println("Test generacji zmiennych delta i psi dla dyskretnego HMM dla konkretnych wartości wejściowych")

	  val hmm = new HmmDiscrete(
			  pi = Array(0.2607487882294766,0.07462268038309043,0.12449716764305992,0.17892298074762017,0.14069964567993057,0.06845399362162316,0.1520547436951991),
			  a = Array(
					  Array(0.053517679093736545,0.10123295516321196,0.19421061276221824,0.22053456827810536,0.2355757412367173,0.011160605688387688,0.1837678377776229),
					  Array(0.09922678509715985,0.12861666022819024,0.2706539597058643,0.08582254854284999,0.2195749971094786,0.004895947793585112,0.1912091015228719),
					  Array(0.1959394961134198,0.19711193959842105,0.04819815167862506,0.14582033476779493,0.18912590249672817,0.11352389913940003,0.110280276205611),
					  Array(0.2559413115332792,0.029260643561060486,0.053464388019915245,0.11708764840470386,0.06512922402929705,0.33441358569385077,0.14470319875789334),
					  Array(0.20254467336397142,0.15456503329069288,0.16604065805726279,0.17756339771779298,0.07647776405082453,0.11969682272311229,0.10311165079634305),
					  Array(0.1598255951852819,0.13555515467373855,0.12050110225863252,0.038831378311535665,0.173054851184996,0.1675167483164655,0.20471517006934986),
					  Array(0.22287486350689995,0.07420431506192543,0.15816136783984944,0.16010892934406712,0.20563839073526488,0.0526255665795033,0.1263865669324899)
			  ),
			  b = Array(
					  Array(0.07284107345692237,0.736429688423154,0.19072923811992354),
					  Array(0.18182144182121812,0.44527601394228217,0.3729025442364997),
					  Array(0.41438299790694433,0.21156629152506798,0.37405071056798767),
					  Array(0.449815221923689,0.05748437995579755,0.4927003981205135),
					  Array(0.5429369198888558,0.17013260598851607,0.28693047412262807),
					  Array(0.6009113691398048,0.20308463633266102,0.19600399452753423),
					  Array(0.4454415984826439,0.015770428655873955,0.5387879728614822)
			  )
	  )
	  val seq = Array(2,2,2,0,1)
	  
	  //wygenerowanie wzorca
	  val expected0 = javaAlgorithmPattern.calcDeltaPsiWithLog(hmm.pi,hmm.a,hmm.b, seq)
	  val expected1 = javaAlgorithmPattern.calcDeltaPsiWithoutLog(hmm.pi,hmm.a,hmm.b, seq)
	  val expectedDelta = expected1.getFirst
	  val expectedPsi = expected0.getSecond
	    
	  //wygenerowanie wyniku
	  val (actualDelta,actualPsi) = DeltaPsiDiscreteCalculator.calcDeltaPsi(hmm,new SequenceDiscrete(seq.map(new ObservationDiscrete(_))))

	  val compareResultDelta = MatrixCompare.compareMatrixDelta(expectedDelta,actualDelta,0.000000000000001)
	  val compareResultPsi = MatrixCompare.compareMatrixInt(expectedPsi,actualPsi)
	    
	  if(!compareResultDelta || !compareResultPsi){
	      	 printAll("Hmm",hmm,"Sekwencja",seq,
	      	     "ExpectedDelta",expectedDelta,"ActualDelta",actualDelta,
	      	     "ExpectedPsi",expectedPsi,"ActualPsi",actualPsi)
	  }
	  //porównanie wyników
	  assert(compareResultDelta,"Zmienne Delta nie zgadzają się")
	  assert(compareResultPsi,"Zmienne Psi nie zgadzają się")
    }
    
    test("Test algorytmu Viterbi'ego Dyskretnego HMM dla konkretnych danych"){
      	  println("Test algorytmu Viterbi'ego Dyskretnego HMM dla konkretnych danych")

	  val hmm = new HmmDiscrete(
			  pi = Array(0.2607487882294766,0.07462268038309043,0.12449716764305992,0.17892298074762017,0.14069964567993057,0.06845399362162316,0.1520547436951991),
			  a = Array(
					  Array(0.053517679093736545,0.10123295516321196,0.19421061276221824,0.22053456827810536,0.2355757412367173,0.011160605688387688,0.1837678377776229),
					  Array(0.09922678509715985,0.12861666022819024,0.2706539597058643,0.08582254854284999,0.2195749971094786,0.004895947793585112,0.1912091015228719),
					  Array(0.1959394961134198,0.19711193959842105,0.04819815167862506,0.14582033476779493,0.18912590249672817,0.11352389913940003,0.110280276205611),
					  Array(0.2559413115332792,0.029260643561060486,0.053464388019915245,0.11708764840470386,0.06512922402929705,0.33441358569385077,0.14470319875789334),
					  Array(0.20254467336397142,0.15456503329069288,0.16604065805726279,0.17756339771779298,0.07647776405082453,0.11969682272311229,0.10311165079634305),
					  Array(0.1598255951852819,0.13555515467373855,0.12050110225863252,0.038831378311535665,0.173054851184996,0.1675167483164655,0.20471517006934986),
					  Array(0.22287486350689995,0.07420431506192543,0.15816136783984944,0.16010892934406712,0.20563839073526488,0.0526255665795033,0.1263865669324899)
			  ),
			  b = Array(
					  Array(0.07284107345692237,0.736429688423154,0.19072923811992354),
					  Array(0.18182144182121812,0.44527601394228217,0.3729025442364997),
					  Array(0.41438299790694433,0.21156629152506798,0.37405071056798767),
					  Array(0.449815221923689,0.05748437995579755,0.4927003981205135),
					  Array(0.5429369198888558,0.17013260598851607,0.28693047412262807),
					  Array(0.6009113691398048,0.20308463633266102,0.19600399452753423),
					  Array(0.4454415984826439,0.015770428655873955,0.5387879728614822)
			  )
	  )
	  val seq = Array(2,2,2,0,1)
      //wygenerowanie wzorca
	  val expected = javaAlgorithmPattern.getViterbi1(hmm.pi,hmm.a,hmm.b, seq)
      //wygenerowanie wyniku
      val (actualProbability,actualSequence) = ViterbiDiscreteAlgorithm.calcViterbi(hmm,new SequenceDiscrete(seq.map(new ObservationDiscrete(_))))

      val compareResultProbability = MatrixCompare.compareDoubleDelta(expected.getFirst,actualProbability,0.0000000000001)
      val compareResultSequence = MatrixCompare.compareVectorsInt(expected.getSecond, actualSequence)
      	  
      if(!compareResultProbability || !compareResultSequence){
      	 printAll("Hmm",hmm,"Sekwencja",seq,
      	     "ExpectedProbability",expected.getFirst(),"ActualProbability",actualProbability,
      	     "ExpectedSequence",expected.getSecond(),"ActualSequence",actualSequence)
      }
    //porównanie wyników
    assert(compareResultProbability,"Prawdopodbieństwa w algorytmie się nie zgadzają")
    assert(compareResultSequence,"Sekwencją o najwyzszym prawdopodbienstwie wystapienia się nie zgadza")
    }
    
    test("Test algorytmu Viterbi'ego Dyskretnego HMM"){
	  
	  println("Test Algorytmu Viterbiego Dyskretnego HMM")
	  //10 krotna iteracja
	  for(x <- 0 until 10){
	    //losowanie liczby mozliwych stanow obserwacji
		val observationsSetSize = random.randomInt(minObservationsSetSize,maxObservationsSetSize)
	    //losowanie HMM
	    val hmm = random.randomHMMDiscrete(minStates, maxStates, observationsSetSize)
	    //losowanie sekwencji obserwacji
	    val seq = random.randomVectorIntegers(minObservations,maxObservations,0,observationsSetSize)
	    //wygenerowanie wzorca
	    val expected = javaAlgorithmPattern.getViterbi1(hmm.pi,hmm.a,hmm.b, seq)
	    //wygenerowanie wyniku
	    val (actualProbability,actualSequence) = ViterbiDiscreteAlgorithm.calcViterbi(hmm,new SequenceDiscrete(seq.map(new ObservationDiscrete(_))))

	    val compareResultProbability = MatrixCompare.compareDoubleDelta(expected.getFirst,actualProbability,0.0000000000001)
	    val compareResultSequence = MatrixCompare.compareVectorsInt(expected.getSecond, actualSequence)
	    
	    if(!compareResultProbability || !compareResultSequence){
	      	 printAll("Hmm",hmm,"Sekwencja",seq,
	      	     "ExpectedProbability",expected.getFirst(),"ActualProbability",actualProbability,
	      	     "ExpectedSequence",expected.getSecond(),"ActualSequence",actualSequence)
	    }
	    //porównanie wyników
	    assert(compareResultProbability,"Prawdopodbieństwa w algorytmie się nie zgadzają")
	    assert(compareResultSequence,"Sekwencją o najwyzszym prawdopodbienstwie wystapienia się nie zgadza dla iteracji: " + x)
	  }
	  println("Koniec Testu Algorytmu Viterbiego")

    }
    
    def sequencesToMatrix(sequnces: Seq[SequenceDiscrete]) = sequnces.map(sd => sd.observations.map(_.pos)).toArray
    
    test("Test algorytmu BaumWelch'a Dyskretnego HMM dla konkretnych danych"){
      	  println("Test algorytmu BaumWelch'a Dyskretnego HMM dla konkretnych danych")

	  val inputHmm = new HmmDiscrete(
			  pi = Array(0.2607487882294766,0.07462268038309043,0.12449716764305992,0.17892298074762017,0.14069964567993057,0.06845399362162316,0.1520547436951991),
			  a = Array(
					  Array(0.053517679093736545,0.10123295516321196,0.19421061276221824,0.22053456827810536,0.2355757412367173,0.011160605688387688,0.1837678377776229),
					  Array(0.09922678509715985,0.12861666022819024,0.2706539597058643,0.08582254854284999,0.2195749971094786,0.004895947793585112,0.1912091015228719),
					  Array(0.1959394961134198,0.19711193959842105,0.04819815167862506,0.14582033476779493,0.18912590249672817,0.11352389913940003,0.110280276205611),
					  Array(0.2559413115332792,0.029260643561060486,0.053464388019915245,0.11708764840470386,0.06512922402929705,0.33441358569385077,0.14470319875789334),
					  Array(0.20254467336397142,0.15456503329069288,0.16604065805726279,0.17756339771779298,0.07647776405082453,0.11969682272311229,0.10311165079634305),
					  Array(0.1598255951852819,0.13555515467373855,0.12050110225863252,0.038831378311535665,0.173054851184996,0.1675167483164655,0.20471517006934986),
					  Array(0.22287486350689995,0.07420431506192543,0.15816136783984944,0.16010892934406712,0.20563839073526488,0.0526255665795033,0.1263865669324899)
			  ),
			  b = Array(
					  Array(0.07284107345692237,0.736429688423154,0.19072923811992354),
					  Array(0.18182144182121812,0.44527601394228217,0.3729025442364997),
					  Array(0.41438299790694433,0.21156629152506798,0.37405071056798767),
					  Array(0.449815221923689,0.05748437995579755,0.4927003981205135),
					  Array(0.5429369198888558,0.17013260598851607,0.28693047412262807),
					  Array(0.6009113691398048,0.20308463633266102,0.19600399452753423),
					  Array(0.4454415984826439,0.015770428655873955,0.5387879728614822)
			  )
	  )
      	  
      val sequences = Seq(
          new SequenceDiscrete(Array(ObservationDiscrete(2),ObservationDiscrete(2),ObservationDiscrete(2),ObservationDiscrete(0),ObservationDiscrete(1))),
          new SequenceDiscrete(Array(ObservationDiscrete(2),ObservationDiscrete(2),ObservationDiscrete(1),ObservationDiscrete(0),ObservationDiscrete(1))),
          new SequenceDiscrete(Array(ObservationDiscrete(2),ObservationDiscrete(1),ObservationDiscrete(1),ObservationDiscrete(0),ObservationDiscrete(1)))
      )
      //wygenerowanie wzorca
	  val expectedList = javaAlgorithmPattern.calcBaumWelch(inputHmm.pi, inputHmm.a, inputHmm.b, sequencesToMatrix(sequences))
      val expectedResultHmm = new HmmDiscrete(
          pi = expectedList(0).asInstanceOf[Array[Double]],
          a = expectedList(1).asInstanceOf[Array[Array[Double]]],
          b = expectedList(2).asInstanceOf[Array[Array[Double]]]
      )
	  
      //wygenerowanie wyniku
      val actualResultHmm = BaumWelchDiscreteAlgorithm.calcBaumWelch(inputHmm, sequences)

      val compareResult = MatrixCompare.compareHmmDiscreteDelta(expectedResultHmm,actualResultHmm,0.0000000000001)
      	  
      if(!compareResult){
      	 printAll("inputHmm",inputHmm,"Sekwencje",sequencesToMatrix(sequences),
      	     "ExpectedHmm",expectedResultHmm,"ActualHmm",actualResultHmm)
      }
    //porównanie wyników
    assert(compareResult,"Wyniki algorytmu BaumWelch, modele hmm nie zgadzają się")
    }
    
    /*
    test("Test algorytmu BaumWelch Dyskretnego HMM"){
	  
	  println("Test Algorytmu BaumWelch Dyskretnego HMM")
	  //10 krotna iteracja
	  for(x <- 0 until 10){
	    //losowanie liczby mozliwych stanow obserwacji
		val observationsSetSize = random.randomInt(minObservationsSetSize,maxObservationsSetSize)
	    //losowanie HMM
	    val hmm = random.randomHMMDiscrete(minStates, maxStates, observationsSetSize)
	    //losowanie sekwencji obserwacji
	    val seq = random.randomVectorIntegers(minObservations,maxObservations,0,observationsSetSize)
	    //wygenerowanie wzorca
	    val expected = javaAlgorithmPattern.getViterbi1(hmm.pi,hmm.a,hmm.b, seq)
	    //wygenerowanie wyniku
	    val (actualProbability,actualSequence) = ViterbiDiscreteAlgorithm.calcViterbi(hmm,new SequenceDiscrete(seq.map(new ObservationDiscrete(_))))

	    val compareResultProbability = MatrixCompare.compareDoubleDelta(expected.getFirst,actualProbability,0.0000000000001)
	    val compareResultSequence = MatrixCompare.compareVectorsInt(expected.getSecond, actualSequence)
	    
	    if(!compareResultProbability || !compareResultSequence){
	      	 printAll("Hmm",hmm,"Sekwencja",seq,
	      	     "ExpectedProbability",expected.getFirst(),"ActualProbability",actualProbability,
	      	     "ExpectedSequence",expected.getSecond(),"ActualSequence",actualSequence)
	    }
	    //porównanie wyników
	    assert(compareResultProbability,"Prawdopodbieństwa w algorytmie się nie zgadzają")
	    assert(compareResultSequence,"Sekwencją o najwyzszym prawdopodbienstwie wystapienia się nie zgadza dla iteracji: " + x)
	  }
	  println("Koniec Testu Algorytmu Viterbiego")

    }
    */
    
}