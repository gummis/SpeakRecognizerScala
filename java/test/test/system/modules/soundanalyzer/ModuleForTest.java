package test.system.modules.soundanalyzer;

import java.util.ArrayList;
import java.util.List;

import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.Module;

public class ModuleForTest implements Module {
	List<Data> list = new ArrayList<Data>();

	public Module getOutput() {
		return null;
	}

	public void putData(Data data) {
		list.add(data);
	}

	public List<Data> clearAndReturn() {
		List<Data> _list_ = list;
		list = new ArrayList<Data>();
		return _list_;
	}
}