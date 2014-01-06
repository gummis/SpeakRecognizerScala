package sly.speakrecognizer.modules;

import sly.speakrecognizer.io.SoundSource;

public class FlowController {
	private SoundSource source;
	private Module output;
	
	public FlowController(SoundSource source, Module output) {
		this.source = source;
		this.output = output;
	}
	
	public void resume(){
		while(!source.isEnd()){
			double[][] samples = source.getSamples();
			for(int y =0 ; y< samples.length;y++)
				for(int x =0 ; x< samples[y].length;x++)
					samples[y][x] = 0.0;
			output.putData(new SoundSamplesData(samples,source.getFreq()));
		}
	}

}
