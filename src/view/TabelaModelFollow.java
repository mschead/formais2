package view;

import java.util.Map;

import javax.swing.table.DefaultTableModel;

import model.Simbolo;
import model.VEstrela;

public class TabelaModelFollow extends DefaultTableModel{

	public TabelaModelFollow(Map<Simbolo, VEstrela> followTodaGramatica) {
		this.setColumnCount(2);
		
    	for (Simbolo simbolo : followTodaGramatica.keySet()) {
    		VEstrela followSimbolo = followTodaGramatica.get(simbolo);
    		String linha[] = {"Follow( " + simbolo + " )", followSimbolo.toString()};
    		this.addRow(linha);
    	}
    	
	}
	
}
