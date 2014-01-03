package sly.speakrecognizer.test.audio

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import sly.speakrecognizer.io.WavReader
import org.scalatest.junit.JUnitRunner
import sly.speakrecognizer.audio.SingleData
import sly.speakrecognizer.audio.modules.AudioSlowModule
import sly.speakrecognizer.audio.modules.Preemphasis
import sly.speakrecognizer.audio.modules.FrameCreator
import sly.speakrecognizer.audio.modules.FftModule
import sly.speakrecognizer.audio.modules.MelFilterBankModule
import sly.speakrecognizer.audio.modules.DctModule
import sly.speakrecognizer.audio.modules.DerivativesProducer
import sly.speakrecognizer.audio.Data
import scala.collection.mutable.Buffer
import sly.speakrecognizer.audio.MultiData
import sly.speakrecognizer.test.commons.RandomGenerator
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization
import org.apache.commons.math3.distribution.MultivariateNormalDistribution
import java.util.Arrays
import sly.speakrecognizer.test.math.MultivariateNormalMixtureExpectationMaximizationFitterTest

@RunWith(classOf[JUnitRunner])
class VowelRecgonizeTest extends FunSuite {

  test("Test zamiany dźwięków na sekwencję obserwacji"){
	  //utworzenie audio analyzera
    
	  val aaaaaaAudioData = new SingleData(WavReader.readAll("samples/aaaaaa.wav"))
	  val eeeeeeAudioData = new SingleData(WavReader.readAll("samples/eeeeee.wav"))
	  val iiiiiiAudioData = new SingleData(WavReader.readAll("samples/iiiiii.wav"))
	  val ooooooAudioData = new SingleData(WavReader.readAll("samples/oooooo.wav"))
	  val uuuuuuAudioData = new SingleData(WavReader.readAll("samples/uuuuuu.wav"))
	  val yyyyyyAudioData = new SingleData(WavReader.readAll("samples/yyyyyy.wav"))
	  
	  //testy preemfazy
	  val aaaaaaPreemphase = new Preemphasis().process(aaaaaaAudioData).get
	  val eeeeeePreemphase = new Preemphasis().process(eeeeeeAudioData).get
	  val iiiiiiPreemphase = new Preemphasis().process(iiiiiiAudioData).get
	  val ooooooPreemphase = new Preemphasis().process(ooooooAudioData).get
	  val uuuuuuPreemphase = new Preemphasis().process(uuuuuuAudioData).get
	  val yyyyyyPreemphase = new Preemphasis().process(yyyyyyAudioData).get
	  
	  //separator ramek
	  val frameCreator = new FrameCreator
	  val aaaaaaFrames = frameCreator.process(aaaaaaPreemphase).get
	  val eeeeeeFrames = frameCreator.process(eeeeeePreemphase).get
	  val iiiiiiFrames = frameCreator.process(iiiiiiPreemphase).get
	  val ooooooFrames = frameCreator.process(ooooooPreemphase).get
	  val uuuuuuFrames = frameCreator.process(uuuuuuPreemphase).get
	  val yyyyyyFrames = frameCreator.process(yyyyyyPreemphase).get
	  
	  val fft = new FftModule
	  val aaaaaaFFT = fft.process(aaaaaaFrames).get
	  val eeeeeeFFT = fft.process(eeeeeeFrames).get
	  val iiiiiiFFT = fft.process(iiiiiiFrames).get
	  val ooooooFFT = fft.process(ooooooFrames).get
	  val uuuuuuFFT = fft.process(uuuuuuFrames).get
	  val yyyyyyFFT = fft.process(yyyyyyFrames).get
	  
	  val mfb = new MelFilterBankModule
	  val aaaaaaMFB = mfb.process(aaaaaaFFT).get
	  val eeeeeeMFB = mfb.process(eeeeeeFFT).get
	  val iiiiiiMFB = mfb.process(iiiiiiFFT).get
	  val ooooooMFB = mfb.process(ooooooFFT).get
	  val uuuuuuMFB = mfb.process(uuuuuuFFT).get
	  val yyyyyyMFB = mfb.process(yyyyyyFFT).get
	  
	  val dct = new DctModule
	  val aaaaaaDCT = dct.process(aaaaaaMFB).get
	  val eeeeeeDCT = dct.process(eeeeeeMFB).get
	  val iiiiiiDCT = dct.process(iiiiiiMFB).get
	  val ooooooDCT = dct.process(ooooooMFB).get
	  val uuuuuuDCT = dct.process(uuuuuuMFB).get
	  val yyyyyyDCT = dct.process(yyyyyyMFB).get
	  
	  val aaaaaaFeaturesAll = new DerivativesProducer().process(aaaaaaDCT).get
	  val eeeeeeFeaturesAll = new DerivativesProducer().process(eeeeeeDCT).get
	  val iiiiiiFeaturesAll = new DerivativesProducer().process(iiiiiiDCT).get
	  val ooooooFeaturesAll = new DerivativesProducer().process(ooooooDCT).get
	  val uuuuuuFeaturesAll = new DerivativesProducer().process(uuuuuuDCT).get
	  val yyyyyyFeaturesAll = new DerivativesProducer().process(yyyyyyDCT).get

	  val dimensinonBeforeCut = aaaaaaFeaturesAll.asArrayDouble(0).length
	  val cutterVectorValues = dimensinonBeforeCut * 2 / 3;
	  val cutterFirstSamples = 4
	  val aaaaaaFeatures = new MultiData(aaaaaaFeaturesAll.iterator.drop(cutterFirstSamples).map(d => new SingleData(d.asArrayDouble(0).dropRight(cutterVectorValues).drop(1))).toArray)
	  val eeeeeeFeatures = new MultiData(eeeeeeFeaturesAll.iterator.drop(cutterFirstSamples).map(d => new SingleData(d.asArrayDouble(0).dropRight(cutterVectorValues).drop(1))).toArray)
	  val iiiiiiFeatures = new MultiData(iiiiiiFeaturesAll.iterator.drop(cutterFirstSamples).map(d => new SingleData(d.asArrayDouble(0).dropRight(cutterVectorValues).drop(1))).toArray)
	  val ooooooFeatures = new MultiData(ooooooFeaturesAll.iterator.drop(cutterFirstSamples).map(d => new SingleData(d.asArrayDouble(0).dropRight(cutterVectorValues).drop(1))).toArray)
	  val uuuuuuFeatures = new MultiData(uuuuuuFeaturesAll.iterator.drop(cutterFirstSamples).map(d => new SingleData(d.asArrayDouble(0).dropRight(cutterVectorValues).drop(1))).toArray)
	  val yyyyyyFeatures = new MultiData(yyyyyyFeaturesAll.iterator.drop(cutterFirstSamples).map(d => new SingleData(d.asArrayDouble(0).dropRight(cutterVectorValues).drop(1))).toArray)
	  
	  val features = Array(
		  filter(3,aaaaaaFeatures),
		  filter(3,eeeeeeFeatures),
		  filter(3,iiiiiiFeatures),
		  filter(3,ooooooFeatures),
		  filter(3,uuuuuuFeatures),
		  filter(3,yyyyyyFeatures)
      )
      
      val names = Array("AAAAAA","EEEEEE","IIIIII","OOOOOO","UUUUUU","YYYYYY")
	  val dimension = aaaaaaFeatures.asArrayDouble(0).length
      var index = 1
      features.foreach{ d =>
        println("Dla samogłoski " + names(index-1))
        index += 1
        val min = Array.fill(dimension)(Double.MaxValue)
        val med = Array.fill(dimension)(0.0)
        val max = Array.fill(dimension)(Double.MinValue)
        var counter = 0
        d.iterator.foreach{ f =>
          val features = f.asArrayDouble(0)
	      //println(features.deep.toString)

		      for(i <- 0 until dimension){
		        if(min(i) > features(i)){
		          min(i) = features(i)
		        }
		        if(max(i) < features(i)){
		          max(i) = features(i)
		        }
		        med(i) += features(i)
		      }
	      counter += 1
	    }
        for(i <- 0 until dimension){
	        med(i) /= counter
	    }
        println("wartości minimalne " + min.deep.toString)
        println("wartości średnie " + med.deep.toString)
        println("wartości maksymalne " + max.deep.toString)
	  }
      
      //===========================
	  //rozlosowanie na sekwencje uczące i rozpoznawane
	  val (aaaRec,aaaLearn) = random(0.75,features(0))
	  val (eeeRec,eeeLearn) = random(0.75,features(1))
	  val (iiiRec,iiiLearn) = random(0.75,features(2))
	  val (oooRec,oooLearn) = random(0.75,features(3))
	  val (uuuRec,uuuLearn) = random(0.75,features(4))
	  val (yyyRec,yyyLearn) = random(0.75,features(5))
      //===========================
	  //tablice z danymi uczacymi
	  val aaaLearnArray = aaaLearn.map(d => d.asArrayDouble(0)).toArray
	  val eeeLearnArray = eeeLearn.map(d => d.asArrayDouble(0)).toArray
	  val iiiLearnArray = iiiLearn.map(d => d.asArrayDouble(0)).toArray
	  val oooLearnArray = oooLearn.map(d => d.asArrayDouble(0)).toArray
	  val uuuLearnArray = uuuLearn.map(d => d.asArrayDouble(0)).toArray
	  val yyyLearnArray = yyyLearn.map(d => d.asArrayDouble(0)).toArray
	  
	  val aaaLearnArrayC = Array
	  
      //===========================
	  //rozklady prawdopodbienstw
	  
	  val aaaEM = learn(aaaLearnArray)
	  val eeeEM = learn(eeeLearnArray)
	  val iiiEM = learn(iiiLearnArray)
	  val oooEM = learn(oooLearnArray)
	  val uuuEM = learn(uuuLearnArray)
	  val yyyEM = learn(yyyLearnArray)
	  
	  val aaaSEM = learnSEM(aaaLearnArray)
	  val eeeSEM = learnSEM(eeeLearnArray)
	  val iiiSEM = learnSEM(iiiLearnArray)
	  val oooSEM = learnSEM(oooLearnArray)
	  val uuuSEM = learnSEM(uuuLearnArray)
	  val yyySEM = learnSEM(yyyLearnArray)
	  

	  val recs = List(
	      ("aaaaaa",aaaRec),
	      ("eeeeee",eeeRec),
	      ("iiiiii",iiiRec),
	      ("oooooo",oooRec),
	      ("uuuuuu",uuuRec),
	      ("yyyyyy",yyyRec)
	  )
	  
	  val distributions = List(
	      ("aaaaaa",aaaEM),
	      ("eeeeee",eeeEM),
	      ("iiiiii",iiiEM),
	      ("oooooo",oooEM),
	      ("uuuuuu",uuuEM),
	      ("yyyyyy",yyyEM)
	  )
	  
	  val distributionsSEM = List(
	      ("aaaaaa",aaaSEM),
	      ("eeeeee",eeeSEM),
	      ("iiiiii",iiiSEM),
	      ("oooooo",oooSEM),
	      ("uuuuuu",uuuSEM),
	      ("yyyyyy",yyySEM)
	  )

	  distributions.foreach{p =>
	    val name = p._1
	    val em = p._2
	    println ("Dla "+name)
	    MultivariateNormalMixtureExpectationMaximizationFitterTest.printMMND(em)
	  }
	  
	  
	  //===================================
	  //rozpoznawanie za pomoca modelu mieszanego MixtureMultivariateNormalDistribution
	  println("rozpoznawanie za pomoca modelu mieszanego MixtureMultivariateNormalDistribution" )

	  var minLikehood = Double.MaxValue
	  var maxLikehood = Double.MinValue
	  var minLikehoodVowel = "Brak"
	  var maxLikehoodVowel = "Brak"
	  
	  //rozpoznawniae obserwacji przeznaczonych do rozpoznania
	  recs.foreach{p =>
	    val recCode = p._1
	    p._2.foreach{ data =>
	      val vector = data.asArrayDouble(0)
	      //==============================
	      //rozpoznanie wektora
	      val emMax = distributions.maxBy(em => em._2.density(vector))
	      println(s"obserwacja typu ${recCode} została rozpoznana jako ${emMax._1}" )
	      //==============================
	      //prawdopodbienstwa wektora
	      distributions.foreach{em => 
	        val likehood = em._2.density(vector)
	        
	        if(likehood != 0.0 && minLikehood > likehood){
	          minLikehood = likehood
	          minLikehoodVowel = em._1
	        }
	          
	        if(likehood != 0.0 && maxLikehood < likehood){
	          maxLikehood = likehood
	          maxLikehoodVowel = em._1
	        }

	        println(s"prawdopodbienstwo wektora typu ${recCode} dla wzorca ${em._1} wynosi ${likehood}" )
	      }
	    }
	  }
	  
	  println(s"Minimalne prawdopodbieństwo dla ${minLikehoodVowel} wynosi ${minLikehood}")
	  println(s"Minimalne prawdopodbieństwo dla ${maxLikehoodVowel} wynosi ${maxLikehood}")
	  
	  val aaaaaaLikehood0 = distributions(0)._2.density(distributions(0)._2.getComponents().get(0).getSecond().getMeans())
	  val eeeeeeLikehood0 = distributions(1)._2.density(distributions(1)._2.getComponents().get(0).getSecond().getMeans())
	  val iiiiiiLikehood0 = distributions(2)._2.density(distributions(2)._2.getComponents().get(0).getSecond().getMeans())
	  val ooooooLikehood0 = distributions(3)._2.density(distributions(3)._2.getComponents().get(0).getSecond().getMeans())
	  val uuuuuuLikehood0 = distributions(4)._2.density(distributions(4)._2.getComponents().get(0).getSecond().getMeans())
	  val yyyyyyLikehood0 = distributions(5)._2.density(distributions(5)._2.getComponents().get(0).getSecond().getMeans())
	  
	  println("aaaaaaLikehood0: " + aaaaaaLikehood0)
	  println("eeeeeeLikehood0: " + eeeeeeLikehood0)
	  println("iiiiiiLikehood0: " + iiiiiiLikehood0)
	  println("ooooooLikehood0: " + ooooooLikehood0)
	  println("uuuuuuLikehood0: " + uuuuuuLikehood0)
	  println("yyyyyyLikehood0: " + yyyyyyLikehood0)

	  val aaaaaaLikehood1 = distributions(0)._2.density(distributions(0)._2.getComponents().get(1).getSecond().getMeans())
	  val eeeeeeLikehood1 = distributions(1)._2.density(distributions(1)._2.getComponents().get(1).getSecond().getMeans())
	  val iiiiiiLikehood1 = distributions(2)._2.density(distributions(2)._2.getComponents().get(1).getSecond().getMeans())
	  val ooooooLikehood1 = distributions(3)._2.density(distributions(3)._2.getComponents().get(1).getSecond().getMeans())
	  val uuuuuuLikehood1 = distributions(4)._2.density(distributions(4)._2.getComponents().get(1).getSecond().getMeans())
	  val yyyyyyLikehood1 = distributions(5)._2.density(distributions(5)._2.getComponents().get(1).getSecond().getMeans())
	  
	  println("aaaaaaLikehood1: " + aaaaaaLikehood1)
	  println("eeeeeeLikehood1: " + eeeeeeLikehood1)
	  println("iiiiiiLikehood1: " + iiiiiiLikehood1)
	  println("ooooooLikehood1: " + ooooooLikehood1)
	  println("uuuuuuLikehood1: " + uuuuuuLikehood1)
	  println("yyyyyyLikehood1: " + yyyyyyLikehood1)
	  
	  //===================================
	  //rozpoznawanie za pomoca modelu niemieszanego MultivariateNormalDistibution
	  println("rozpoznawanie za pomoca modelu niemieszanego MultivariateNormalDistibution" )
	  
	  var minLikehoodMND = Double.MaxValue
	  var maxLikehoodMND = Double.MinValue
	  var minLikehoodVowelMND = "Brak"
	  var maxLikehoodVowelMND = "Brak"
	  
	  //rozpoznawniae obserwacji przeznaczonych do rozpoznania
	  recs.foreach{p =>
	    val recCode = p._1
	    p._2.foreach{ data =>
	      val vector = data.asArrayDouble(0)
	      //==============================
	      //rozpoznanie wektora
	      val emMax = distributionsSEM.maxBy(em => em._2.density(vector))
	      println(s"obserwacja typu ${recCode} została rozpoznana jako ${emMax._1}" )
	      //==============================
	      //prawdopodbienstwa wektora
	      distributionsSEM.foreach{em => 
	        val likehood = em._2.density(vector)
	        
	        if(likehood != 0.0 && minLikehoodMND > likehood){
	          minLikehoodMND = likehood
	          minLikehoodVowelMND = em._1
	        }
	          
	        if(likehood != 0.0 && maxLikehoodMND < likehood){
	          maxLikehoodMND= likehood
	          maxLikehoodVowelMND = em._1
	        }

	        println(s"prawdopodbienstwo wektora typu ${recCode} dla wzorca ${em._1} wynosi ${likehood}" )
	      }
	    }
	  }
	  
	  println(s"Minimalne prawdopodbieństwo dla ${minLikehoodVowelMND} wynosi ${minLikehoodMND}")
	  println(s"Minimalne prawdopodbieństwo dla ${maxLikehoodVowelMND} wynosi ${maxLikehoodMND}")
	  
	  val aaaaaaLikehoodMND = distributionsSEM(0)._2.density(distributionsSEM(0)._2.getMeans)
	  val eeeeeeLikehoodMND = distributionsSEM(1)._2.density(distributionsSEM(1)._2.getMeans)
	  val iiiiiiLikehoodMND = distributionsSEM(2)._2.density(distributionsSEM(2)._2.getMeans)
	  val ooooooLikehoodMND = distributionsSEM(3)._2.density(distributionsSEM(3)._2.getMeans)
	  val uuuuuuLikehoodMND = distributionsSEM(4)._2.density(distributionsSEM(4)._2.getMeans)
	  val yyyyyyLikehoodMND = distributionsSEM(5)._2.density(distributionsSEM(5)._2.getMeans)
	  
	  println("aaaaaaLikehoodMND: " + aaaaaaLikehoodMND)
	  println("eeeeeeLikehoodMND: " + eeeeeeLikehoodMND)
	  println("iiiiiiLikehoodMND: " + iiiiiiLikehoodMND)
	  println("ooooooLikehoodMND: " + ooooooLikehoodMND)
	  println("uuuuuuLikehoodMND: " + uuuuuuLikehoodMND)
	  println("yyyyyyLikehoodMND: " + yyyyyyLikehoodMND)

  }
  
  def learn(data : Array[Array[Double]]) = {
        val fitter = new MultivariateNormalMixtureExpectationMaximization(data)
        val initialMix = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2)
        fitter.fit(initialMix)
        println("Learned LogLikelihood = " + fitter. getLogLikelihood)
        fitter.getFittedModel
  }

  def learnSEM(data : Array[Array[Double]]) = {
	  MaximumLikehoodFitter.fit(data)
  }
  
  def filter(length : Int, data: Data) : Data = {
    var ind = 0
    val res = data.iterator.filter{f => ind += 1;ind > length}.toArray[Data]
    new MultiData(res)
  }
  
  def random(ratio: Double, data: Data) = {
    val rand = new RandomGenerator
    val rec = data.iterator.toBuffer
    val learn = Buffer[Data]()
    val denominator = rec.length.toDouble
    while(learn.size.toDouble / denominator < ratio){
      val ind = rand.randomInt(max = rec.length)
      learn += rec.remove(ind)
    }
    (rec.toArray,learn.toArray)  
  }
}