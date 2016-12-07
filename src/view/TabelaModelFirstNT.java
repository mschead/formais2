package view;

import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import model.Simbolo;

public class TabelaModelFirstNT extends DefaultTableModel {

	public TabelaModelFirstNT(Map<Simbolo, List<Simbolo>> firstNT) {
		this.setColumnCount(2);
		
    	for (Simbolo simbolo : firstNT.keySet()) {
    		List<Simbolo> firstNTSimbolo = firstNT.get(simbolo);
    		String linha[] = {"FirstNT( " + simbolo + " )", firstNTSimbolo.toString()};
    		this.addRow(linha);
    	}
	}
	
}
