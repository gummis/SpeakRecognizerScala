package sly.speakrecognizer.modules;

public class SoundSamplesData extends AbstractSoundData {

	private final double[][] data;

	public SoundSamplesData(double[][] data,double freq) {
		super(freq);
		this.data = data;
	}
	
	public SoundSamplesData(double[][] data, int len,double freq) {
		super(freq);
		double[][] ndata = new double[data.length][len];
		for(int x = 0 ; x < data.length ; x++){
			System.arraycopy(data[x], 0, ndata[x], 0, len);
		}
		this.data = ndata;
	}

	public SoundSamplesData(double[] data,double freq) {
		super(freq);
		this.data = new double[][] { data };
	}

	public SoundSamplesData(double data,double freq) {
		super(freq);
		this.data = new double[][] { new double[] { data } };
	}

	public SoundSamplesData(double datar, double datal,double freq) {
		super(freq);
		this.data = new double[][] { new double[] { datar },
				new double[] { datal } };
	}

	public SoundSamplesData(double datarf, double datalf, double datarb, double datalb,double freq) {
		super(freq);
		this.data = new double[][] { new double[] { datarf },
				new double[] { datalf }, new double[] { datarb },
				new double[] { datalb } };
	}

	public static double[][] getMultiChannelsOneSample(double[] data){
		double[][] retdata = new double[data.length][1];
		for(int x = 0 ; x < data.length ; x++){
			retdata[x][0] = data[x];
		}
		return retdata;
	}
	
	public int getLengthChannels() {
		return data.length;
	}

	public int getLengthSamples() {
		return data[0].length;
	}
	
	public double[][] getData(){
		return data;
	}

	public double[] getData(int channel){
		return data[channel];
	}
	
	public double getData(int channel,int sample){
		return data[channel][sample];
	}
}
