package samples.measure;

public class Vector {
	private final double[] values;
	
	public Vector(double[] v){
		values = v;
	}

	public double[] getValues() {
		return values;
	}
	
	public int length() {
		return values.length;
	}
	
	public double get(int index){
		return values[index];
	}

}
