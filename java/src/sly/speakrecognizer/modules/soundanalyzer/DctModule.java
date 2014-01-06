package sly.speakrecognizer.modules.soundanalyzer;

import sly.speakrecognizer.math.functions.DCT;
import sly.speakrecognizer.modules.AbstractModule;
import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.Module;
import sly.speakrecognizer.modules.SoundSamplesData;

public class DctModule extends AbstractModule implements Translator{
	private DCT dct = null;
	private int sizeCoef;
	private double sqrt;
	
	public DctModule(Module output, Parameters parameters) {
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
		if(dct == null){
			sizeCoef = 12;
			dct = new DCT(tab.length,sizeCoef);
			sizeCoef += 1;
			sqrt = Math.sqrt(tab.length);
		}else{
			if(dct.getLengthFilters() != tab.length){
				throw new IllegalStateException("Ilość filtrów cech Mel niezgodna z poprzednim ustawieniem");
			}
		}

		double[] res = new double[sizeCoef];
		//DCT
		dct.transform(tab,res,1);
		//Signal Energy
		double sum = 0.0;
		for(double v : tab){
			sum += v;
		}
		res[0] = sum / sqrt;
		return new SoundSamplesData(res,sdata.getFreq());
	}
	
}
