package sly.speakrecognizer.test.hmm.vector.domain;

import be.ac.ulg.montefiore.run.distributions.MultiGaussianDistribution;

public class HmmAndOpdfTemplate {
	public double calcOpdfProbability(double[] obs, double[] mean, double[][] covariance){
		MultiGaussianDistribution mgd = new MultiGaussianDistribution(mean, covariance);
		return mgd.probability(obs);
	}
}
