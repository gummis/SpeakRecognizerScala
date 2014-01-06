package test.io.wave;

import java.io.File;

import org.junit.Test;

import sly.speakrecognizer.io.wav.WavFile;

public class WaveFileTest {

	@Test
	public void testRead() {
	     try
	      {
	    	 // Open the wav file specified as the first argument
	         WavFile wavFile = WavFile.openWavFile(new File("d:/dyplom/probki/system.otworz.normal.wav"));

	         // Display information about the wav file
	         wavFile.display();

	         // Get the number of audio channels in the wav file
	         int numChannels = wavFile.getNumChannels();

	         // Create a buffer of 100 frames
	         double[] buffer = new double[100 * numChannels];

	         int framesRead;
	         double min = Double.MAX_VALUE;
	         double max = Double.MIN_VALUE;

	         do
	         {
	            // Read frames into buffer
	            framesRead = wavFile.readFrames(buffer, 100);

	            // Loop through frames and look for minimum and maximum value
	            for (int s=0 ; s<framesRead * numChannels ; s++)
	            {
	               if (buffer[s] > max) max = buffer[s];
	               if (buffer[s] < min) min = buffer[s];
	            }
	         }
	         while (framesRead != 0);

	         // Close the wavFile
	         wavFile.close();

	         // Output the minimum and maximum value
	         System.out.printf("Min: %f, Max: %f\n", min, max);
	      }
	      catch (Exception e)
	      {
	         System.err.println(e);
	      }
	}
	@Test
	public void testWrite()throws Exception {
         WavFile r = WavFile.openWavFile(new File("d:/dyplom/probki/system.otworz.iza.wav"));
         int sampleRate = (int) r.getSampleRate();    // Samples per second
         int numChannels = 2;
         int validBits = r.getValidBits();

         int addFrames = 5*sampleRate;
         int size = (int) (2 * addFrames + r.getNumFrames());
         double[][] buffer = new double[numChannels][size];

         int frameCounter = 0;
         int max = addFrames;
         while (frameCounter < max)
         {
        	 buffer[0][frameCounter] = Math.sin(2.0 * Math.PI * 400 * frameCounter / sampleRate);
        	 buffer[1][frameCounter] = Math.sin(2.0 * Math.PI * 500 * frameCounter / sampleRate);
        	 frameCounter++;
         }
         
         double[][] buf = new double[1][(int) r.getNumFrames()];
    	 r.readFrames(buf, (int)r.getNumFrames());
    	 for(double bbuf : buf[0]){
    		 buffer[0][frameCounter] = bbuf;
    		 buffer[1][frameCounter] = bbuf;
    		 frameCounter++;
    	 }

    	 max = frameCounter+addFrames;
         while (frameCounter < max)
         {
        	 buffer[0][frameCounter] = Math.sin(2.0 * Math.PI * 500 * frameCounter / sampleRate);
        	 buffer[1][frameCounter] = Math.sin(2.0 * Math.PI * 400 * frameCounter / sampleRate);
        	 frameCounter++;
         }
         r.close();
         File file = new File("output.wav");
         if(file.exists()){
        	 file.delete();
         }
         // Create a wav file with the name specified as the first argument
         WavFile wavFile = WavFile.newWavFile(file, numChannels, max, validBits, sampleRate);

         wavFile.writeFrames(buffer, max);

         // Close the wavFile
         wavFile.close();
      }
}
