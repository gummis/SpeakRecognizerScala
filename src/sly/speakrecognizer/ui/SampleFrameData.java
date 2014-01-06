package sly.speakrecognizer.ui;

/**
 * Obiekt pamiętający wszystkie stany danych przetwarznej ramki.
 * Od surowych danych(probki) do cepstrum + pochodne.
 * @author sly
 *
 */
public class SampleFrameData {
	private double[] raw;
	private double[] fft;
	private double[] mfb;
	private double[] dct;
	private double[] drp;
	
	public void setRaw(double[] raw) {
		this.raw = raw;
	}

	public void setFft(double[] ddata) {
		fft = ddata;
	}

	public void setMfb(double[] ddata) {
		mfb = ddata;
	}

	public void setDct(double[] ddata) {
		dct = ddata;
	}

	public void setDrp(double[] ddata) {
		drp = ddata;
	}

	public double[] getRaw() {
		return raw;
	}

	public double[] getFft() {
		return fft;
	}

	public double[] getMfb() {
		return mfb;
	}

	public double[] getDct() {
		return dct;
	}

	public double[] getDrp() {
		return drp;
	}
	
	
}
