package sly.speakrecognizer.ui;

import java.util.List;

import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.SoundSamplesData;
import sly.speakrecognizer.modules.soundanalyzer.DctModule;
import sly.speakrecognizer.modules.soundanalyzer.DerivativesProducer;
import sly.speakrecognizer.modules.soundanalyzer.FftModule;
import sly.speakrecognizer.modules.soundanalyzer.FrameCreator;
import sly.speakrecognizer.modules.soundanalyzer.MelFilterBankModule;
import sly.speakrecognizer.modules.soundanalyzer.Preemphasis;
import test.system.modules.soundanalyzer.ModuleForTest;

public class SampleDataUtil {

	public static SampleFrameData[] createSampleFrameData(double[] raw) {
		
		ModuleForTest module = new ModuleForTest();
		//podzielenie na ramki
		FrameCreator frameCreator = new FrameCreator(module,null);
		Preemphasis preemphasis = new Preemphasis(frameCreator,null);
		preemphasis.putData(new SoundSamplesData(raw, 8000));
		List<Data> list = module.clearAndReturn();

		FftModule fft = new FftModule(null, null);
		MelFilterBankModule mfb = new MelFilterBankModule(null,null);
		DctModule dct = new DctModule(null,null);
		DerivativesProducer dp = new DerivativesProducer(null,null);
		int index = 0;
		SampleFrameData[] result = new SampleFrameData[list.size()];
		for(Data data : list){
			double[] sample = ((SoundSamplesData) data).getData(0);
			SampleFrameData sfd = new SampleFrameData();
			sfd.setRaw(sample);
			//================
			SoundSamplesData sdata = (SoundSamplesData) fft.translate(new SoundSamplesData(sample, 8000));
			double[] ddata = sdata.getData(0);
			sfd.setFft(ddata);
			//================
			sdata = (SoundSamplesData) mfb.translate(sdata);
			ddata = sdata.getData(0);
			sfd.setMfb(ddata);
			//================
			sdata = (SoundSamplesData) dct.translate(sdata);
			ddata = sdata.getData(0);
			sfd.setDct(ddata);
			//================
			sdata = (SoundSamplesData) dp.translate(sdata);
			ddata = sdata.getData(0);
			sfd.setDrp(ddata);
			//==============================
			result[index++] = sfd;
		}
		return result;
	}

}
