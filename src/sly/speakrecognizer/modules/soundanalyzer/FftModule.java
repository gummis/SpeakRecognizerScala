package sly.speakrecognizer.modules.soundanalyzer;

import java.util.Arrays;

import sly.speakrecognizer.math.fft.FFT;
import sly.speakrecognizer.modules.AbstractModule;
import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.Module;
import sly.speakrecognizer.modules.SoundSamplesData;

public class FftModule extends AbstractModule implements Translator{
	public FFT fft = null;
	int n;
	public FftModule(Module output, Parameters parameters) {
		super(output);
	}

	public void putData(Data data) {
		if(output != null){
			output.putData(translate(data));
		}
	}

	public Data translate(Data data) {
		SoundSamplesData sdata = (SoundSamplesData) data;
		double[] tab = sdata.getData(0);
		if(fft == null){
			fft = new FFT(n = tab.length);
		}else{
			if(n != tab.length){
				throw new IllegalStateException("Szerokość ramki podanej do modułu fft rózny od poprzedniej");
			}
		}
		double[] window = fft.getWindow();
		for(int i = 0 ; i < tab.length ; i++){
			tab[i] *= window[i];
		}

		double[] ctab = new double[tab.length];
		Arrays.fill(ctab, 0.0);
		fft.fft(tab, ctab);

		double[] rtab = new double[tab.length/2];
		for(int i = 0 ; i < tab.length/2 ; i++){
			rtab[i] = Math.pow(tab[i], 2.0) + Math.pow(ctab[i], 2.0);
		}

		return new SoundSamplesData(rtab,sdata.getFreq());
	}
}

