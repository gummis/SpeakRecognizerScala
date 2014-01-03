package sly.speakrecognizer.test.hmm.discrete.algorithm.template;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

import be.ac.ulg.montefiore.run.jahmm.ForwardBackwardCalculator;
import be.ac.ulg.montefiore.run.jahmm.ForwardBackwardCalculator.Computation;
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;
import be.ac.ulg.montefiore.run.jahmm.OpdfInteger;
import be.ac.ulg.montefiore.run.jahmm.ViterbiCalculator;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;

public class DiscreteAlghoritmsTemplateJava {
	
	private List<ObservationInteger> observations;
	private Hmm<ObservationInteger> hmm;
	private ForwardBackwardCalculator fwc;
	

	private void prepare( double[] pi,double[][]a, double[][] b, int[] oseq){

		observations = null;
		hmm = null;
		fwc = null;
		
		if(pi != null && a != null && b != null){

			List<OpdfInteger> opdfs = new ArrayList<OpdfInteger>();
			for(double[] ar : b){
				opdfs.add(new OpdfInteger(ar));
			}
					
			hmm = new Hmm<ObservationInteger>(pi,a,opdfs);
		}
		
		if(oseq != null){
			observations = new ArrayList<ObservationInteger>();
			for(int ob: oseq){
				observations.add(new ObservationInteger(ob));
			}
		}
		
		if(hmm != null && observations != null){
			fwc = new ForwardBackwardCalculator(observations, hmm, EnumSet.of(Computation.ALPHA,Computation.BETA));
		}
	}

	public double[][] getAlphaOrBeta(boolean beta, double[] pi,double[][]a, double[][] b, int[] oseq) {
		prepare(pi,a,b,oseq);
		double[][] alphaOrBeta = new double[oseq.length][pi.length];
		for(int t = 0 ; t < oseq.length ; t++)
			for(int i = 0 ; i < pi.length ; i++){
				alphaOrBeta[t][i] = beta ? fwc.betaElement(t, i) : fwc.alphaElement(t, i);
			}
		return alphaOrBeta;
	}
	
	public double[][][] getXi(double[] pi,double[][]a, double[][] b, int[] oseq){
		prepare(pi,a,b,oseq);
		return new BaumWelchLearnerDiscreteTemplater().discreteEstimateXi(observations, fwc, hmm);
	}
	
	public double[][] getGamma(double[] pi,double[][]a, double[][] b, int[] oseq){
		double[][][] xi = getXi(pi,a,b,oseq);
		return new BaumWelchLearnerDiscreteTemplater().discreteEstimateGamma(xi, fwc);
	}

	class BaumWelchLearnerDiscreteTemplater extends BaumWelchLearner {
		public double[][][] discreteEstimateXi(List<ObservationInteger> sequence, ForwardBackwardCalculator fbc,
						Hmm<ObservationInteger> hmm){
				return estimateXi(sequence,fbc,hmm);
		}
		
		public double[][] discreteEstimateGamma(double[][][] xi, ForwardBackwardCalculator fbc){
			return estimateGamma(xi,fbc);
		}
	}
	
	public Pair<Double,int[]> getViterbi(double[] pi,double[][]a, double[][] b, int[] oseq){
		prepare(pi,a,b,oseq);
		ViterbiCalculator calc = new ViterbiCalculator(observations,hmm);
		return new Pair<Double,int[]>(calc.lnProbability(), calc.stateSequence());
	}
	
	public Pair<double[][],int[][]> calcDeltaPsiWithLog(double[] pi,double[][]a, double[][] b, int[] oseq) {
		prepare(pi,a,b,oseq);
		ViterbiCalculatorTemplate template = new ViterbiCalculatorTemplate(observations,  hmm);
		return new Pair<double[][],int[][]>(template.getDelta(),template.getPsy());
	}
	
	public Map<String,Object> transformToViterbi1Input(double[] pi,double[][]a, double[][] b, int[] oseq){
        Hashtable<Integer, Hashtable<Integer, Double>> transitions =
                new Hashtable<Integer, Hashtable<Integer, Double>>();

        StringBuilder seq = new StringBuilder();
        for(int o : oseq){
        	char c = (char) ('a' + o);
        	seq.append(c);
        }
        		
        Hashtable<Integer, Double> q0 = new Hashtable<Integer, Double>();
        for(int x = 0 ; x < pi.length ; x++){
            q0.put(x+1, pi[x]);
        }
        transitions.put(0,q0);
        for(int x = 0 ; x < a.length ; x++){
        	double[] ae = a[x];
            Hashtable<Integer, Double> q = new Hashtable<Integer, Double>();
            int y;
            for(y = 0 ; y < ae.length ; y++){
            	q.put(y+1,ae[y]);
            }
        	q.put(y+1,ae[y-1]);
            transitions.put(x+1,q);
        }
        
        // prepare outputs probabilities table
        Hashtable<Integer, Hashtable<Character, Double>> outputs = new Hashtable<Integer, Hashtable<Character, Double>>();
        for(int x = 0 ; x < b.length ; x++){
        	Hashtable<Character, Double> out = new Hashtable<Character, Double>();
        	double[] be = b[x];
            for(int y = 0 ; y < be.length ; y++){
            	char c = (char) ('a' + y);
            	out.put(c,be[y]);
            }
            outputs.put(x+1,out);
        }
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("seq", seq.toString());
        map.put("transitions", transitions);
        map.put("outputs", outputs);
        return map;
	}

	public Pair<Double,int[]> getViterbi1(double[] pi,double[][]a, double[][] b, int[] oseq) {
		Map<String,Object> map = transformToViterbi1Input(pi,a,b,oseq);
		List<Integer> bestStates = ViterbiCalculatorTemplate1.viterbiPath(map.get("seq").toString(), 
				(Hashtable<Integer, Hashtable<Integer, Double>>) map.get("transitions"), 
				(Hashtable<Integer, Hashtable<Character, Double>>) map.get("outputs"));
		int[] bestStatesArray = new int[bestStates.size()];
		int ind = 0;
		for(Integer inte : bestStates){
			bestStatesArray[ind++] = inte-1;
		}
		double[] probabilities = ViterbiCalculatorTemplate1.getProb();
		double max = Double.MIN_VALUE;
		for(double val : probabilities){
			if(max < val)
				max = val;
		}
		double probability = max;
		return new Pair<Double,int[]>(probability,bestStatesArray);
	}
	
	public Pair<double[][],int[][]> calcDeltaPsiWithoutLog(double[] pi,double[][]a, double[][] b, int[] oseq) {
		Map<String,Object> map = transformToViterbi1Input(pi,a,b,oseq);
		ViterbiCalculatorTemplate1.viterbiPath(map.get("seq").toString(), 
				(Hashtable<Integer, Hashtable<Integer, Double>>) map.get("transitions"), 
				(Hashtable<Integer, Hashtable<Character, Double>>) map.get("outputs"));
		
		return new Pair<double[][],int[][]>(ViterbiCalculatorTemplate1.getDelta(),ViterbiCalculatorTemplate1.getPsi());
	}
	
	public Object[] calcBaumWelch(double[] pi,double[][]a, double[][] b, int[][] sequences){
		return new Object[]{pi,a,b};
	}
}
