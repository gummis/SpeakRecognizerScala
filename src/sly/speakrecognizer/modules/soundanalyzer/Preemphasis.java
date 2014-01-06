package sly.speakrecognizer.modules.soundanalyzer;

import sly.speakrecognizer.modules.AbstractModule;
import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.Module;
import sly.speakrecognizer.modules.SoundSamplesData;

public class Preemphasis extends AbstractModule{
	double sample = 0.0;
	double alpha = 0.97;
	public Preemphasis(Module output, Parameters parameters) {
		super(output);
	}

	public void putData(Data data) {
		SoundSamplesData sdata = (SoundSamplesData) data;
		double[] tab = sdata.getData(0);

	    for (int i=0; i < tab.length; i++) {
	    	sample = tab[i] -= alpha * sample;
	    }		
	    if(output != null){
	    	output.putData(sdata);
	    }
	}
}
