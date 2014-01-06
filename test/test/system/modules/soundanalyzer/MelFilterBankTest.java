package test.system.modules.soundanalyzer;

import java.util.Arrays;

import org.junit.Test;

import sly.speakrecognizer.audio.modules.MelFilterBank;
import sly.speakrecognizer.math.fft.FFT;
import sly.speakrecognizer.modules.SoundSamplesData;

public class MelFilterBankTest {
	
	@Test
	public void test(){
		//utworzenie filtru
		int sampleRate = 8000;
		int minFreq = 60;
		int maxFreq = 8000;
		int countFilters = 40;
		int sizeFrame = 512;
		//========================================
		MelFilterBank mfb = new MelFilterBank(countFilters,sizeFrame/2,sampleRate,minFreq,maxFreq);
		
		//utworzenie próbki
		double[] sample = new double[sizeFrame];
		for(int x = 0; x < sample.length ; x++){
			sample[x] = Math.sin(x);
		}
		
		FFT fft = new FFT(sizeFrame);

		double[] window = fft.getWindow();
		for(int i = 0 ; i < sample.length ; i++){
			sample[i] *= window[i];
		}

		double[] ctab = new double[sample.length];
		Arrays.fill(ctab, 0.0);
		fft.fft(sample, ctab);

		double[] rtab = new double[sample.length/2];
		for(int i = 0 ; i < sample.length/2 ; i++){
			rtab[i] = Math.pow(sample[i], 2.0) + Math.pow(ctab[i], 2.0);
		}
		
		double[] filterResult = mfb.filter(rtab);
		
		System.out.println(Arrays.toString(filterResult));
	}
}
