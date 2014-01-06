package sly.speakrecognizer.modules.soundanalyzer;

public class MelFilter {
	private final double[] values;
	private final int startIndex;
	
	public MelFilter(double[] values, int startIndex){
		this.values = values;
		this.startIndex = startIndex;
	}
	
	public double filter(double[] data){
		int index = startIndex;
		double res = 0.0;
		for(int x = 0 ; x < values.length ; x++){
			res += data[index++] * values[x];
		}
		return res;
	}
}