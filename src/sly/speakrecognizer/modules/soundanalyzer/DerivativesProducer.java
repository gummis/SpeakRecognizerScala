package sly.speakrecognizer.modules.soundanalyzer;

import java.util.Arrays;

import sly.speakrecognizer.modules.AbstractModule;
import sly.speakrecognizer.modules.Data;
import sly.speakrecognizer.modules.Module;
import sly.speakrecognizer.modules.SoundSamplesData;

public class DerivativesProducer extends AbstractModule implements Translator{

	private double bufCepstrum[][];
	private double bufFirstDeriv[][];
	private int position;
	
	public DerivativesProducer(Module output,Parameters params) {
		super(output);
	}

	/**
	 * position wskazuje na poprzednią biezaca pozycje
	 * gdy nadejdzie dana nalezy go zwiekszyc o jeden
	 * i wrzucic na poczatek bufora nowa probke (position -3) 
	 */
	@Override
	public void putData(Data data) {

		if(output != null){
			output.putData(translate(data));
		}
	}

	@Override
	public Data translate(Data data) {
		SoundSamplesData sdata = (SoundSamplesData) data;
		double[] tab = sdata.getData(0);
		if(bufCepstrum == null){
			bufCepstrum = new double[7][tab.length];
			bufFirstDeriv = new double[7][tab.length];
			for(int x = 0 ; x < 7 ; x++){
				Arrays.fill(bufCepstrum[x], 0.0);
				Arrays.fill(bufFirstDeriv[x], 0.0);
			}
			position = 0 ;
		}else{
			if(bufCepstrum[0].length != tab.length){
				throw new IllegalStateException("Szerokość ramki podanej do modułu fft rózny od poprzedniej");
			}
		}
		
		//zwiekszenie pozycji
		position++;
		position %= 7;
		//wyliczenie pozycji sąsiednich
		//int posM3 = (position-3+7)%7;
		//int posM2 = (position-2+7)%7;
		int posM1 = (position-1+7)%7;
		int posP1 = (position+1)%7;
		//int posP2 = (position+2)%7;
		int posP3 = (position+3)%7;
		//wrzucenie do bufora kolejnej próbki na koniec jako p3
		bufCepstrum[posP3] = tab;
		//wyliczenie pierszej pochodnej dla p1=p3-m1
		double[] t1 = bufCepstrum[posM1];
		double[] t2 = bufFirstDeriv[posP1];
		for(int x = 0 ; x < tab.length ; x++){
			t2[x] = tab[x] - t1[x];
		}
		//ramka wyjściowa: zmienne , pochodna, druga pochodna
		double[] result = new double[tab.length*3];
		
		//kopiowanie danych biezących
		System.arraycopy(bufCepstrum[position], 0, result, 0, tab.length);
		//kopiowanie pierwszej pochodnej
		System.arraycopy(bufFirstDeriv[position], 0, result, tab.length, tab.length);
		//obliczenie drugiej pochodnej
		t1 = bufFirstDeriv[posM1];
		t2 = bufFirstDeriv[posP1];
		int index = tab.length + tab.length;
		for(int x = 0 ; x < tab.length ; x++){
			result[index++] = t2[x] - t1[x];
		}
		return new SoundSamplesData(result,sdata.getFreq());
	}
}
