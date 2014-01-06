package sly.speakrecognizer.modules.soundanalyzer;

import java.util.Arrays;

import sly.speakrecognizer.SERuntimeException;
import sly.speakrecognizer.modules.AbstractModule;
import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.Module;
import sly.speakrecognizer.modules.SoundSamplesData;

public class FrameCreator extends AbstractModule {
	/**
	 * szerokość fazy bez wstęg bocznych w ms
	 */
	private double phase = 17.0;

	/**
	 * szerokość wstęgi bocznej
	 */
	private double overlaping = 7.5;
	
	/**
	 * bufor na próbki
	 */
	private double[] buffer;
	
	/**
	 * ile danych w buforze
	 */
	private int length = 0;
	
	/**
	 * częstotliwość próbkowania w Hz
	 */
	double freq = 0.0;
	
	/**
	 * szerokość fazy w ilości próbek 
	 */
	int lengthPhase = 0;
	
	public FrameCreator(Module output, Parameters parameters) {
		super(output);
	}
	public void putData(Data data) {
		if(data == null){
			throw new SERuntimeException("Obiekt FrameCreator otrzymał dane równe null");
		}
		if(!(data instanceof SoundSamplesData)){
			throw new SERuntimeException("Obiekt FrameCreator otrzymał nieprawidłowy rodzaj danych: " + data.getClass().getSimpleName());
		}
		if(output == null){
			return;
		}
		//===========================================
		//processing
		SoundSamplesData sdata = (SoundSamplesData) data;
		if(buffer == null){
			freq = sdata.getFreq();
			lengthPhase = (int) (phase * freq / 1000.0);
			int lengthOverlaping = (int) (overlaping * freq / 1000.0);
			int maxSize = lengthPhase + 2 * lengthOverlaping;
			//poniewaz liczba próbek musi być potęgą liczby 2 to zwięszkamy maxSize to takiej wartości
		    int temp = (int)(Math.log(maxSize) / Math.log(2));
		    temp = 1 << temp;
		    if(temp != maxSize){
		    	maxSize = temp << 1;
		    }
			buffer = new double[maxSize];
		}else{
			if(freq != sdata.getFreq()){
				throw new SERuntimeException("W czasie przetwarzania obiekt FrameCreator wykrył zmianę częstotliwości próbkowania");
			}
		}
		//============================================
		double[] s = sdata.getData(0);
		int index = 0;
		while(index != s.length){
			int copy = Math.min(s.length - index, buffer.length - length);
			System.arraycopy(s,index, buffer, length, copy);
			index += copy;
			length += copy;
			if(length == buffer.length){
				output.putData(new SoundSamplesData(Arrays.copyOf(buffer, buffer.length),freq));
				System.arraycopy(buffer,lengthPhase, buffer, 0, length -= lengthPhase);
			}
		}
	}
}
