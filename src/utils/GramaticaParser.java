package utils;

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
//			List<String> linhas = Files.lines(Paths.get("/home/marcos/github/formais2/src/utils/teste.txt")).collect(Collectors.toList());
//			String[] simbolos = linhas.get(0).split("\n->|\\|");
//			
//			for (String s : simbolos)
//				System.out.print(s.trim());
//			
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		List<String> linhas = Arrays.asList(gramatica.split("\n"));
		
		for (int index = 0; index < linhas.size(); index++) {
			List<String> simbolos = Arrays.asList(linhas.get(index).split("->|\\|"));
			for (int j = 0; j < simbolos.size(); j++) {
				String simbolo = simbolos.get(0);
				if (index == 0) {
					simbolosNaoTerminais.add(new Simbolo(simbolo, false));
				} else {
//					simbolo.spl
				}
			}
			
		}
		
		
		return null;
	}

}
