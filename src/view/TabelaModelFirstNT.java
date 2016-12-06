package view;

import java.util.Map;

import javax.swing.table.DefaultTableModel;

import model.Simbolo;
import model.VEstrela;

public class TabelaModelFirstNT extends DefaultTableModel {

	public TabelaModelFirstNT(Map<Simbolo, VEstrela> firstNT) {
		this.setColumnCount(2);
		
    	for (Simbolo simbolo : firstNT.keySet()) {
    		VEstrela firstNTSimbolo = firstNT.get(simbolo);
    		String linha[] = {"FirstNT( " + simbolo + " )", firstNTSimbolo.toString()};
    		this.addRow(linha);
    	}
	}
	
}
