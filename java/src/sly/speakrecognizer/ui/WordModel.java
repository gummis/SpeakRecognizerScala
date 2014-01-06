package sly.speakrecognizer.ui;

import java.io.Serializable;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;

public class WordModel implements Serializable{

	private String name;
	private Hmm<ObservationVector> hmm;
	
	public WordModel(String name,int nbStates) {
		hmm = new Hmm<ObservationVector>(nbStates,new OpdfMultiGaussianFactory(39));
		this.name = name;
	}

	
	public void learn(List<List<ObservationVector>> observations) {
		BaumWelchLearner learner = new BaumWelchLearner();
		hmm = learner.learn(hmm, observations);
	}
	
	public double probability(List<? extends ObservationVector> oseq){
		return hmm.lnProbability(oseq);
	}
	
}
