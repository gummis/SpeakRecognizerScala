package sly.speakrecognizer.audio.modules

import sly.speakrecognizer.audio.Module
import sly.speakrecognizer.audio.Data
import sly.speakrecognizer.audio.SingleData

class Preemphasis(alpha: Double = 0.97) extends Module {
	
  private var sample = 0.0
  
  override def process(input: Data) = Some(new SingleData(input.asArrayDouble(0).map{ v => sample = v - alpha * sample ; sample}))
  
}