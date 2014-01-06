package sly.speakrecognizer.io;


public interface SoundSource {

	public double getFreq();
	public double[][] getSamples();
	public boolean isEnd();

}
