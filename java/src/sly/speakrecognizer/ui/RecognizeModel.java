package sly.speakrecognizer.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ac.ulg.montefiore.run.jahmm.ObservationVector;

public class RecognizeModel {

	private Map<String, WordModel> words;

	public RecognizeModel() {
		clear();
	}

	public void newWord(String name, int nbStates)
			throws RecognizeModelException {
		name = parseName(name);
		if (words.containsKey(name))
			throw new RecognizeModelException(
					"Istnieje już takie słowo w słowniku.");
		if (nbStates < 3) {
			nbStates = CalcNbStates(name);
		}
		words.put(name, new WordModel(name, nbStates));
		for (RecognizeModelListener l : listeners) {
			l.added(name);
		}
	}

	private int CalcNbStates(String name) {
		return 5;
	}

	public void removeWord(String name) throws RecognizeModelException {
		name = parseName(name);
		if (!words.containsKey(name))
			throw new RecognizeModelException("Brak słowa '" + name
					+ "' w słowniku.");
		words.remove(name);
		for (RecognizeModelListener l : listeners) {
			l.removed(name);
		}
	}

	private String parseName(String name) throws RecognizeModelException {
		if (name == null)
			throw new RecognizeModelException("Nazwa nowego słowa null");
		name = name.trim();
		if (name.isEmpty())
			throw new RecognizeModelException("Nazwa nowego słowa pusta");
		return name;
	}

	public List<String> getNames() {
		List<String> names = new ArrayList<String>(words.keySet());
		Collections.sort(names);
		return names;
	}

	public String[] getNamesArray() {
		List<String> list = getNames();
		return getNames().toArray(new String[list.size()]);
	}

	public WordModel getWordModel(String name) throws RecognizeModelException {
		WordModel model = words.get(name);
		if (model != null) {
			throw new RecognizeModelException("Model o nazwie '" + name
					+ "' nie istnieje");
		}
		return model;
	}

	public void saveToFile(File file) {
		FileOutputStream fileOut = null;
		if (file.exists()) {
			file.delete();
		}
		try {
			fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(words);
			out.close();
		} catch (IOException i) {
			i.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void loadFromFile(File file) {
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		try {
			fileIn = new FileInputStream(file);
			in = new ObjectInputStream(fileIn);
			words = (Map<String, WordModel>) in.readObject();
		} catch (Exception i) {
			i.printStackTrace();
		} finally {
			try {
				in.close();
				fileIn.close();
			} catch (IOException e) {
			}
		}
	}
	public void clear() {
		words = new HashMap<String, WordModel>();
	}

	public int getSize() {
		return words.size();
	}

	private List<RecognizeModelListener> listeners = new ArrayList<RecognizeModelListener>();

	public void addListener(RecognizeModelListener l) {
		listeners.add(l);
	}

	public void removeListDataListener(RecognizeModelListener l) {
		listeners.remove(l);
	}

	public void learn(String name, List<List<ObservationVector>> observations) {
		WordModel model = words.get(name);
		if (model != null) {
			model.learn(observations);
		}
	}

	public String recognize(List<ObservationVector> sequence) {
		double resprob = 0.0;
		String res = "";
		for (String name : words.keySet()) {
			WordModel word = words.get(name);
			double prob = word.probability(sequence);
			if (prob > resprob) {
				resprob = prob;
				res = name;
			}
		}
		return res;
	}


}
