package samples.measure;

import java.util.List;

public class VectorSequenceClass {
	private final String name;
	private final List<VectorSequence> sequences;
	
	public VectorSequenceClass(String n,List<VectorSequence> s){
		name = n;
		sequences = s;
	}
	
	public String getName() {
		return name;
	}

	public List<VectorSequence> getSequences() {
		return sequences;
	}

	public void add(VectorSequence seq) {
		sequences.add(seq);
	}
	public int length() {
		return sequences.size();
	}
	
	public VectorSequence get(int index){
		return sequences.get(index);
	}
}
