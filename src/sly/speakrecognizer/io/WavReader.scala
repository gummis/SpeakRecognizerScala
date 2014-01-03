package sly.speakrecognizer.io

import java.io.File

object WavReader {
	def readAll(path: String) = {
	  	val wavFile = WavFile.openWavFile(new File(path))
	  	require(wavFile.getNumChannels == 1,s"Plik $path musi być mono")
	  	require(wavFile.getSampleRate == 8000,s"Plik $path musi mieć częstotliowśc próbkowania 8kHz")
	  	require(wavFile.getValidBits == 16,s"Plik $path musi mieć bitowość próbki równą 16")
	  	val length = wavFile.getFramesRemaining.toInt
	  	val sampleBuffer = Array.ofDim[Int](length)
	  	wavFile.readFrames(sampleBuffer, length)
	  	wavFile.close
		sampleBuffer.map(_.toDouble / 32768.0)
	}
}