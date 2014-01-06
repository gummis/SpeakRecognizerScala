package sly.speakrecognizer.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ListNamesWordsComboModel implements ComboBoxModel<String>,RecognizeModelListener {
	private RecognizeModel model;
	private List<String> list;
	private int index = -1;
	public ListNamesWordsComboModel(RecognizeModel model) {
		this.model = model;
		model.addListener(this);
		list = null;
		if(getList().size() > 0)
		index = 0;
	}

	public int getSize() {
		return getList().size();
	}

	private List<String> getList() {
		if(list == null){
			list = model.getNames();
		}
		return list;
	}
	
	public String getElementAt(int index) {
		return getList().get(index);
	}
	
	private List<ListDataListener> listeners = new ArrayList<ListDataListener>();

	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	public void setSelectedItem(Object item) {
		index = getList().indexOf(item);
	}

	public Object getSelectedItem() {
		if(index < 0 || index >= getSize())
			return null;
		return getList().get(index);
	}

	public void added(String name) {
		list = null;
		int indexOf = getList().indexOf(name);
		index = indexOf;
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, indexOf, indexOf);
		for(ListDataListener l : listeners){
			l.intervalAdded(event);
		}
	}

	public void removed(String name) {
		int indexOf = getList().indexOf(name);
		list = null;
		index = getList().size() > 0 ? 0 : -1;
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, indexOf, indexOf);
		for(ListDataListener l : listeners){
			l.intervalAdded(event);
		}
	}

}
