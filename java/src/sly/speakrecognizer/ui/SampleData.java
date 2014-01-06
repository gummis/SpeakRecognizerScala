package sly.speakrecognizer.ui;

public class SampleData {
	private double[] raw;
	private SampleFrameData[] frames;

	public SampleData(double[] raw){
		this.raw = raw;
	}
	
	public SampleFrameData[] getSampleFrameData(){
		if(frames == null){
			frames = SampleDataUtil.createSampleFrameData(raw);
		}
		return frames;
	}

	public void setSampleFrameData(SampleFrameData[] sfd){
		frames = sfd;
	}
	
	public double[] getRawData() {
		return raw;
	}
}
