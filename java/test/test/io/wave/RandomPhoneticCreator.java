package test.io.wave;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import sly.speakrecognizer.io.wav.WavFile;

public class RandomPhoneticCreator {

	@Test
	public void test() {
		List<String> list = new ArrayList<String>(26);
		list.add("alpha");
		list.add("bravo");
		list.add("charlie");
		list.add("delta");
		list.add("echo");
		list.add("foxtrot");
		list.add("golf");
		list.add("hotel");
		list.add("india");
		list.add("juliet");
		list.add("kilo");
		list.add("lima");
		list.add("mike");
		list.add("november");
		list.add("oscar");
		list.add("papa");
		list.add("quebec");
		list.add("romeo");
		list.add("sierra");
		list.add("tango");
		list.add("uniform");
		list.add("victor");
		list.add("vhiskey");
		list.add("xray");
		list.add("yankee");
		list.add("zulu");
		
		double[][] out = new double[26][];
		Random r = new Random(System.currentTimeMillis());
        int length = 0;
        int numChannels=0,validBits=0,sampleRate=0;
        boolean hr = false;
		while(!list.isEmpty()){
			String s = list.remove(r.nextInt(list.size()));
			WavFile wav = null;
	        try {
				wav = WavFile.openWavFile(new File("d:/dyplom/probki/"+s+"_sly.wav"));
	        }catch (Exception e) {
	        	System.out.println("nie ma próbki "+s+"_sly.wav");
			}
	        if(wav == null){
	        	continue;
	        }
	        if(!hr){
	        	hr = true;
	        	numChannels = wav.getNumChannels();
	        	validBits = wav.getValidBits();
	        	sampleRate = (int) wav.getSampleRate();
	        }
	        
	        //===================
	        double[] o = new double[(int) wav.getNumFrames()];
	        try {
				wav.readFrames(o, o.length);
			} catch (Exception e) {
				System.out.println("próbka nieodczytana "+s+"_sly.wav");
				o = null;
			}
	        out[25-list.size()] = o;
	        if(o != null){
	        	length += o.length;
	        }
	        try{
	        	wav.close();
	        }catch(Exception e){
	        }
	        //===================
		}

        File file = new File("output.wav");
        if(file.exists()){
       	 file.delete();
        }

        WavFile wavFile = null;
        try{
	        // Create a wav file with the name specified as the first argument
	        wavFile = WavFile.newWavFile(file, numChannels,(long)length, validBits, (long)sampleRate);
	
	        for(double[] o : out){
	        	if(o != null){
	                wavFile.writeFrames(o, o.length);
	        	}
	        }

        }catch(Exception e){
			System.out.println("Plik wynikowy nie został poprawnie wygenerowany");
        }
        finally{
            try {
				wavFile.close();
			} catch (IOException e) {
				System.out.println("Plik wynikowy nie został poprawnie zamkniety");
			}
        }
	}
	
}
