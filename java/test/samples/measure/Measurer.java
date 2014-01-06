package samples.measure;

public class Measurer {

	public double distance(Vector v1, Vector v2){
		double sum = 0.0;
		double[] v1v = v1.getValues();
		double[] v2v = v2.getValues();
		long nano = System.nanoTime();
		for(int y = 0 ; y < 100000 ;y++)
			for(int x = 0 ; x < v1.length() ; x++){
				double v = v1v[x] - v2v[x];
				sum += v*v;
			}
		System.out.println("NANO: "+(System.nanoTime() - nano));
		return sum;
	}
	
	public double distance(VectorSequence v1, VectorSequence v2){
		double sum = 0.0;
		
		
		
		
		return sum;
	}
	
}
