package model;

public class AlfaNumero {

	private VEstrela alfa;
	private int numero;
	
	public AlfaNumero(VEstrela alfa, int numero) {
		this.alfa = alfa;
		this.numero = numero;
	}

	public VEstrela getAlfa() {
		return alfa;
	}

	public void setAlfa(VEstrela alfa) {
		this.alfa = alfa;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	@Override
	public String toString() {
		return alfa + "-> " + numero;
	}

	public boolean interseccaoCom(AlfaNumero alfaNumero) {
		for (Simbolo simbolo1 : this.getAlfa().getSimbolos()) {
			for (Simbolo simbolo2 : alfaNumero.getAlfa().getSimbolos()) {
				if (simbolo1.equals(simbolo2))
					return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alfa == null) ? 0 : alfa.hashCode());
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
		AlfaNumero other = (AlfaNumero) obj;
		if (alfa == null) {
			if (other.alfa != null)
				return false;
		} else if (!alfa.getSimbolos().equals(other.alfa.getSimbolos()))
			return false;
		return true;
	}
	
	
	
}
