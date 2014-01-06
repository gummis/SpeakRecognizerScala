package be.ac.ulg.montefiore.run.jahmm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchScaledLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;

public class TestAll {
	@Test
	public void test() {
		//tworzenie obserwacji

		//uczenie 
		BaumWelchLearner bwlearner = new BaumWelchScaledLearner();

		List<List<ObservationVector>> sequences = new ArrayList<List<ObservationVector>>();

		List<ObservationVector> seq = new ArrayList<ObservationVector>();
		seq.add(new ObservationVector(new double[]{1,2,3,4}));
		seq.add(new ObservationVector(new double[]{2,3,4,5}));
		seq.add(new ObservationVector(new double[]{5,7,8,10}));
		seq.add(new ObservationVector(new double[]{12,33,44,55}));
		sequences.add(seq);

		seq = new ArrayList<ObservationVector>();
		seq.add(new ObservationVector(new double[]{34,55,66,66}));
		seq.add(new ObservationVector(new double[]{55,66,77,88}));
		seq.add(new ObservationVector(new double[]{99,100,111,111}));
		seq.add(new ObservationVector(new double[]{1111,1111,1111,155}));
		sequences.add(seq);
		
		List<List<ObservationVector>> sequences1 = new ArrayList<List<ObservationVector>>();
				
		seq = new ArrayList<ObservationVector>();
		seq.add(new ObservationVector(new double[]{55,55,44,33}));
		seq.add(new ObservationVector(new double[]{44,35,43,32}));
		seq.add(new ObservationVector(new double[]{22,22,24,24}));
		seq.add(new ObservationVector(new double[]{12,21,14,16}));
		sequences1.add(seq);

		seq = new ArrayList<ObservationVector>();
		seq.add(new ObservationVector(new double[]{44,66,77,78}));
		seq.add(new ObservationVector(new double[]{13,12,11,10}));
		seq.add(new ObservationVector(new double[]{6,7,8,9}));
		seq.add(new ObservationVector(new double[]{1,3,2,1}));
		sequences1.add(seq);

		Hmm<ObservationVector> hmm = bwlearner.learn(new Hmm<ObservationVector>(5,new OpdfMultiGaussianFactory(4)), sequences);
		Hmm<ObservationVector> hmm1 = bwlearner.learn(new Hmm<ObservationVector>(5,new OpdfMultiGaussianFactory(4)), sequences1);
		

		System.out.println("hmm=");
		System.out.println(hmm);
		System.out.println("hmm1=");
		System.out.println(hmm1);
		
	}

}
