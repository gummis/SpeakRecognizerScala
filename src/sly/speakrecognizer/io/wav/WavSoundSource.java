package sly.speakrecognizer.io.wav;

import java.io.IOException;

import sly.speakrecognizer.io.SoundSource;
import sly.speakrecognizer.io.wav.WavFile;

public class WavSoundSource implements SoundSource{
	private WavFile file;
	public WavSoundSource(WavFile wavFile) {
		file = wavFile;
	}
	
	public double getFreq() {
		return file.getSampleRate();
	}

	public double[][] getSamples() {
		long size = file.getFramesRemaining();
		if(size > 0x1000){
			size = 0x1000;
		}
		double[][] buf = new double[file.getNumChannels()][(int)size];
		try{
			if(size != file.readFrames(buf, (int)size)){
				throw new IllegalStateException("Ilo�� rz�danych pr�bek i ilo�� zwr�conych niezgodna przez obiekt WavFile");
			}
		}catch (Exception e){
			try {
				file.close();
			} catch (IOException e1) {
			}
			file = null;
			return null;
		}
		return buf;
	}

	public boolean isEnd() {
		return file == null || file.getFramesRemaining() <= 0;
	}
}
