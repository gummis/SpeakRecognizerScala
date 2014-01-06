package samples.measure;

import org.junit.Test;

public class MeasureTest {
	@Test
	public void MeasureRandomVectors(){
		Measurer m = new Measurer();
		VectorSequenceRandomFactory f = new VectorSequenceRandomFactory(3, 3, 3);
		VectorSequence vs = f.generate();
		Vector v0 = vs.get(0);
		Vector v1 = vs.get(1);
		Vector v2 = vs.get(2);/*
		System.out.println("Result of MeasureRandomVectors");
		System.out.println(m.distance(v0, v0));
		System.out.println(m.distance(v0, v1));
		System.out.println(m.distance(v0, v2));
		System.out.println(m.distance(v1, v0));
		System.out.println(m.distance(v1, v1));
		System.out.println(m.distance(v1, v2));
		System.out.println(m.distance(v2, v0));
		System.out.println(m.distance(v2, v1));
		System.out.println(m.distance(v2, v2));*/
m.distance(v2, v1);
		
		System.out.println("End Result of MeasureRandomVectors");
	}
	/*
	@Test
	public void MeasureVectors(){
		Measurer m = new Measurer();
		Vector v0 = new Vector(new double[]{1,2,3});
		Vector v1 = new Vector(new double[]{2,3,4});
		Vector v2 = new Vector(new double[]{3,2,1});
		System.out.println("Result of MeasureVectors");
		System.out.println(m.distance(v0, v0));
		System.out.println(m.distance(v0, v1));
		System.out.println(m.distance(v0, v2));
		System.out.println(m.distance(v1, v0));
		System.out.println(m.distance(v1, v1));
		System.out.println(m.distance(v1, v2));
		System.out.println(m.distance(v2, v0));
		System.out.println(m.distance(v2, v1));
		System.out.println(m.distance(v2, v2));
		System.out.println("End Result of MeasureVectors");		
	}
	
	@Test
	public void MeasureRandomVectorSequences(){
		Measurer m = new Measurer();
		VectorSequenceRandomFactory f = new VectorSequenceRandomFactory(3, 3, 15);
		VectorSequence v0 = f.generate();
		VectorSequence v1 = f.generate();
		VectorSequence v2 = f.generate();;
		System.out.println("Result of MeasureRandomVectorSequences");
		System.out.println(m.distance(v0, v0));
		System.out.println(m.distance(v0, v1));
		System.out.println(m.distance(v0, v2));
		System.out.println(m.distance(v1, v0));
		System.out.println(m.distance(v1, v1));
		System.out.println(m.distance(v1, v2));
		System.out.println(m.distance(v2, v0));
		System.out.println(m.distance(v2, v1));
		System.out.println(m.distance(v2, v2));
		System.out.println("End Result of MeasureRandomVectorSequences");		
	}*/
}
