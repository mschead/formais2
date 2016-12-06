package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Gramatica;
import model.Simbolo;
import model.VEstrela;

public class GramaticaParser {

	
	public static Gramatica textToGramatica(String gramatica) {
		Gramatica gramaticaModelo = new Gramatica();
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		
		boolean inicialAdicionado = false;
		List<String> linhas = Arrays.asList(gramatica.split("\n"));
		for (String alfa : linhas) {
			List<Simbolo> simbolosObtidos = new ArrayList<>();
			Set<VEstrela> ladoDireito = new HashSet<>();
			Simbolo naoTerminal = null;
			
			List<String> simbolos = Arrays.asList(alfa.split(" "));
			for (int index = 0; index < simbolos.size(); index++) {
				String simbolo = simbolos.get(index);
				if (index == 0) {
					naoTerminal = new Simbolo(simbolo, false);
					simbolosNaoTerminais.add(naoTerminal);
					if (!inicialAdicionado) {
						gramaticaModelo.setSimboloInicial(naoTerminal);
					}
				}
				
				if (index == 1) {
				}
				
				if (index > 1) {
					if(!simbolo.equals("|")) {
						boolean isTerminal = true;
						Simbolo simboloParaGramatica;
						if (simbolo.matches("[A-Z][0-9]*")) {
							simboloParaGramatica = new Simbolo(simbolo, false);
							simbolosNaoTerminais.add(simboloParaGramatica);
						} else {
							simboloParaGramatica = new Simbolo(simbolo, true);
							simbolosTerminais.add(simboloParaGramatica);
						}
						simbolosObtidos.add(new Simbolo(simbolo, isTerminal));
					} else {
						ladoDireito.add(new VEstrela(simbolosObtidos));
						simbolosObtidos = new ArrayList<>();
					}
					
					if (index == simbolos.size() - 1) {
						ladoDireito.add(new VEstrela(simbolosObtidos));
						producoes.put(naoTerminal, ladoDireito);
					}
					
					
					
				}  
				
			}
				
		}
		
		
		return gramaticaModelo;
	}

}
