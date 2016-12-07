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
		Map<Simbolo, List<VEstrela>> producoes = new HashMap<>();
		int PRODUCAO = 1;
		
		boolean inicialAdicionado = false;
		List<String> linhas = Arrays.asList(gramatica.split("\n"));
		for (String alfa : linhas) {
			List<Simbolo> simbolosObtidos = new ArrayList<>();
			List<VEstrela> ladoDireito = new ArrayList<>();
			Simbolo naoTerminal = null;
			
			List<String> simbolos = Arrays.asList(alfa.split(" "));
			for (int index = 0; index < simbolos.size(); index++) {
				String simbolo = simbolos.get(index);
				if (index == 0) {
					naoTerminal = new Simbolo(simbolo, false);
					simbolosNaoTerminais.add(naoTerminal);
					if (!inicialAdicionado) {
						gramaticaModelo.setSimboloInicial(naoTerminal);
						inicialAdicionado = true;
					}
				}
				
				if (index > 1) {
					if(!simbolo.equals("|")) {
						Simbolo simboloParaGramatica = null;
						if (simbolo.matches("[A-Z][0-9]*")) {
							simboloParaGramatica = new Simbolo(simbolo, false);
							simbolosNaoTerminais.add(simboloParaGramatica);
							simbolosObtidos.add(simboloParaGramatica);
						} else if (!simbolo.equals("&")) {
							simboloParaGramatica = new Simbolo(simbolo, true);
							simbolosTerminais.add(simboloParaGramatica);
							simbolosObtidos.add(simboloParaGramatica);
						} else if (simbolo.equals("&")) {
							simbolosObtidos.add(Simbolo.EPSILON);
						}
					} else {
						ladoDireito.add(new VEstrela(PRODUCAO++, simbolosObtidos));
						simbolosObtidos = new ArrayList<>();
					}
					
					if (index == simbolos.size() - 1) {
						ladoDireito.add(new VEstrela(PRODUCAO++, simbolosObtidos));
						producoes.put(naoTerminal, ladoDireito);
					}
				}  
			}
		}
		
		gramaticaModelo.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramaticaModelo.setSimbolosTerminais(simbolosTerminais);
		gramaticaModelo.setProducoes(producoes);
		return gramaticaModelo;
	}

	
	public static String gramaticaToText(Gramatica gramatica) {
		StringBuilder g = new StringBuilder();
		Map<Simbolo, List<VEstrela>> producoes = gramatica.getProducoes();
		
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			List<VEstrela> ladoDireito = new ArrayList<>(producoes.get(ladoEsquerdo));
			g.append(ladoEsquerdo + " -> ");
			for (int index = 0; index < ladoDireito.size(); index++) {
				VEstrela alfa = ladoDireito.get(index);
				g.append(alfa);
				if (ladoDireito.size() - 1 != index) {
					g.append(" | ");
				}
			}
			g.append("\n");
		}
		return g.toString();
	}
	
}
