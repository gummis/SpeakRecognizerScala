package sly.speakrecognizer.audio.modules

import sly.speakrecognizer.audio.SingleData
import sly.speakrecognizer.audio.Module
import sly.speakrecognizer.audio.Data
import sly.speakrecognizer.audio.MultiData

class DctModule(inputValuesLength: Int = 20, coefficentsLength :Int = 13) extends Module {
	
  val sqrt = Math.sqrt(inputValuesLength)
  val dct = new DCT(inputValuesLength,coefficentsLength - 1);
  	
  override def process(input: Data): Option[Data] = {
    val res = input.iterator.map(d => new SingleData({
        val resAr = Array.ofDim[Double](coefficentsLength);
        val tab = d.asArrayDouble(0)
        dct.transform(tab,resAr,1)
        var sum = 0.0
        tab.foreach{d =>
          sum += d
        }
        resAr(0) = sum / sqrt
        resAr
    })).toArray[Data]
    Some(new MultiData(res))
  }
  
}