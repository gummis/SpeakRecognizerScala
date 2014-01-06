package sly.speakrecognizer.modules;


public abstract class AbstractModule implements Module{
	protected Module output;
	protected AbstractModule(Module output){
		this.output = output;
	}
	public final Module getOutput() {
		return output;
	}
	
}
