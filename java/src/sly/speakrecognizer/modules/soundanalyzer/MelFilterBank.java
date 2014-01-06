package sly.speakrecognizer.modules.soundanalyzer;

import java.util.ArrayList;
import java.util.List;

public class MelFilterBank {
	
	private final double freq,minFreq,maxFreq;
	private final int length;
	private final MelFilter[] filters;
	
	public MelFilterBank(int n, int length, double freq,double minFreq,double maxFreq) {
		this.length = length;
		this.freq = freq;
		this.minFreq = minFreq;
		this.maxFreq = maxFreq;
		filters = new MelFilter[n];
		
		/**
		 * rozdzielczość transformaty w HZ
		 */
		double stepFreq = freq / (double)length;
		/**
		 * częstotliwość minimalna w skali mel
		 */
		double minFreqMel = linToMelFreq(minFreq);
		/**
		 * częstotliwość maksymalna w skali mel
		 */
		double maxFreqMel = linToMelFreq(maxFreq);
		/**
		 * roznica miedzy minimalna a maksymalna czestotliwoscia w skali mel
		 */
		double diffFreqMel = maxFreqMel - minFreqMel;
		/**
		 * roznica miedzy dwoma punktami czestotliwosci w skali mel
		 */
		double stepFreqMel = diffFreqMel / (n+1);
		
		/**
		 * lista wyliczanych współczynników filtra
		 * oraz index od którego w którym występuje pierwszy współczynik rózny od zera
		 * czyli poczatek trójkata
		 */
		List<Double> muls = new ArrayList<Double>(n);
		int index;

		//poniewaz medFreq zostanie skopiowane do leftfreq a rigthFreq do medFreq
		//wrzucamy do medFreq minimalna czestotliwosc a do rigthFreq sume minimalnej i jednego skoku
		double leftFreq;
		double medFreq = minFreq;
		double rightFreq = melToLinFreq(minFreqMel +stepFreqMel);

		for(int x = 0 ; x < n ; x++){
			
			//wyliczenie punktów trojkata w skali liniowej
			leftFreq = medFreq;
			medFreq = rightFreq;
			rightFreq = melToLinFreq(minFreqMel + stepFreqMel * (x+2));

			//wyczyszczenie tablicy współczyników
			muls.clear();
			
			//wyliczenie indexu czestotliwosci  ktora jest wieksza od lewej czestoliwosci trojkata
			//czyli indexu pierwszego wspolczynnika roznego od zera
			index = (int) (leftFreq / stepFreq);
			index++;
			
			//róznicze miedzy punktami trojkata
			//srodkowy - lewy
			double diffMedLeftFreq = medFreq - leftFreq;
			//prawy - srodkowy
			double diffRightMedFreq = rightFreq - medFreq;
			
			double val;
			int currIndex = index;
			double indexFreq;
			while((indexFreq = stepFreq * ++currIndex) < rightFreq){
				//jeśli czestotliwość jest juz za prawym zboczem to koniec wyliczania 
				if(indexFreq >= rightFreq)
					break;

				//jeśli częstotliwość dokładnie w zboczu narastającym
				if(indexFreq <= medFreq){
					val = (indexFreq - leftFreq) / diffMedLeftFreq;
				}else{
					//częstoliwość w zboczu opadajacym
					val = (rightFreq - indexFreq) / diffRightMedFreq;
				}
				muls.add(val);
			}
			
			//zamiana listy na tablice
			double[] vals = new double[muls.size()];
			int i = 0;
			for(double v : muls){
				vals[i++] = v;
			}
			filters[x] = new MelFilter(vals, index);
		}
	}

	public int getN() {
		return filters.length;
	}
	
	public double getMinFreq(){
		return minFreq;
	}
	
	public double getFreq(){
		return freq;
	}
	
	public double getMaxFreq(){
		return maxFreq;
	}

	public int getLength(){
		return length;
	}
	
	//---------	
    private double linToMelFreq(double inputFreq) {
        return (2595.0 * (Math.log(1.0 + inputFreq / 700.0) / Math.log(10.0)));
    }

    private double melToLinFreq(double inputFreq) {
        return (700.0 * (Math.pow(10.0, (inputFreq / 2595.0)) - 1.0));
    }

    public double[] filter(double[] data){
    	double[] res = new double[getN()];
    	for(int x = 0 ; x < res.length ; x++){
    		res[x] = filters[x].filter(data);
    	}
    	return res;
    }
}