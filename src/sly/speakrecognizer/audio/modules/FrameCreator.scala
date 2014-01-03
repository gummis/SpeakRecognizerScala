package sly.speakrecognizer.audio.modules

import sly.speakrecognizer.audio.Module
import sly.speakrecognizer.audio.Data
import sly.speakrecognizer.audio.SingleData
import scala.collection.mutable.Buffer
import sly.speakrecognizer.audio.MultiData

class FrameCreator(
	/** czestotliwosc probkowania w Hz */
    val freq: Double = 8000.0,
    
	/** szerokość fazy bez wstęg bocznych w ms */
	val phase: Double = 17.0,

	/** szerokość wstęgi bocznej */
	val overlaping: Double = 7.5
    
) extends Module {
	val lengthPhase = (phase * freq / 1000.0).toInt
	val lengthOverlaping = (overlaping * freq / 1000.0).toInt
	val maxSize = locally {
	  var maxSize = lengthPhase + 2 * lengthOverlaping
	  var temp = (Math.log(maxSize) / Math.log(2)).toInt
	  temp = 1 << temp
	  if(temp != maxSize){
		maxSize = temp << 1;
	  }
	  maxSize
	}
	val buffer = Array.ofDim[Double](maxSize)
	var length = 0
  
  override def process(input: Data): Option[Data] = {
		//============================================
		val s = input.asArrayDouble(0)
		var index = 0;
		val res = Buffer[SingleData]()
		while(index != s.length){
			val copy = Math.min(s.length - index, buffer.length - length);
			System.arraycopy(s,index, buffer, length, copy);
			index += copy;
			length += copy;
			if(length == buffer.length){
			  res += new SingleData(buffer.clone)
			  length -= lengthPhase
    		  System.arraycopy(buffer,lengthPhase, buffer, 0, length);
			}
		}
			  
		if(res.isEmpty){
		  None
		}else if(res.size == 1){
		  Some(new SingleData(res.head))
		}else{
		  Some(new MultiData(res.toArray))
		}
  }
}