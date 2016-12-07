package view;

<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> 98b0bad4dddd4f1eea86e09237a0432f9aad37ee
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import model.Simbolo;

public class TabelaModelFirstNT extends DefaultTableModel {

<<<<<<< HEAD
	public TabelaModelFirstNT(Map<Simbolo, List<Simbolo>> firstNT) {
		this.setColumnCount(2);
		
    	for (Simbolo simbolo : firstNT.keySet()) {
    		List<Simbolo> firstNTSimbolo = firstNT.get(simbolo);
=======
	public TabelaModelFirstNT(Map<Simbolo, Set<Simbolo>> firstNT) {
		this.setColumnCount(2);
		
    	for (Simbolo simbolo : firstNT.keySet()) {
    		List<Simbolo> l = new ArrayList<>();
    		l.addAll(firstNT.get(simbolo));
 
    		VEstrela firstNTSimbolo = new VEstrela();
    		firstNTSimbolo.setSimbolos(l);
>>>>>>> 98b0bad4dddd4f1eea86e09237a0432f9aad37ee
    		String linha[] = {"FirstNT( " + simbolo + " )", firstNTSimbolo.toString()};
    		this.addRow(linha);
    	}
	}
	
}
