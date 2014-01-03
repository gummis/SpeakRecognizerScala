package sly.speakrecognizer.audio.modules

import sly.speakrecognizer.audio.SingleData
import sly.speakrecognizer.audio.Module
import sly.speakrecognizer.audio.Data
import sly.speakrecognizer.audio.MultiData

class DerivativesProducer(length: Int = 13) extends Module {

  val bufCepstrum = Array.fill(7, length)(0.0)
  val bufFirstDeriv = Array.fill(7, length)(0.0)
  var position = 0

  override def process(input: Data): Option[Data] = {
    val res = input.iterator.map(d => new SingleData(withDerivatives(d.asArrayDouble(0)))).toArray[Data]
    Some(new MultiData(res))
  }

  private def withDerivatives(tab: Array[Double]): Array[Double] = {
    require(tab.length == length, "Nieprawidłowa ilość cech")
    //zwiekszenie pozycji
    position += 1
    position %= 7
    //wyliczenie pozycji sąsiednich
    //val posM3 = (position-3+7)%7
    //val posM2 = (position-2+7)%7
    val posM1 = (position - 1 + 7) % 7
    val posP1 = (position + 1) % 7
    //val posP2 = (position+2)%7
    val posP3 = (position + 3) % 7
    //wrzucenie do bufora kolejnej próbki na koniec jako p3
    bufCepstrum(posP3) = tab
    //wyliczenie pierszej pochodnej dla p1=p3-m1
    val t1a = bufCepstrum(posM1)
    val t2a = bufFirstDeriv(posP1)
    for (x <- 0 until length) {
      t2a(x) = tab(x) - t1a(x)
    }
    //ramka wyjściowa: zmienne , pochodna, druga pochodna
    val result = Array.ofDim[Double](length * 3)

    //kopiowanie danych biezących
    System.arraycopy(bufCepstrum(position), 0, result, 0, length)
    //kopiowanie pierwszej pochodnej
    System.arraycopy(bufFirstDeriv(position), 0, result, length, length)
    //obliczenie drugiej pochodnej
    val t1b = bufFirstDeriv(posM1)
    val t2b = bufFirstDeriv(posP1)
    var index = length * 2
    for (x <- 0 until length) {
      result(index) = t2b(x) - t1b(x)
      index += 1
    }
    result
  }
}