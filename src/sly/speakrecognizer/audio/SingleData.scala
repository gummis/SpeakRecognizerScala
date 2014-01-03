package sly.speakrecognizer.audio

import java.util.Arrays

class SingleData(val any: Any) extends Data{
  override def size = 1
  override def iterator = Iterator.single[Data](this)
  override def asArrayDouble(index: Int) = { require(index == 0 ) ; any.asInstanceOf[Array[Double]] }
  override def isArrayDouble = any.isInstanceOf[Array[Double]]
  override def isMultiData = false
  override def toString = {
    "SingleData(" +
    (any match {
      case arrayDouble : Array[Double] => Arrays.toString(arrayDouble).replace("[","").replace("]","")
    })+
    ")"
  }
}