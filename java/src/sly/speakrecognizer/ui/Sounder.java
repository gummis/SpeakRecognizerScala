package sly.speakrecognizer.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Sounder {

	private class SounderThread extends Thread {
		
		SourceDataLine line;
		AudioFormat format;
		boolean stopped;

		public SounderThread() throws Exception{
			
			
			format = new AudioFormat(8000.0F, 16, 1, true,
					true);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					format);
			if (!AudioSystem.isLineSupported(info)) {
				throw new Exception("Przy danych ustawieniach nie mozna było rozpoczać nagrywania z mikrofonu");
			}

			line = (SourceDataLine) AudioSystem.getLine(info);
			stopped = false;
			setDaemon(true);
			this.start();
		}
		
		public void run() {
			try {
				line.open(format, 0x1000);
				line.start();

				int indexData = 0;
				int indexArray;
				double max = -((double)Short.MIN_VALUE);
				byte[] array = new byte[1600];

				while(!stopped && indexData < data.length){
					indexArray = 0;
					while(indexArray < array.length && indexData < data.length){
						short val = (short)(data[indexData++] * max);
						array[indexArray++] = (byte) ((val >> 8)&0xFF);
						array[indexArray++] = (byte) (val&0xFF);
					}

					line.write(array, 0,indexArray);
				}
				stopped = false;
				thread = null;
				for(SounderListener sl : listeners){
					sl.stopped();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(line != null){
					line.drain();
					line.stop();
					line.close();
				}
			}
		}
	}

	private SounderThread thread;
	private double[] data;
	
	public Sounder() {

	}

	public void setData(double[] data) {
		this.data = data;
	}

	public void startPlaying() throws Exception {
		thread = new SounderThread();
	}

	public void stopPlaying() {
		if(thread != null){
			thread.stopped = true;
		}
	}

	public boolean isReady() {
		return true;
	}

	public boolean isPlaying() {
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
