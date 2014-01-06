package sly.speakrecognizer.ui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class MainFrame extends JFrame {
	
	public static MainFrame instance;
	public MainFrame(String[] args) {
		super();
		ini(args);
	}

	private void ini(String[] args) {
		setTitle("Program trenujący system rozpoznawania mowy. wersja 0.01");
		try{
	        UIManager.setLookAndFeel(
	                UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){}
		JTabbedPane tpane = new JTabbedPane();
		RecognizePanel rpanel = new RecognizePanel(args);
		LearnPanel lpanel = new LearnPanel(args);
		
		tpane.addTab("Rozpoznawanie", null, rpanel, "Testowanie modelu rozpoznającego.");
		tpane.addTab("Trenowanie", null, lpanel, "Uczenie modelu rozpoznającego.");
		add(tpane);
		setPreferredSize(new Dimension(800,600));
		this.setLocationRelativeTo(null);
		this.pack();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
	}

	public static void main(String[] args){
		instance = new MainFrame(args);
		instance.setVisible(true);
	}
}
