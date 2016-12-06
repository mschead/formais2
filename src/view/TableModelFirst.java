package view;

import java.util.Map;

import javax.swing.table.DefaultTableModel;

import model.Simbolo;
import model.VEstrela;

public class TableModelFirst extends DefaultTableModel {

	public TableModelFirst(Map<Simbolo, VEstrela> firstTodaGramatica) {
		this.setColumnCount(2);
		
    	for (Simbolo simbolo : firstTodaGramatica.keySet()) {
    		VEstrela firstSimbolo = firstTodaGramatica.get(simbolo);
    		String linha[] = {"First( " + simbolo + " )", firstSimbolo.toString()};
    		this.addRow(linha);
    	}
    	
	}
	
}
