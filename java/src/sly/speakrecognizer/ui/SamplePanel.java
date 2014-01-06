package sly.speakrecognizer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import be.ac.ulg.montefiore.run.jahmm.ObservationVector;

public class SamplePanel extends JPanel implements SounderListener {

	private JButton recordButton;
	private JButton playButton;
	private JButton stopButton;
	private JButton addButton;
	private JButton clrButton;
	private JCheckBox silentBox;
	private JTextField silentLevelInput;
	private JLabel toolbarLabel;
	private ChartPanel chartPanel;
	private Microphone microphone;
	private Sounder sounder;
	private SampleData sdata;
	private boolean rec;
	private List<List<ObservationVector>> observations;
	private RecognizeModel model;

	
	public SamplePanel(RecognizeModel model){
		super(new BorderLayout());
		this.model = model;
		microphone = new Microphone();
		microphone.addSounderListener(this);
		sounder = new Sounder();
		sounder.addSounderListener(this);
		observations = new ArrayList<List<ObservationVector>>();
		ini();
	}

	private void ini() {
		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		recordButton = new JButton("Nagrywanie");
		recordButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					rec = true;
					microphone.startRecording();
					setEnabledButtons();
				}catch(Exception ex){
					JOptionPane.showMessageDialog(SamplePanel.this,ex.getMessage(),"Błąd",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		playButton = new JButton("Odtwarzanie");
		playButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					rec = false;
					sounder.setData(getSampleData().getRawData());
					sounder.startPlaying();
					setEnabledButtons();
				}catch(Exception ex){
					JOptionPane.showMessageDialog(SamplePanel.this,ex.getMessage(),"Błąd",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					if(microphone.isRecording()){
						microphone.stopRecording();
					}else{
						sounder.stopPlaying();
					}
					setEnabledButtons();
				}catch(Exception ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(SamplePanel.this,ex.getMessage(),"Błąd",JOptionPane.ERROR_MESSAGE);
				}
			}
		});		
		silentBox = new JCheckBox("Obcięcie ciszy");
		silentBox.addChangeListener(new ChangeListener(){
			boolean state = silentBox.isSelected();
			public void stateChanged(ChangeEvent e) {
				if(state != silentBox.isSelected()){
					state = silentBox.isSelected();
					refresh();
				}
			}
		});
		addButton = new JButton();
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				getSequence();
				observations.add(getSequence());
				sdata = null;
				chartPanel.setChart(null);
				setEnabledButtons();
			}
		});
		clrButton = new JButton("Wyczyść próbki");
		clrButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				observations.clear();
				setEnabledButtons();
			}
		});

		silentLevelInput = new JTextField();
		silentLevelInput.setText("1.0");
		silentLevelInput.setPreferredSize(new Dimension(50,20));

		setEnabledButtons();
		toolbarLabel = new JLabel();
		toolbar.add(playButton);
		toolbar.add(recordButton);
		toolbar.add(stopButton);
		toolbar.add(silentBox);
		toolbar.add(silentLevelInput);
		toolbar.add(addButton);
		toolbar.add(clrButton);
		toolbar.add(toolbarLabel);
		chartPanel = new ChartPanel(null);
		
		
		add(chartPanel,BorderLayout.CENTER);
		add(toolbar,BorderLayout.SOUTH);
		setEnabledButtons();
	}

	protected List<ObservationVector> getSequence() {
		return getSequence(getSampleData());
	}
	
	private List<ObservationVector> getSequence(SampleData sdata) {
		SampleFrameData[] sfda = sdata.getSampleFrameData();
		List<ObservationVector> list = new ArrayList<ObservationVector>(sfda.length);
		for(SampleFrameData sfd : sfda){
			list.add(new ObservationVector(sfd.getDrp()));
		}
		return list;
	}

	protected void setEnabledButtons() {
		boolean busy = sounder.isPlaying() || microphone.isRecording() ;
		recordButton.setEnabled( !busy && microphone.isReady());
		playButton.setEnabled(!busy && sounder.isReady() && sdata != null);
		stopButton.setEnabled(busy);
		addButton.setText("Dodaj próbkę (" + observations.size() + ")");
		addButton.setEnabled(playButton.isEnabled());
		clrButton.setEnabled(observations.size() > 0);
	}

	protected void setDataAndRefresh(double[] data) {
		if(data == null || data.length==0){
			sdata = null;
			chartPanel.setChart(null);
			return;
		}
		sdata = new SampleData(data);
		refresh();
	}


	private void refresh() {
		SampleData sdata = getSampleData();
		JFreeChart chart = createChart(sdata);
		chartPanel.setChart(chart);
		if(chart==null){toolbarLabel.setText("");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Ilość ramek: ");
		sb.append(sdata.getSampleFrameData().length);
		sb.append(", Czas trwania: ");
		DecimalFormat format = (DecimalFormat) DecimalFormat.getIntegerInstance();
		sb.append(format.format(sdata.getRawData().length/8));
		sb.append(" ms");
		
		List<ObservationVector> sequence = getSequence(sdata);
		String recword = model.recognize(sequence);
		sb.append(" Rozpoznawane słowo: ");
		sb.append(recword);
		
		toolbarLabel.setText(sb.toString());
	}

	private SampleData getSampleData() {
		if(sdata == null){
			return null;
		}
		if(!silentBox.isSelected()){
			return sdata;
		}
		//obcięcie ciszy
		double silentMax = 0.0;
		try{
			silentMax = new BigDecimal(silentLevelInput.getText()).doubleValue();
		}catch(Exception e){
		}
		int begin = 0;
		while(begin < sdata.getSampleFrameData().length){
			SampleFrameData sfd = sdata.getSampleFrameData()[begin++];
			if(sfd.getDct()[0] > silentMax){
				begin--;
				break;
			}
		}
	
		int end = sdata.getSampleFrameData().length-1;
		while(end > -1){
			SampleFrameData sfd = sdata.getSampleFrameData()[end--];
			if(sfd.getDct()[0] > silentMax){
				end++;
				break;
			}
		}
		
		if(begin >= end){
			return sdata;
		}
		
		int rbegin = 136 * begin;
		int rend = 136 * end + 256;

		if(rbegin >= rend){
			return sdata;
		}
		
		double[] raw = Arrays.copyOfRange(sdata.getRawData(), rbegin, rend);
		SampleFrameData[] newsdf = Arrays.copyOfRange(sdata.getSampleFrameData(), begin, end+1);
		SampleData sd = new SampleData(raw);
		sd.setSampleFrameData(newsdf);
		return sd;
	}

	private JFreeChart createChart(SampleData sdata) {
		if(sdata == null){
			return null;
		}
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
       //dodanie wartości
        int index = 0;
    	Boolean comparable = new Boolean(false);
        for(SampleFrameData sdf : sdata.getSampleFrameData()){
            dataset.addValue(sdf.getDct()[0], comparable, new Integer(index));
            index++;
        }

       //tworzenie wykresu:
       JFreeChart chart = ChartFactory.createBarChart
               ("Energia Ramek Sygnału",
               "Ramki",
               "Energia",
               dataset,
               PlotOrientation.VERTICAL,
               false,
               false,
               false);

       //kolory wewnętrzne

       CategoryPlot plot = (CategoryPlot) chart.getPlot();
       plot.setBackgroundPaint(Color.white);
       plot.setRangeGridlinePaint(Color.red);

       //kolory słupków
       BarRenderer barrenderer = (BarRenderer) plot.getRenderer();
       barrenderer.setSeriesPaint(0, new Color(0,0,150));
       barrenderer.setDrawBarOutline(false);
       //odległóść pomiędzy słupkami
       barrenderer.setItemMargin(0.0);
       return chart;
	}



	public void stopped() {
		if(rec){
			setDataAndRefresh(microphone.getData());
		}
		setEnabledButtons();
	}

	public void clear() {
		sdata = null;
		observations.clear();
		chartPanel.setChart(null);
		setEnabledButtons();
		toolbarLabel.setText("");
	}

	public List<List<ObservationVector>> getObservations() {
		return observations;
	}

}
