package sly.speakrecognizer.audio

import sly.speakrecognizer.commons.SRException

class MultiSerialModule(modules: Array[Module]) extends Module {
  override def process(data: Data) = {
    try {
      Some(modules.foldLeft(data)((d,m) => m.process(d).getOrElse(throw new SRException(SRException.NO_DATA_FROM_MODULE))))
    }catch{
      case SRException(SRException.NO_DATA_FROM_MODULE,None) => None
    }
  }
}

class MultiParallelModule(modules: Array[Module]) extends Module {
  override def process(data: Data) = {
    try{
      Some(new MultiData(modules.map(_.process(data).getOrElse(throw new SRException(SRException.NO_DATA_FROM_MODULE)))))
    }catch{
      case SRException(SRException.NO_DATA_FROM_MODULE,None) => None
    }
  }
}
