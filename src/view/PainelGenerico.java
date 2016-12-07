package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PainelGenerico extends JFrame {

	private JTable tabela;
	private JScrollPane scrollPane;
	
	public PainelGenerico(DefaultTableModel model)	{
		tabela = new JTable(model);
		scrollPane = new JScrollPane(tabela);
		iniciar();
	}
	

	public void iniciar() {
		this.setTitle("Resultado");
		this.setSize(500, 500);
//        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        
        getContentPane().add(scrollPane);
        
	}
	
	
	public void habilitarBotaoAnalise() {
		JButton analisar = new JButton("Analisar sentença");
		getContentPane().add(analisar);
	}
	
	
	
	
}
