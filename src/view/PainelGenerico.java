package view;

import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import model.Simbolo;
import model.VEstrela;

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
	
	
	
	
	
}
