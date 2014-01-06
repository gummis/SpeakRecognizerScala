package samples.measure;

import java.util.List;

public class VectorSequence{
	private final List<Vector> vectors;
	private double[] dtw;
	
	public VectorSequence(List<Vector> v){
		vectors = v;
	}
	public List<Vector> getVectors() {
		return vectors;
	}
	public void add(Vector vec) {
		vectors.add(vec);
	}
	public int length() {
		return vectors.size();
	}
	
	public Vector get(int index){
		return vectors.get(index);
	}
	public void ini(){
		dtw = new double[vectors.size()];
	}
	public double dtwIni(Vector x){
		double sum = 0.0;
		double v;
		for(int i = 0 ; i < dtw.length ; i++){
			double[] xv = x.getValues();
			double[] wv = vectors.get(i).getValues();
			for(int i1 = 0 ; i1 < wv.length ; i1++){
					v = xv[i1] - wv[i1];
					sum += v*v;
			}
			dtw[i] = sum;
		}
		return sum;
	}
	
	public double dtwIter(Vector x){
		double[] xv = x.getValues();
		double[] wv = vectors.get(0).getValues();
		double sum = 0;
		double v;
		for(int i1 = 0 ; i1 < wv.length ; i1++){
			v = xv[i1] - wv[i1];
			sum += v*v;
		}		
		double j_1i_1 = dtw[0];
		double i_1 = dtw[0] += sum;
		for(int i = 1 ; i < dtw.length ; i++){
			wv = vectors.get(i).getValues();
			sum = 0;
			for(int i1 = 0 ; i1 < wv.length ; i1++){
				v = xv[i1] - wv[i1];
				sum += v*v;
			}
			i_1 = Math.min(Math.min(dtw[i],i_1)+sum,j_1i_1+2*sum);
			j_1i_1 = dtw[i];
			dtw[i] = i_1;
		}
		return sum;
	}
	
}