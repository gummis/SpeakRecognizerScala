package sly.speakrecognizer.audio.modules

import sly.speakrecognizer.audio.SingleData
import sly.speakrecognizer.audio.Module
import sly.speakrecognizer.audio.Data
import sly.speakrecognizer.audio.MultiData

class MelFilterBankModule(filtersLength: Int = 20, sampleLength: Int = 128, freq:Double = 8000.0, minFreq:Double = 60.0) extends Module {
	
  val mfb = new MelFilterBank(filtersLength,sampleLength,freq/2,minFreq,freq/2)
  
  override def process(input: Data): Option[Data] = {
    val res = input.iterator.map(d => new SingleData(mfb.filter(d.asArrayDouble(0)))).toArray[Data]
    Some(new MultiData(res))
  }
}