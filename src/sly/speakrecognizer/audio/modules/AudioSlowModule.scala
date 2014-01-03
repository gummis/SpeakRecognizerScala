package sly.speakrecognizer.audio.modules

import sly.speakrecognizer.audio.MultiSerialModule
import sly.speakrecognizer.audio.modules._


class AudioSlowModule extends MultiSerialModule(Array(
		new Preemphasis
		,new FrameCreator
		,new FftModule
		,new MelFilterBankModule()
		,new DctModule()
		,new DerivativesProducer()
	)
)
