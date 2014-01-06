package sly.speakrecognizer.modules.soundanalyzer;

import sly.speakrecognizer.SERuntimeException;
import sly.speakrecognizer.modules.AbstractModule;
import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.Module;

public class SoundAnalyzer extends AbstractModule{
	public SoundAnalyzer(Module output, Parameters parameters) {
		super(create(output,parameters));
	}

	private static Module create(Module output,Parameters parameters) {
		DerivativesProducer dp = new DerivativesProducer(output,parameters);
		DctModule dct = new DctModule(dp,parameters);
		MelFilterBankModule mfbm = new MelFilterBankModule(dct,parameters);
		FftModule fft = new FftModule(mfbm,parameters);
		Preemphasis preemphasis = new Preemphasis(fft,parameters);
		FrameCreator frameCreator = new FrameCreator(preemphasis,parameters);
		Resampler resampler = new Resampler(frameCreator,parameters);
		return new Mixer(resampler,parameters);
	}
	
	public void putData(Data data) {
		if(data == null){
			throw new SERuntimeException("Obiekt SoundAnalyzer otrzymał dane równe null");
		}
		output.putData(data);
	}
}
