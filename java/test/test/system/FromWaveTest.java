package test.system;

import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import sly.speakrecognizer.io.wav.WavFile;
import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.SoundSamplesData;
import sly.speakrecognizer.modules.soundanalyzer.DctModule;
import sly.speakrecognizer.modules.soundanalyzer.DerivativesProducer;
import sly.speakrecognizer.modules.soundanalyzer.FftModule;
import sly.speakrecognizer.modules.soundanalyzer.FrameCreator;
import sly.speakrecognizer.modules.soundanalyzer.MelFilterBankModule;
import sly.speakrecognizer.modules.soundanalyzer.Preemphasis;
import sly.speakrecognizer.modules.soundanalyzer.SoundAnalyzer;
import test.system.modules.soundanalyzer.ModuleForTest;

public class FromWaveTest{
	/* TODO: Poprawić: brakuje SoundAnalyzerResult który by wyrzucał rzultaty analizy
	 * do strumienia
	@Test
	public void testAllFromWav()throws Exception {
		//otworzenie wejsciowego pliku waw
		
	   // WavFile wavFile = WavFile.openWavFile(new File("d:/dyplom/probki/system.otworz.normal.wav"));
	    WavFile wavFile = WavFile.openWavFile(new File("d:/dyplom/probki/silent_16khz.wav"));
	    //utworzenie �r�d�a d�wi�ku (z pliku)
	    SoundSource source = new WavSoundSource(wavFile);
	    //utworzenie modu�u analizuj�cego d�wi�k
	    FileOutputStream stream = new FileOutputStream("analyzer_result.txt");
		Module moduleAnalyzer = new SoundAnalyzer(new SoundAnalyzerResult(stream), null);
		//uruchomienie strumienia ze �r�d�a d�wieku
		new FlowController(source,moduleAnalyzer).resume();
	}*/

	@Test
	public void testSilent()throws Exception{

		//wygenerowanie ciszy
		//16kHz próbkowanie
		//czas trwania zeby starczył na 3 ramki
		//czas = ((1*10ms + 2*7.5) + dopelnienie do ^2) +2 *10
		//czas 25*16=400 + dop^2 =512 + 2*10*16=320 = 832
		double tab[] = new double[832];
		Arrays.fill(tab,0.0);
		test(tab,16000,new FileOutputStream("d:/dyplom/probki/analiza/testSilent_raport.txt"));
	}
	
	
	@Test
	public void testSoundAnalyzerModules()throws Exception{
		//cisza
		test("d:/dyplom/probki/silent_44khz.wav");
		test("d:/dyplom/probki/sinus_100Hz.wav");
		test("d:/dyplom/probki/sinus_250Hz.wav");
		test("d:/dyplom/probki/sinus_500Hz.wav");
		test("d:/dyplom/probki/sinus_1000Hz.wav");
		test("d:/dyplom/probki/sinus_2000Hz.wav");
		test("d:/dyplom/probki/sinus_4000Hz.wav");
		test("d:/dyplom/probki/sinus_7000Hz.wav");
		test("d:/dyplom/probki/aaaa.wav");
		test("d:/dyplom/probki/eeee.wav");
		test("d:/dyplom/probki/iiii.wav");
		test("d:/dyplom/probki/oooo.wav");
		test("d:/dyplom/probki/uuuu.wav");
		test("d:/dyplom/probki/yyyy.wav");
		test("d:/dyplom/probki/nie.wav");
		test("d:/dyplom/probki/tak.wav");

		test("d:/dyplom/probki/alpha_sly.wav");
		test("d:/dyplom/probki/bravo_sly.wav");
		test("d:/dyplom/probki/charlie_sly.wav");
		test("d:/dyplom/probki/delta_sly.wav");
		test("d:/dyplom/probki/echo_sly.wav");
		test("d:/dyplom/probki/foxtrot_sly.wav");
		test("d:/dyplom/probki/golf_sly.wav");
		test("d:/dyplom/probki/hotel_sly.wav");
		test("d:/dyplom/probki/india_sly.wav");
		test("d:/dyplom/probki/juliet_sly.wav");
		test("d:/dyplom/probki/kilo_sly.wav");
		test("d:/dyplom/probki/lima_sly.wav");
		test("d:/dyplom/probki/mike_sly.wav");
		test("d:/dyplom/probki/november_sly.wav");
		test("d:/dyplom/probki/oscar_sly.wav");
		test("d:/dyplom/probki/papa_sly.wav");
		test("d:/dyplom/probki/quebec_sly.wav");
		test("d:/dyplom/probki/romeo_sly.wav");
		test("d:/dyplom/probki/sierra_sly.wav");
		test("d:/dyplom/probki/tango_sly.wav");
		test("d:/dyplom/probki/uniform_sly.wav");
		test("d:/dyplom/probki/victor_sly.wav");
		test("d:/dyplom/probki/whiskey_sly.wav");
		test("d:/dyplom/probki/x-ray_sly.wav");
		test("d:/dyplom/probki/yankee_sly.wav");
		test("d:/dyplom/probki/zulu_sly.wav");
	}
	
	private void test(String path)throws Exception{
		File inputFile = new File(path);
		File outputFile = new File(inputFile.getParentFile(),"analiza/" + inputFile.getName().replace(".wav", ".txt"));
		if(!inputFile.exists() || outputFile.exists())
			return;
		WavFile wavFile = WavFile.openWavFile(inputFile);
		double[][] buffer = new double[wavFile.getNumChannels()][(int) wavFile.getFramesRemaining()];
		wavFile.readFrames(buffer, (int)wavFile.getFramesRemaining());
		OutputStream stream = new FileOutputStream(outputFile);
		test(buffer[0],(int) wavFile.getSampleRate(),stream);
		stream.close();
	}
	
	private void test(double[] tab,double freq,OutputStream out)throws Exception{
		long time = 0;
		Random r = new Random(System.currentTimeMillis());
		ModuleForTest module = new ModuleForTest();
		//przetestowanie Frame Creatora
		//powinien wytworzyć trzy ramki wypełnione zerami
		PrintStream pout = new PrintStream(out);
		/**********************************************************
		 * ********* FRAME CREATOR ********************************
		 * ********************************************************
		 */
		pout.println("***********************************************************************************************************");
		pout.println("1. Testowanie Frame Creatora");
		FrameCreator frameCreator = new FrameCreator(module,null);
		//wyslanie z nadmiarem 500 próbek
		double[] temp = new double[520];
		System.arraycopy(tab, 0, temp, 0, 520);
		long prev = System.currentTimeMillis();
		frameCreator.putData(new SoundSamplesData(temp,freq));
		//szczątkowe wysylanie danych
		int left = tab.length - 520;
		while(left > 0){
			int size = r.nextInt(20) + 1;
			if(size > left)
				size = left;
			temp = new double[size];
			System.arraycopy(tab, tab.length - left, temp, 0, size);
			left -= size;
			frameCreator.putData(new SoundSamplesData(temp,freq));
		}
		long next = System.currentTimeMillis();
		List<Data> results = module.clearAndReturn();
		time += next-prev;
		printArrayOneChannel(results,pout,freq,next-prev,16);
		/**********************************************************
		 * ********* PREEMPHASIS **********************************
		 * ********************************************************
		 */
		pout.println("***********************************************************************************************************");
		pout.println("2. Testowanie Preemfazy");
		Preemphasis preemphasis = new Preemphasis(module,null);
		prev = System.currentTimeMillis();
		//wysłanie próbek
		for(Data data : results){
			preemphasis.putData(data);
		}
		next = System.currentTimeMillis();
		results = module.clearAndReturn();
		time += next-prev;
		printArrayOneChannel(results,pout,freq,next-prev,16);
		/**********************************************************
		 * ********* FFT ******************************************
		 * ********************************************************
		 */
		pout.println("***********************************************************************************************************");
		pout.println("3. Testowanie FFT");		
		FftModule fft = new FftModule(module,null);
		prev = System.currentTimeMillis();
		//wysłanie próbek
		for(Data data : results){
			fft.putData(data);
		}
		next = System.currentTimeMillis();
		results = module.clearAndReturn();
		time += next-prev;
		printArrayOneChannel(results,pout,freq,next-prev,16);
		/**********************************************************
		 * ********* Mel Filter Bank ******************************
		 * ********************************************************
		 */
		pout.println("***********************************************************************************************************");
		pout.println("4. Testowanie Banku Filtrów Mel");		
		MelFilterBankModule mfbm = new MelFilterBankModule(module,null);
		prev = System.currentTimeMillis();
		//wysłanie próbek
		for(Data data : results){
			mfbm.putData(data);
		}
		next = System.currentTimeMillis();
		results = module.clearAndReturn();
		time += next-prev;
		printArrayOneChannel(results,pout,freq,next-prev,16);
		/**********************************************************
		 * ********* DCT ******************************************
		 * ********************************************************
		 */
		pout.println("***********************************************************************************************************");
		pout.println("5. Testowanie DCT");		
		DctModule dct = new DctModule(module,null);
		prev = System.currentTimeMillis();
		//wysłanie próbek
		for(Data data : results){
			dct.putData(data);
		}
		next = System.currentTimeMillis();
		results = module.clearAndReturn();
		time += next-prev;
		printArrayOneChannel(results,pout,freq,next-prev,16);
		/**********************************************************
		 * ********* DerivativesProducer **************************
		 * ********************************************************
		 */
		pout.println("***********************************************************************************************************");
		pout.println("6. Testowanie DerivativesProducer");		
		DerivativesProducer dp = new DerivativesProducer(module,null);
		prev = System.currentTimeMillis();
		//wysłanie próbek
		for(Data data : results){
			dp.putData(data);
		}
		next = System.currentTimeMillis();
		results = module.clearAndReturn();
		time += next-prev;
		int lengthCoef = ((SoundSamplesData)results.get(0)).getData(0).length/3;
		printArrayOneChannel(results,pout,freq,next-prev,lengthCoef);
		pout.println("***********************************************************************************************************");
		pout.println("PODSUMOWANIE");		
		pout.println("Łaczny czas: " + time + "ms");
		double proc = time * 100;
		proc /= results.size();
		proc = Math.round(proc);
		proc /= 10.0;
		pout.println("Efektywność kodu: " + proc + "%");
		//============================================
		//Porównanie wyniku z wynikiem sound analyzera
		SoundAnalyzer analyzer = new SoundAnalyzer(module,null);
		analyzer.putData(new SoundSamplesData(tab,freq));
		List<Data> resultsA = module.clearAndReturn();
		//porównanie
		
		Assert.assertEquals("Rozmiary list wyników nierówne.",results.size(), resultsA.size());
		for(int x = 0 ; x < results.size() ; x++){
			SoundSamplesData data = (SoundSamplesData) results.get(x);
			SoundSamplesData dataA = (SoundSamplesData) resultsA.get(x);
			Assert.assertEquals("Częstotliwości ramek nierówne.",data.getFreq(),dataA.getFreq(),0.0);
			assertArrayEquals(data.getData(0), dataA.getData(0),0.0);
		}
		//============================================		
	}


	private void printArrayOneChannel(List<Data> results, PrintStream pout,double freq, long time,int grouping) {
		SoundSamplesData res;
		pout.println("Czas liczenia: " + time + "ms");
		pout.println("Moduł wygenerował " + results.size() + " ramek");
		for(int x = 0 ; x < results.size() ; x++){
			pout.println("===========================================================================================================");
			res = (SoundSamplesData) results.get(x);
			Assert.assertEquals(freq,res.getFreq(),0.0);
			double[] temp = res.getData(0);
			pout.println("Ramka nr " + (x + 1) + " ma " + temp.length + " próbek. Zawartość:");
			int count = 0 ;
			int countLine = 0;
			int left = temp.length;
			while(left > 0){
				int leftInt = grouping;
				StringBuilder sb = new StringBuilder().append("Linia ").append(countLine++).append(":  ");
				while(left > 0 && leftInt > 0){
					sb.append(temp[count++]);
					left--;leftInt--;
					sb.append(" , ");
				}
				pout.println(sb.toString());
			}
			//pout.println("Koniec zawartości ramki nr " + (x + 1));
		}		
	}
}
