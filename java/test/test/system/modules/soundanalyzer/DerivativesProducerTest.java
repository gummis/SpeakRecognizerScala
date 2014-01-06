package test.system.modules.soundanalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.SoundSamplesData;
import sly.speakrecognizer.modules.soundanalyzer.DerivativesProducer;

public class DerivativesProducerTest {

	@Test
	public void test(){
		int countOfTests = 1000;
		int flankSize = 3;
		int windowSize = flankSize * 2 + 1;
		int length = 10;
		//kolejka do porównania
		List<double[]> list = new ArrayList<double[]>();
		//wypełnienia zerami kolejki (czyli ciszą)
		for(int x = 0 ; x < windowSize ; x++){
			double[] t = new double[length * 3];
			Arrays.fill(t,0.0);
			list.add(t);
		}
		ModuleForTest module = new ModuleForTest();
		DerivativesProducer producer = new DerivativesProducer(module,null);
		//-------------------------------------------------------------
		Random r = new Random(System.currentTimeMillis());
		for(int x = 0; x < countOfTests ; x++){
			double probka[] = new double[length];
			//wygenerowanie próbki
			for(int p = 0 ; p < length ; p++){
				probka[p] = r.nextDouble();
			}
			//wyrzucenie ostatniej próbki z kolejki
			list.remove(0);
			//dodanie nowej
			list.add(probka);
			//wyliczenie tablicy do porównania
			double[] compare = new double[length*3];
			//skopiowanie srodkowej wartosci
			System.arraycopy(list.get(3), 0, compare, 0, length);
			//wyliczenie pierwszej pochodnej
			double[] tempM2 = list.get(3-2);
			double[] tempP2 = list.get(3+2);
			for(int y = 0 ; y < length ; y++){
				compare[y + length] = tempP2[y] - tempM2[y];
			}
			//wyliczenie drugiej pochodnej
			double[] tempM3 = list.get(3-3);
			double[] tempM1 = list.get(3-1);
			double[] tempP3 = list.get(3+3);
			double[] tempP1 = list.get(3+1);			
			for(int y = 0 ; y < length ; y++){
				compare[y + length + length] = (tempP3[y] - tempM1[y]) - (tempP1[y] - tempM3[y]);
			}
			//wygenerowanie wyniku do z badanego obiektu
			//wrzucenie probki do badanego obiektu
			producer.putData(new SoundSamplesData(probka,8000));
			//pobranie wyniku
			List<Data> results = module.clearAndReturn();
			Assert.assertEquals(1, results.size());
			double[] res = ((SoundSamplesData)results.get(0)).getData(0);

			//porównanie
			org.junit.Assert.assertArrayEquals(compare,res,0.0);
		}
	}
}
