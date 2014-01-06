package sly.speakrecognizer.modules.soundanalyzer;

import sly.speakrecognizer.audio.modules.MelFilterBank;
import sly.speakrecognizer.modules.AbstractModule;
import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.Module;
import sly.speakrecognizer.modules.SoundSamplesData;

public class MelFilterBankModule extends AbstractModule implements Translator {
	private MelFilterBank mfb = null;
	private static final double[] freqIndexes = new double[]{8000,11025,16000,22050,32000,44100};
	private static final int[] filtersLength = new int[]{20,28,32,34,35,36};
	
	public MelFilterBankModule(Module output, Parameters parameters) {
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
		double freq  = sdata.getFreq();
		if(mfb == null){
			int ind = -1;
			for(int i = 0 ; i < freqIndexes.length ; i++)
				if(freqIndexes[i] == freq){
					ind = i;
					break;
				}
			int N = ind >= 0 ? filtersLength[ind] : 36;
			mfb = new MelFilterBank(N,tab.length,freq/2,60,freq/2);
		}else{
			if(tab.length != mfb.getLength() || freq != mfb.getFreq()*2){
				throw new IllegalStateException("Bank filtrów Mel dostał ramkę której długość lub częstotliwość nie zgodna z poprzednią.");
			}
		}
		return new SoundSamplesData(mfb.filter(tab),freq);
	}


}
