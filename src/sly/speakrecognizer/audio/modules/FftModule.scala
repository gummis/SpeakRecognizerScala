package sly.speakrecognizer.audio.modules

import sly.speakrecognizer.audio.SingleData
import sly.speakrecognizer.audio.Module
import sly.speakrecognizer.audio.Data
import sly.speakrecognizer.math.fft.FFT
import sly.speakrecognizer.audio.MultiData

class FftModule(n: Int = 256) extends Module {
	
  val fft = new FFT(n)
  	
  override def process(input: Data): Option[Data] = {
	  val res = input.iterator.map { inp =>

	    val tab = inp.asArrayDouble(0)
		val window = fft.getWindow()
		for (i <- 0 until tab.length)
			tab(i) *= window(i)

		val ctab = Array.fill(tab.length)(0.0)
		fft.fft(tab, ctab)

		val rtab = Array.ofDim[Double](tab.length/2)
		
		for (i <- 0 until rtab.length)
		  rtab(i) = Math.pow(tab(i), 2.0) + Math.pow(ctab(i), 2.0)
		  
		new SingleData(rtab)
	  } .toArray[Data]
	  Some(new MultiData(res))
  }
  
}