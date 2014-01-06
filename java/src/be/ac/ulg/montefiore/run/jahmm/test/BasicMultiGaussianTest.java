/* jahmm package - v0.6.1 */

/*
  *  Copyright (c) 2004-2006, Jean-Marc Francois.
 *
 *  This file is part of Jahmm.
 *  Jahmm is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Jahmm is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Jahmm; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

package be.ac.ulg.montefiore.run.jahmm.test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import junit.framework.TestCase;
import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;


public class BasicMultiGaussianTest 
extends TestCase
{
	final static private double DELTA = 1.E-10;
	
	private Hmm<ObservationVector> hmm;
	private List<ObservationVector> sequence,sequence1,sequence2;
	private List<ObservationVector> randomSequence,randomSequence1,randomSequence2;
	
	
	protected void setUp()
	{ 
		hmm = new Hmm<ObservationVector>(5, new OpdfMultiGaussianFactory(39));
		
		sequence = new ArrayList<ObservationVector>();
		for (int i = 0; i < 5; i++){
			double[] tab = new double[39];
			for(int x = 0 ; x < tab.length ; x++)
				tab[x] = i*40+x;
			sequence.add(new ObservationVector(tab));
		}

		sequence1 = new ArrayList<ObservationVector>();
		for (int i = 0; i < 5; i++){
			double[] tab = new double[39];
			for(int x = 0 ; x < tab.length ; x++)
				tab[x] = 200 + i*40 + x;
			sequence1.add(new ObservationVector(tab));
		}

		sequence2 = new ArrayList<ObservationVector>();
		for (int i = 0; i < 5; i++){
			double[] tab = new double[39];
			for(int x = 0 ; x < tab.length ; x++)
				tab[x] = 300 - i*40 - x;
			sequence2.add(new ObservationVector(tab));
		}
		
		randomSequence = new ArrayList<ObservationVector>();
		for (int i = 0; i < 30000; i++){
			double[] tab = new double[39];
			for(int x = 0 ; x < tab.length ; x++)
				tab[x] = Math.random()*100;
			randomSequence.add(new ObservationVector(tab));
		}
		
		randomSequence1 = new ArrayList<ObservationVector>();
		for (int i = 0; i < 30000; i++){
			double[] tab = new double[39];
			for(int x = 0 ; x < tab.length ; x++)
				tab[x] = Math.random()*100;
			randomSequence1.add(new ObservationVector(tab));
		}
		
		randomSequence2 = new ArrayList<ObservationVector>();
		for (int i = 0; i < 30000; i++){
			double[] tab = new double[39];
			for(int x = 0 ; x < tab.length ; x++)
				tab[x] = Math.random()*100;
			randomSequence2.add(new ObservationVector(tab));
		}
		
	}
	
	public void testBaumWelch(){
		BaumWelchLearner learner = new BaumWelchLearner();
		List<List<ObservationVector>> sequences = new ArrayList<List<ObservationVector>>();
		sequences.add(sequence);
		sequences.add(sequence1);
		sequences.add(sequence2);
		Hmm<ObservationVector> hmm1 = learner.learn(hmm, sequences);
		hmm1 = null;
	}
	
	/*public void testForwardBackward()
	{	
		ForwardBackwardCalculator fbc = new ForwardBackwardCalculator(sequence, hmm,EnumSet.allOf(ForwardBackwardCalculator.Computation.class));
		
		assertEquals(1.8697705349794245E-5, fbc.probability(), DELTA);
		
		ForwardBackwardScaledCalculator fbsc = new ForwardBackwardScaledCalculator(sequence, hmm,EnumSet.allOf(ForwardBackwardCalculator.Computation.class));
		
		assertEquals(1.8697705349794245E-5, fbsc.probability(), DELTA);
	}*/
	
	
	/*public void testViterbi()
	{	
		ViterbiCalculator vc = new ViterbiCalculator(sequence, hmm);
		
		assertEquals(4.1152263374485705E-8, 
				Math.exp(vc.lnProbability()), DELTA);
	}*/
	
	
	/*public void testKMeansCalculator()
	{	
		int nbClusters = 20;
		
		KMeansCalculator<ObservationVector> kmc = new
		KMeansCalculator<ObservationVector>(nbClusters, randomSequence);
		
		assertEquals("KMeans did not produce expected number of clusters",
				nbClusters, kmc.nbClusters());
	}*/
}
