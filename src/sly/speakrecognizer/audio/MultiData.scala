package sly.speakrecognizer.audio

import java.util.Arrays

class MultiData(val multiData: Array[Data]) extends Data {
  override def size = multiData.length
  override def iterator = multiData.iterator
  override def asArrayDouble(index: Int) = multiData(index).asArrayDouble(0)
  override def isArrayDouble = false
  override def isMultiData = true
  override def toString = {
    Arrays.toString(multiData.asInstanceOf[Array[Object]])
  }
}