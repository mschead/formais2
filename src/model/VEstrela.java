package model;

import java.util.ArrayList;
import java.util.List;

public class VEstrela {

	private List<Simbolo> simbolos = new ArrayList<>();
	private int ORDEM = -1;
	private static int INDEX = 0;

	public VEstrela(List<Simbolo> simbolos) {
		this.simbolos = new ArrayList<>(simbolos); 
	}
	
	public VEstrela(Simbolo...simbolos) {
		for (Simbolo simbolo : simbolos) {
			this.simbolos.add(simbolo);
		}
	}
	
	public VEstrela(VEstrela simbolos) {
		this.inserirSimbolos(simbolos);
		this.ORDEM = simbolos.ORDEM;
	}
	
	public VEstrela(int ordem) {
		this.ORDEM = ordem;
	}
	
	
	public List<Simbolo> getSimbolos() {
		return simbolos;
	}

	public void setSimbolos(List<Simbolo> simbolos) {
		this.simbolos = simbolos;
	}
	
	public void definirOrdem() {
		this.ORDEM = ++INDEX;
	}
	
	public int obterOrdem() {
		return ORDEM;
	}
	
	public static void zerarContador() {
		INDEX = -1;
	}
	
	public boolean primeiroSimboloTerminal() {
		return simbolos.get(0).isTerminal();
	}
	
	public boolean primeiroSimboloEpsilon() {
		return simbolos.get(0).equals(Simbolo.EPSILON);
	}
	
	public boolean possuiEpsilon() {
		return simbolos.contains(Simbolo.EPSILON);
	}
	
	public boolean derivaEpsilonApenas() {
		return simbolos.get(0).equals(Simbolo.EPSILON);
	}
	
	public Simbolo primeiroSimbolo() {
		return simbolos.get(0);
	}

	public void retirarEpsilons() {
		this.simbolos.remove(Simbolo.EPSILON);
	}
	
	public void retirarPrimeiro() {
		this.simbolos.remove(0);
	}
	
	public boolean inserirSimbolos(VEstrela simbolos) {
		boolean novoSimboloInserido = false;
		
		for (Simbolo simbolo : simbolos.simbolos) {
			if (!this.simbolos.contains(simbolo)) {
				this.simbolos.add(simbolo);
				novoSimboloInserido = true;
			}
		}
		
		return novoSimboloInserido;
	}
	
	public void inserirSimbolosNoComeco(VEstrela simbolos) {
		for (int index = simbolos.getSimbolos().size() - 1; index > -1; index--) {
			Simbolo simbolo = simbolos.getSimbolos().get(index);
				this.simbolos.add(0, simbolo);
		}
	}
	
	public boolean inserirSimbolo(Simbolo simbolo) {
		if (!simbolos.contains(simbolo)) {
			this.simbolos.add(simbolo);
			return true;
		}
		return false;
	}

	public boolean possuiSimbolos(VEstrela simbolos) {
		return this.simbolos.containsAll(simbolos.simbolos);
	}
	
	public boolean possuiSimbolo(Simbolo simbolo) {
		return this.simbolos.contains(simbolo);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((simbolos == null) ? 0 : simbolos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VEstrela other = (VEstrela) obj;
		if (simbolos == null) {
			if (other.simbolos != null)
				return false;
		} else if (!simbolos.equals(other.simbolos))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		for (int index = 0; index < simbolos.size(); index++) {
			if (index != simbolos.size() - 1)
				s.append(simbolos.get(index) + " ");
			else
				s.append(simbolos.get(index));
		}
		
		return s.toString();
	}
	
}
