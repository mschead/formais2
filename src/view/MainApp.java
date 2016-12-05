package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class MainApp extends JFrame {

	public MainApp() {
		super.setTitle("Formais");	
		
		this.getContentPane().add(new JTextArea());
		
	}
	
	public static void main(String[] args) {
	     	MainApp janela = new MainApp();
	     	janela.setSize(500, 500);
	        janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//	        janela.pack();
	        janela.setVisible(true);	
	}
	
	
}
