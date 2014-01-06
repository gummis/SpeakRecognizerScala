package sly.speakrecognizer.modules;


public class TransparentModule extends AbstractModule{

	protected TransparentModule(Module output) {
		super(output);
	}

	public void putData(Data data) {
		output.putData(data);
	}
}
