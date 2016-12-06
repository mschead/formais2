package view;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import model.AlfaNumero;
import model.Simbolo;

public class TabelaModelParsing extends DefaultTableModel {

	public TabelaModelParsing(Map<Simbolo, List<AlfaNumero>> parsing, Set<Simbolo> terminais) {
		this.setNumRows(parsing.keySet().size());
		this.addColumn("#");
		for (Simbolo simbolo : terminais)
			this.addColumn(simbolo.toString());
		this.addColumn("$");
		
		
		int numeroLinha = 0;
		for (Simbolo simbolo : parsing.keySet()) {
			this.setValueAt(simbolo.toString(), numeroLinha, 0);
			List<AlfaNumero> alfaNumero = parsing.get(simbolo);
			for (AlfaNumero simbolos : alfaNumero) {
				int numeroProducao = simbolos.getNumero();
				for (Simbolo terminal : simbolos.getAlfa().getSimbolos()) {
					int coluna = this.findColumn(terminal.toString());
					this.setValueAt(numeroProducao, numeroLinha, coluna);
				}
			}
			numeroLinha++;
		}
		
	}
	
}
