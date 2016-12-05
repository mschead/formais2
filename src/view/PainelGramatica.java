package view;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class PainelGramatica extends JPanel {
	
	public PainelGramatica() {
		JTextArea campoGramatica = new JTextArea();
		campoGramatica.setEditable(true);
		campoGramatica.setSize(200, 200);
		this.add(campoGramatica);
	}

}
