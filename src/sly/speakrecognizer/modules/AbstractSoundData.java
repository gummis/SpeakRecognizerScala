package sly.speakrecognizer.modules;

public abstract class AbstractSoundData implements SoundData{
	private double freq;
	public AbstractSoundData(double freq){
		this.freq = freq;
	}
	public double getFreq(){
		return freq;
	}
	public void setFreq(double freq){
		this.freq = freq;
	}
}
