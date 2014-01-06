package sly.speakrecognizer.modules.soundanalyzer;

import sly.speakrecognizer.SERuntimeException;
import sly.speakrecognizer.modules.AbstractModule;
import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.Module;
import sly.speakrecognizer.modules.SoundSamplesData;

public class Mixer extends AbstractModule{

	protected Mixer(Module output, Parameters parameters) {
		super(output);
	}

	public void putData(Data data) {
		if(data == null){
			throw new SERuntimeException("Obiekt Mixer otrzyma� dane r�wne null");
		}
		if(!(data instanceof SoundSamplesData)){
			throw new SERuntimeException("Obiekt Mixer otrzyma� nieprawid�owy rodzaj danych: " + data.getClass().getSimpleName());
		}
		if(output == null){
			return;
		}
		//===========================================
		//przetwarzanie
		SoundSamplesData sdata = (SoundSamplesData) data;
		if(sdata.getLengthChannels() == 1){
			output.putData(sdata);
			return;
		}
		SoundSamplesData sdata1 = new SoundSamplesData(sdata.getData(0),sdata.getFreq());
		sdata1.setFreq(sdata.getFreq());
		output.putData(sdata1);
	}
}
