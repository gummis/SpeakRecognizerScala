package sly.speakrecognizer.audio.modules;

public class DCT {
	private final double[][] filterMuls;
	
	public DCT(int lengthFilters,int lengthCepstralCoefficients){
		filterMuls = new double[lengthCepstralCoefficients][];
		for(int icc = 0 ; icc < lengthCepstralCoefficients ; icc++){
			double[] muls = new double[lengthFilters];
			double mul = Math.PI * icc / lengthFilters;
			for(int ifi = 0 ; ifi < lengthFilters ; ifi++){
				muls[ifi] = Math.cos(mul * (0.5 + ifi));
			}
			filterMuls[icc] = muls;
		}
	}
	
	public int getLengthFilters(){
		return filterMuls[0].length;
	}
	
	public int getLengthCepstralCoefficients(){
		return filterMuls.length;
	}
	
	public void transform(double[] values,double[] result,int startIndex){
		for(int icc=0; icc < filterMuls.length; icc++){
			double sum = 0.0;
			double[] muls = filterMuls[icc];
			for(int ifi=0; ifi < muls.length; ifi++){
				sum += muls[ifi] * values[ifi];
			}
			result[startIndex + icc] = sum;
		}
	}
}
