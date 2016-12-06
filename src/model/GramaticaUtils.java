package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GramaticaUtils {

	public static Map<Simbolo, VEstrela> calcularFirst(Gramatica gramatica) {
		Map<Simbolo, VEstrela> first = new HashMap<>();
		
		// gera o first dos não terminais (regra 1)
		for (Simbolo simbolo : gramatica.getSimbolosTerminais())
			first.put(simbolo, new VEstrela(simbolo));
		
		
		Map<Simbolo, Set<VEstrela>> producoes = gramatica.getProducoes();
		
		// pegar cada produção de um não terminal, e obter os first do lado direito (regra 2)
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			Set<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
			for (VEstrela simbolos : ladoDireito) {
				simbolos.definirOrdem(); // aproveita o loop para determinar a ordem das produções, para construir a tabela de parsing.
				if (simbolos.primeiroSimboloTerminal() || simbolos.primeiroSimboloEpsilon()) {
					VEstrela firstAtual = first.get(ladoEsquerdo);
					if (firstAtual != null) {
						firstAtual.inserirSimbolo(simbolos.primeiroSimbolo());
					} else {
						first.put(ladoEsquerdo, new VEstrela(simbolos.primeiroSimbolo()));
					}
				}
			}
		}
		VEstrela.zerarContador();
		
		// gerar os first das produções cujo lado direito comecem por um não terminal (regra 3)
		boolean firstModificado = true;
		while (firstModificado) {
			firstModificado = false;
			for (Simbolo ladoEsquerdo : producoes.keySet()) {
				Set<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
				for (VEstrela simbolos : ladoDireito) {
					if (!simbolos.primeiroSimboloTerminal()) {
						int index = 0;
						boolean podeDerivarEpsilon = false;
						boolean acabou = false;
						while (!acabou) {
							VEstrela firstNT = first.get(simbolos.getSimbolos().get(index));
							if (firstNT != null) {
								if (firstNT.possuiEpsilon()) {
									firstNT = new VEstrela(firstNT);	//clone
									firstNT.retirarEpsilons();
									podeDerivarEpsilon = true;
								} else {
									podeDerivarEpsilon = false;
								}
								VEstrela firstSimboloAtual = first.get(ladoEsquerdo);
								if (firstSimboloAtual == null) {
									firstSimboloAtual = new VEstrela(firstNT);
									first.put(ladoEsquerdo, firstSimboloAtual);
								} else {
									firstModificado = firstSimboloAtual.inserirSimbolos(firstNT);
								}
								
								// diferenciar analise do ultimo simbolo do resto
								if (simbolos.getSimbolos().size() - index != 1) {
									if (podeDerivarEpsilon) {
										index++;
									} else {
										acabou = true;
										if (simbolos.getSimbolos().get(index + 1).isTerminal()) {
											firstModificado = firstSimboloAtual.inserirSimbolo(simbolos.getSimbolos().get(index + 1));
										}
									}
								} else {
									if (podeDerivarEpsilon) {
										firstSimboloAtual.inserirSimbolo(Simbolo.EPSILON);
									} else {
										index++;
									}
									acabou = true;
								}
							} else {
								acabou = true;
							}
						}
					}
					

				}
			}
		}
		
		return first;
	}
	
	public static Map<Simbolo, VEstrela> calcularFollow(Gramatica gramatica, Map<Simbolo, VEstrela> first) {
		Map<Simbolo, VEstrela> follow = new HashMap<>();
		
		// Insere o símbolo de final de palavra no follow da produção inicial (regra 1)
		follow.put(gramatica.getSimboloInicial(), new VEstrela(Simbolo.DOLAR));
		
		// Seja aBC, adiciona o first de C no follow de B para todos os lados direitos de todas as produções.
		Map<Simbolo, Set<VEstrela>> producoes = gramatica.getProducoes();
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			Set<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
			for (VEstrela simbolos : ladoDireito) {
				for (int indexI = 0; indexI < simbolos.getSimbolos().size(); indexI++) { 
					Simbolo naoTerminal = simbolos.getSimbolos().get(indexI);
					if (!naoTerminal.isTerminal()) {
						boolean pronto = false;
						int indexJ = indexI + 1;
						while (!pronto && indexJ < simbolos.getSimbolos().size()) {
							Simbolo beta = simbolos.getSimbolos().get(indexJ);
							VEstrela followDoNaoTerminal = follow.get(naoTerminal);
							VEstrela firstBeta = new VEstrela(first.get(beta).getSimbolos());
							firstBeta.retirarEpsilons();
							
							if (followDoNaoTerminal != null) {
								followDoNaoTerminal.inserirSimbolos(firstBeta);
							} else {
								followDoNaoTerminal = new VEstrela(firstBeta);
								follow.put(naoTerminal, followDoNaoTerminal);
							}
							
							if (beta.isTerminal() || !first.get(beta).getSimbolos().contains(Simbolo.EPSILON)) {
								pronto = true;
							}
							indexJ++;
						}
					}
				}
			}
		}
		
		
		// Pega o lado direito das produções em que haja um NT, e adiciona o follow do lado esquerdo ao follow do lado direito (regra 3)
		boolean modificado = true;
		while (modificado) {
			modificado = false;
			for (Simbolo ladoEsquerdo : producoes.keySet()) {
				Set<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
				VEstrela followLadoEsquerdo = follow.get(ladoEsquerdo);
				if (followLadoEsquerdo != null) {
					for (VEstrela simbolos : ladoDireito) {
						int index = simbolos.getSimbolos().size() - 1;
						boolean naoTerminalDerivaEpsilon = true;
						while (index > -1 && naoTerminalDerivaEpsilon) {
							Simbolo naoTerminalAtual = simbolos.getSimbolos().get(index);
							if (!naoTerminalAtual.isTerminal()) {
								VEstrela followQueRecebe = follow.get(naoTerminalAtual);
								if (followQueRecebe != null) {
									modificado = followQueRecebe.inserirSimbolos(followLadoEsquerdo);
								} else {
									followQueRecebe = new VEstrela(followLadoEsquerdo);
									follow.put(naoTerminalAtual, followQueRecebe);
									modificado = true;
								}
								
								if (!first.get(naoTerminalAtual).possuiEpsilon()) {
									naoTerminalDerivaEpsilon = false;
								}
								index--;
							} else {
								index = -1; // força parada da analise de uma das produções.
							}
						}
					}	
				}
			}
		}
				
		return follow;
	}
	
	// conjunto de símbolos de Vn que podem iniciar sequências derivadas de A
	public static Set<Simbolo> calcularFirstNT(Gramatica gramatica, Map<Simbolo, VEstrela> first) {
		Map<Simbolo, Set<VEstrela>> producoes = gramatica.getProducoes();
		Set<Simbolo> firstNT = new HashSet<>();
		
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			Set<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
			for (VEstrela simbolos : ladoDireito) {
				for (Simbolo simbolo : simbolos.getSimbolos()) {
					if (!simbolo.isTerminal() && simbolo.equals(ladoEsquerdo)) {
						firstNT.add(ladoEsquerdo);
						break;
					} else if (first.get(ladoEsquerdo).getSimbolos().contains(Simbolo.EPSILON)) {
						
					}
				}
			}
		}
		
		return firstNT;
	}
	
	public static boolean verificarFatoracao(Gramatica gramatica, Map<Simbolo, VEstrela> first) {
		Map<Simbolo, List<AlfaNumero>> firstPorAlfa = new HashMap<>();
		Map<Simbolo, Set<VEstrela>> producoes = gramatica.getProducoes();
		
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			Set<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
			List<AlfaNumero> alfaNumeroLista = new ArrayList<>();
			firstPorAlfa.put(ladoEsquerdo, alfaNumeroLista);
			
			for (VEstrela simbolos : ladoDireito) {
				int index = 0;
				boolean achouTerminalOuNaoDerivaEpsilon = false;
				VEstrela firstAlfa = new VEstrela();
				boolean podeDerivarEpsilon = false;
				
				while (!achouTerminalOuNaoDerivaEpsilon) {
					Simbolo simbolo = simbolos.getSimbolos().get(index);
					if (simbolo.equals(Simbolo.EPSILON)) {
						firstAlfa.inserirSimbolo(Simbolo.EPSILON);
						break;
					}
					VEstrela firstCalculado = first.get(simbolo);
					
					firstAlfa.inserirSimbolos(firstCalculado);
					if (simbolo.isTerminal() || !podeDerivarEpsilon) {
						achouTerminalOuNaoDerivaEpsilon = true;
					}
					
				}
				List<AlfaNumero> lista = firstPorAlfa.get(ladoEsquerdo);
				lista.add(new AlfaNumero(firstAlfa, simbolos.obterOrdem()));
			}
		}
		
		
		for (Simbolo simbolo : firstPorAlfa.keySet()) {
			List<AlfaNumero> ladoDireito = firstPorAlfa.get(simbolo);
			for (int i = 0; i < ladoDireito.size(); i++) {
				AlfaNumero um = ladoDireito.get(i);
				for (int j = i + 1; j < ladoDireito.size(); j++) {
					AlfaNumero dois = ladoDireito.get(j);
					if (um.interseccaoCom(dois)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean firstInterseccaoFollow(Map<Simbolo, VEstrela> first, Map<Simbolo, VEstrela> follow) {
		return false;
	}
	
	// Usando First para verificar se G esta fatorada;
	// Usando First-NT para verificar se G possui recursão a esquerda;
	public static boolean isGramaticaLL1(Gramatica gramatica) {
		return false;
	}
	
	public static Map<Simbolo, List<AlfaNumero>> construirTabelaParsing(Gramatica gramatica, Map<Simbolo, VEstrela> first, Map<Simbolo, VEstrela> follow) {
		Map<Simbolo, List<AlfaNumero>> estruturaParser = new HashMap<>();
		Map<Simbolo, Set<VEstrela>> producoes = gramatica.getProducoes();
		
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			Set<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
			List<AlfaNumero> alfaNumeroLista = new ArrayList<>();
			estruturaParser.put(ladoEsquerdo, alfaNumeroLista);
			
			for (VEstrela simbolos : ladoDireito) {
				int index = 0;
				boolean achouTerminalOuNaoPossuiEpsilon = false;
				boolean podeDerivarEpsilon = false;
				VEstrela firstAlfa = new VEstrela(simbolos.obterOrdem());
				
				while (!achouTerminalOuNaoPossuiEpsilon && index < simbolos.getSimbolos().size()) {
					Simbolo simbolo = simbolos.getSimbolos().get(index);
					if (simbolo.equals(Simbolo.EPSILON)) {
						firstAlfa.inserirSimbolos(follow.get(ladoEsquerdo));
						break;
					}
					VEstrela firstCalculado = first.get(simbolo);
					if (firstCalculado.possuiEpsilon()) {
						firstCalculado = new VEstrela(firstCalculado);	//clone
						firstCalculado.retirarEpsilons();
						podeDerivarEpsilon = true;
					} else {
						podeDerivarEpsilon = false;
					}
					
					firstAlfa.inserirSimbolos(firstCalculado);
					
					if (index == simbolos.getSimbolos().size() - 1 && podeDerivarEpsilon) {
						firstAlfa.inserirSimbolos(follow.get(ladoEsquerdo));
					}
					
					if (simbolo.isTerminal() || !podeDerivarEpsilon) {
						achouTerminalOuNaoPossuiEpsilon = true;
					} else {
						index++;
					}
					
				}
				List<AlfaNumero> lista = estruturaParser.get(ladoEsquerdo);
				lista.add(new AlfaNumero(firstAlfa, simbolos.obterOrdem()));
			}
		}
				
		return estruturaParser;
	}
	
	// analise sintatica, precisa da tabela
	public static boolean reconheceSentenca(Gramatica gramatica, Map<Simbolo, List<AlfaNumero>> estruturaParser, List<Simbolo> sentenca) {
		VEstrela parse = new VEstrela(gramatica.getSimboloInicial(), Simbolo.DOLAR);
		sentenca.add(Simbolo.DOLAR);
		
		boolean analiseFeita = false;
		while (!analiseFeita) {
			Simbolo simbolo = parse.getSimbolos().get(0);
			Simbolo posicaoSentenca = sentenca.get(0);
			if (simbolo.isTerminal()) {
				if (simbolo.equals(posicaoSentenca)) {
					if (simbolo.equals(Simbolo.DOLAR)) {
						return true;
					} else if (!simbolo.equals(Simbolo.DOLAR)) {
						sentenca.remove(0);
						parse.retirarPrimeiro();
					}
				} else {
					return false; // erro de análise sintática
				}
			} else {
				List<AlfaNumero> alfaNumero = estruturaParser.get(simbolo);
				int numeroProducao = -1;
				for (AlfaNumero par : alfaNumero) {
					if (par.getAlfa().possuiSimbolo(posicaoSentenca)) {
						numeroProducao = par.getNumero();
						break;
					}
				}
				
				if (numeroProducao == -1) 
					return false;
				
				VEstrela ladoDireito = gramatica.obterLadoDireitoPorNumeroComNaoTerminal(simbolo, numeroProducao);
				parse.getSimbolos().remove(0);
				if (!ladoDireito.derivaEpsilonApenas()) {
					parse.inserirSimbolosNoComeco(ladoDireito);
				}
			}
		}
		
		return true;
	}
	
	// sem ciclos
	// & livre
	// sem simbolos inuteis
	public static Gramatica obterPropria(Gramatica gramatica) {
		return null;
	}
	
}
