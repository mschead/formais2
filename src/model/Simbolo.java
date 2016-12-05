package model;


public class Simbolo {

	// Deixar um espaço em branco entre os símbolos do lado direito.
	// Representar não-terminais por letra maiúscula (seguida de 0 ou + dígitos).
	// Representar terminais com um ou mais caracteres contíguos (quaisquer caracteres, exceto letras maiúsculas).
	public static final Simbolo EPSILON = new Simbolo("&", true);
	public static final Simbolo DOLAR = new Simbolo("$", true);
	public static final Simbolo VELHA = new Simbolo("#", true);
	
	private String simbolo;
	private boolean isTerminal;
	
	public Simbolo(String simbolo, boolean isTerminal) {
		this.simbolo = simbolo;
		this.isTerminal = isTerminal;
	}
	
	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public boolean isTerminal() {
		return isTerminal;
	}

	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((simbolo == null) ? 0 : simbolo.hashCode());
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
		Simbolo other = (Simbolo) obj;
		if (simbolo == null) {
			if (other.simbolo != null)
				return false;
		} else if (!simbolo.equals(other.simbolo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return simbolo;
	}
	
}
