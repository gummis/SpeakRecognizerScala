package sly.speakrecognizer.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class LearnPanel extends JPanel {

	private RecognizeModel model;

	private JComboBox<String> list;
	private SamplePanel samplePanel;
	
	public LearnPanel(String[] args) {
		super(new BorderLayout());
		model = new RecognizeModel();
		//lista słow
		JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		add(toolBar,BorderLayout.NORTH);
		JLabel label = new JLabel("Lista słów w słowniku: ");
		list = new JComboBox<String>(new ListNamesWordsComboModel(model));
		list.setPreferredSize(new Dimension(200,20));
		JButton newModButton = new JButton("Nowy słownik");
		JButton loadModButton = new JButton("Otwórz słownik");
		JButton saveModButton = new JButton("Zapisz słownik");
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		JButton newButton = new JButton("Nowe słowo");
		JButton delButton = new JButton("Usuń słowo");
		JButton learnButton = new JButton("Trenuj");
		
		toolBar.add(newModButton);
		toolBar.add(loadModButton);
		toolBar.add(saveModButton);
		toolBar.add(sep);

		toolBar.add(label);
		toolBar.add(list);
		toolBar.add(newButton);
		toolBar.add(delButton);
		toolBar.add(learnButton);

		//=============
		newModButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				model.clear();
				list.setModel(new ListNamesWordsComboModel(model));
				samplePanel.clear();
			}
		});
		//=============
		loadModButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//Create a file chooser
				JFileChooser fc = getFileChooser();
				fc.setApproveButtonText("Załaduj");
				fc.setDialogTitle("Ładowanie pliku słownika...");

				//In response to a button click:

				if(fc.showOpenDialog(MainFrame.instance) == JFileChooser.APPROVE_OPTION){
					model.loadFromFile(fc.getSelectedFile());
					list.setModel(new ListNamesWordsComboModel(model));
					samplePanel.clear();
				}
			}
		});
		//=============
		saveModButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//Create a file chooser
				JFileChooser fc = getFileChooser();
				fc.setApproveButtonText("Zapisz");
				fc.setDialogTitle("Zapisanie pliku słownika...");
				if(fc.showSaveDialog(MainFrame.instance) == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					if(!file.getName().endsWith(".srd")){
						file = new File(file.getParentFile(),file.getName() +".srd");
					}
					model.saveToFile(file);
				}
			}
		});
		//=============
		newButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("Podaj nazwę nowego słowa");
				if(name != null){
					try {
						model.newWord(name,0);
						refresh(name);
					} catch (RecognizeModelException ex) {
						JOptionPane.showMessageDialog(LearnPanel.this,ex.getMessage(),"Błąd",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		//=============
		delButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					Object o = list.getSelectedItem();
					if(o == null)
						throw new RecognizeModelException("Wybierz słowo z listy które chcesz usunąć");					
					String name = o.toString();
					model.removeWord(name);
				} catch (RecognizeModelException ex) {
					JOptionPane.showMessageDialog(LearnPanel.this,ex.getMessage(),"Błąd",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		//=============
		learnButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				model.learn(list.getSelectedItem().toString(),samplePanel.getObservations());
				samplePanel.clear();
			}
		});
		//=====================
		samplePanel = new SamplePanel(model);
		add(samplePanel,BorderLayout.CENTER);
		//=====================
		
	}

	protected JFileChooser getFileChooser() {
		JFileChooser fc = new JFileChooser("c:");
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		
		fc.addChoosableFileFilter(new FileFilter() {
			public String getDescription() {
				return "Plik słownika *.srd";
			}

			public boolean accept(File f) {
				return !f.isFile() ||
						f.getName().endsWith(".srd");
			}
		});
		return fc;
	}

	protected void refresh(String name) {
		list.setSelectedItem(name);
	}

}
