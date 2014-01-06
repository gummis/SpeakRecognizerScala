package samples.measure;

import java.util.Arrays;
import java.util.Random;

public class VectorSequenceRandomFactory {
	private final Random r;
	private final int lengthVector;
	private final int sequenceMin;
	private final int sequenceMax;
	
	
	public VectorSequenceRandomFactory(int lv,int smin,int smax){
		r = new Random(System.currentTimeMillis());
		lengthVector = lv;
		sequenceMin = smin;
		sequenceMax = smax;
	}
	
	public VectorSequence generate(){
		Vector[] va = new Vector[r.nextInt(sequenceMax - sequenceMin + 1) + sequenceMin];
		double[] values;
		for(int x = 0 ; x < va.length ; x++){
			values = new double[lengthVector];
			for(int y = 0 ; y < values.length ; y++){
				values[y] = r.nextDouble();
			}
			va[x] = new Vector(values);
		}
		return new VectorSequence(Arrays.asList(va));
	}
}
