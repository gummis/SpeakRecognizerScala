package sly.speakrecognizer.test.hmm.vector.algorithm;

public class AlghoritmsTemplateJava {
	
	/*public static double[][] getAlpha(double[] pi,double[][]a, int[] oseq) {
		int statesLength = pi.length;
		double sum;
		double[][] alpha = new double[oseq.length][statesLength];
		//1. Inicialization
		for(int i = 0 ; i < statesLength ; i++){
			alpha[0][i] = hmm.getPI()[i] * hmm.getB()[i][oseq[0]];
		}
		//2. Induction
		for(int t = 1 ; t < oseq.length ; t++){
			int o = oseq[t];
			double[] prev = alpha[t-1];
			double[] cur = alpha[t];
			for(int j = 0 ; j < statesLength ; j++){
				sum = 0.0;
				for(int i = 0 ; i < statesLength ; i++){
					sum += prev[i] * hmm.getA()[i][j];
				}
				cur[j] = sum * hmm.getB()[j][o];
			}
		}
		return alpha;
	}
	
	public static double[][] getBeta(HmmDiscrete hmm, int[] oseq) {
		int statesLength = hmm.getStateNames().length;
		double sum;
		double[][] beta = new double[oseq.length][statesLength];
		//1. Inicialization
		int last = beta.length-1;
		for(int i = 0 ; i < statesLength ; i++){
			beta[last][i] = 1.0;
		}
		//2. Induction
		for(int t = last-1 ; t >= 0 ; t--){
			int o = oseq[t+1];
			for(int i = 0 ; i < statesLength ; i++){
				sum = 0.0;
				for(int j = 0 ; j < statesLength ; j++){
					sum += hmm.getA()[i][j] * hmm.getB()[j][o] * beta[t+1][j];
				}
				beta[t][i] = sum;
			}
		}
		return beta;
	}
	
	public static double opdfProbability(double[] observation, double[] mean, double[] covariance){
		
	}*/
	
}
