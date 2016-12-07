package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Gramatica {

	private Set<Simbolo> simbolosTerminais = new HashSet<>();
	private Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
	private Map<Simbolo, List<VEstrela>> producoes = new HashMap<>();
	private Simbolo simboloInicial;
	
	public Set<Simbolo> getSimbolosTerminais() {
		return simbolosTerminais;
	}
	
	public void setSimbolosTerminais(Set<Simbolo> simbolosTerminais) {
		this.simbolosTerminais = simbolosTerminais;
	}
	
	public Set<Simbolo> getSimbolosNaoTerminais() {
		return simbolosNaoTerminais;
	}
	
	public void setSimbolosNaoTerminais(Set<Simbolo> simbolosNaoTerminais) {
		this.simbolosNaoTerminais = simbolosNaoTerminais;
	}
	
	public Map<Simbolo, List<VEstrela>> getProducoes() {
		return producoes;
	}
	
	public VEstrela obterLadoDireitoPorNumeroComNaoTerminal(Simbolo naoTerminal, int ordem) throws LadoSemOrdemException {
		for (VEstrela ladoDireito : producoes.get(naoTerminal)) {
			if (ladoDireito.obterOrdem() == ordem) {
				return ladoDireito;
			}
		}
		
		throw new LadoSemOrdemException();
	}
	
	public void setProducoes(Map<Simbolo, List<VEstrela>> producoes) {
		this.producoes = producoes;
	}
	
	public Simbolo getSimboloInicial() {
		return simboloInicial;
	}
	
	public void setSimboloInicial(Simbolo simboloInicial) {
		this.simboloInicial = simboloInicial;
	}
	
	
}
