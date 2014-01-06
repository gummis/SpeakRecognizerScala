package sly.speakrecognizer.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class Microphone {

	private class MicrophoneThread extends Thread{

		TargetDataLine line;
		AudioFormat format;
		boolean stopped;

		public MicrophoneThread() throws Exception{
			format = new AudioFormat(8000.0F, 16, 1, true,
					true);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class,
					format);
			if (!AudioSystem.isLineSupported(info)) {
				throw new Exception("Przy danych ustawieniach nie mozna było rozpoczać nagrywania z mikrofonu");
			}

			line = (TargetDataLine) AudioSystem.getLine(info);
			stopped = false;
			setDaemon(true);
			this.start();
		}
		
		public void run() {
			try {
				line.open(format, 0x1000);
				int numBytesRead;
				byte[] array = new byte[line.getBufferSize()];

				// Begin audio capture.
				line.start();
				double max = -((double)Short.MIN_VALUE);
				// Here, stopped is a global boolean set by another thread.
				while (!stopped) {
					sleep(10);
					// Read the next chunk of data from the TargetDataLine.
					numBytesRead = line.read(array, 0, array.length);

					for(int x = 0 ; x < numBytesRead; x+=2 ){
						short sample = (short)( ((array[x]&0xFF)<<8) | (array[x+1]&0xFF) );
						data.add(((double)sample)/max);
					}
				}
				stopped = false;

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(line!= null && line.isOpen()){
					line.stop();
					line.close();
				}
				thread = null;
				for(SounderListener sl : listeners){
					sl.stopped();
				}
			}
		}
	}

	MicrophoneThread thread;
	private List<Double> data = new Vector<Double>(1000000);

	public Microphone() {

	}

	public void clearData() {
		data.clear();
	}

	public void startRecording() throws Exception {
		clearData();
		thread = new MicrophoneThread();
	}

	public void stopRecording() {
		if(thread != null){
			thread.stopped = true;
		}
	}

	public double[] getData() {
		double[] dd = new double[data.size()];
		int ddi = 0;
		for (double d : data)
			dd[ddi++] = d;
		return dd;
	}

	public boolean isReady() {
		return true;
	}

	public boolean isRecording() {
		return thread != null;
	}
	private List<SounderListener> listeners = new ArrayList<SounderListener>();
	public void addSounderListener(SounderListener listener){
		listeners.add(listener);
	}
	public void removeSounderListener(SounderListener listener){
		listeners.remove(listener);
	}
}
