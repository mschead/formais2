package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.AlfaNumero;
import model.Gramatica;
import model.Simbolo;
import model.VEstrela;

public class GramaticaUtils {

	public static Map<Simbolo, VEstrela> calcularFirst(Gramatica gramatica) {
		Map<Simbolo, VEstrela> first = new HashMap<>();
		
		// gera o first dos não terminais (regra 1)
		for (Simbolo simbolo : gramatica.getSimbolosTerminais())
			first.put(simbolo, new VEstrela(simbolo));
		
		
		Map<Simbolo, List<VEstrela>> producoes = gramatica.getProducoes();
		
		// pegar cada produção de um não terminal, e obter os first do lado direito (regra 2)
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			List<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
			for (VEstrela simbolos : ladoDireito) {
//				simbolos.definirOrdem(); // aproveita o loop para determinar a ordem das produções, para construir a tabela de parsing.
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
//		VEstrela.zerarContador();
		
		// gerar os first das produções cujo lado direito comecem por um não terminal (regra 3)
		boolean firstModificado = true;
		while (firstModificado) {
			firstModificado = false;
			for (Simbolo ladoEsquerdo : producoes.keySet()) {
				List<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
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
										firstModificado = firstSimboloAtual.inserirSimbolo(Simbolo.EPSILON);
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
		Map<Simbolo, List<VEstrela>> producoes = gramatica.getProducoes();
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			List<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
			for (VEstrela simbolos : ladoDireito) {
				for (int indexI = 0; indexI < simbolos.getSimbolos().size(); indexI++) { 
					Simbolo naoTerminal = simbolos.getSimbolos().get(indexI);
					if (!naoTerminal.isTerminal()) {
						boolean pronto = false;
						int indexJ = indexI + 1;
						while (!pronto && indexJ < simbolos.getSimbolos().size()) {
							Simbolo beta = simbolos.getSimbolos().get(indexJ);
							VEstrela followDoNaoTerminal = follow.get(naoTerminal);
							if (first.get(beta) != null) {
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
				List<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
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
									if (modificado) {
										followQueRecebe.inserirSimbolos(followLadoEsquerdo);
									} else {
										modificado = followQueRecebe.inserirSimbolos(followLadoEsquerdo);
									}
								} else {
									followQueRecebe = new VEstrela(followLadoEsquerdo);
									follow.put(naoTerminalAtual, followQueRecebe);
									modificado = true;
								}

								if (first.get(naoTerminalAtual) == null
										|| !first.get(naoTerminalAtual).possuiEpsilon()) {
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
	public static Map<Simbolo, List<Simbolo>> calcularFirstNT(Gramatica gramatica, Map<Simbolo, VEstrela> first) {
		Map<Simbolo, List<Simbolo>> firstNT = new HashMap<>();
		Map<Simbolo, List<Simbolo>> firstNTAux = new HashMap<>();
		Map<Simbolo, List<VEstrela>> producoes = gramatica.getProducoes();
		
		// prepara os firstNT e firstNT aux (para comparação iterativa)
		for (Simbolo naoTerminal : gramatica.getSimbolosNaoTerminais()) {
			List<Simbolo> firstNTA = new ArrayList<>();
			List<Simbolo> firstNTAAux = new ArrayList<>();
			firstNT.put(naoTerminal, firstNTA);
			firstNTAux.put(naoTerminal, firstNTAAux);
		}
		
		do {
			// firstNTAux = firstNT
			for (Simbolo naoTerminal : gramatica.getSimbolosNaoTerminais()) {
				List<Simbolo> firstNTA = firstNT.get(naoTerminal);
				List<Simbolo> firstNTAAux = firstNTAux.get(naoTerminal);
				for (Simbolo s : firstNTA) {
					if (!firstNTAAux.contains(s)) {
						firstNTAAux.add(s);
					}
				}				
			}
			// para cada producaoA -> B, se o primeiro simbolo de B for um naoTerminal, adiciona o nao Terminal em firstNT(A)
			// se & C first(B), repita o processo para o proximo simbolo de B
			for (Simbolo ladoEsquerdo : producoes.keySet()) {
				List<Simbolo> firstNTA = new ArrayList<>();
				for (Simbolo s : firstNT.get(ladoEsquerdo)) {
					firstNTA.add(s);
				}
				for (VEstrela producao : producoes.get(ladoEsquerdo)) {
					for (int i = 0; i < producao.getSimbolos().size(); i++) {
						Simbolo s = producao.getSimbolos().get(i);
						if (s.isTerminal()) {
							break;
						}
						if (!firstNTA.contains(s)) {
							firstNTA.add(s);
						}
						if (!first.get(s).possuiEpsilon()) {
							break;
						}
					}
				}
				List<Simbolo> list = firstNT.get(ladoEsquerdo);
				for (Simbolo s2 : firstNTA) {
					if (!list.contains(s2)) {
						list.add(s2);
					}
				}
			}
		} while (!firstNT.equals(firstNTAux));
		
		return firstNT;
	}
	
		public static boolean estaFatorada(Gramatica gramatica, Map<Simbolo, VEstrela> first) {
		Map<Simbolo, List<VEstrela>> producoes = gramatica.getProducoes();
		
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			List<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
			for (VEstrela producaoAtual : ladoDireito) {
				Set<Simbolo> firstAtual = new HashSet<>();
				for (VEstrela producaoComparada : ladoDireito) {
					Set<Simbolo> firstComparada = new HashSet<>();
					boolean mesmaProd = producaoAtual.equals(producaoComparada);
					if (!mesmaProd) {
						// calcula o first da producao atual
						for (Simbolo s : producaoAtual.getSimbolos()) {
							if (s.isTerminal()) {
								if (s != Simbolo.EPSILON) {
									firstAtual.add(s);
								}
								break;
							} else {
								for (Simbolo sa : first.get(s).getSimbolos()) {
									if (!firstAtual.contains(sa)) {
										firstAtual.add(sa);
									}
								}
							}
							if (!first.get(s).possuiEpsilon()) {
									break;
							}
						}
						// calcula o first da producao comparada
						for (Simbolo s : producaoComparada.getSimbolos()) {
							if (s.isTerminal()) {
								if (s != Simbolo.EPSILON) {
									firstComparada.add(s);
								}
								break;
							} else {
								for (Simbolo sa : first.get(s).getSimbolos()) {
									if (!firstComparada.contains(sa)) {
										firstComparada.add(sa);
									}
								}
							}
							if (!first.get(s).possuiEpsilon()) {
									break;
							}
						}
						Set<Simbolo> simbolosFirstAtual = new HashSet<>();
						simbolosFirstAtual.addAll(firstAtual);
						simbolosFirstAtual.retainAll(firstComparada);
						simbolosFirstAtual.remove(Simbolo.EPSILON);
						if (!simbolosFirstAtual.isEmpty()) {
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}

	public static boolean temRecursaoAEsquerda(Gramatica gramatica, Map<Simbolo, List<Simbolo>> firstNT) {
		boolean temRecursao = false;
		for (Simbolo ladoEsquerdo : gramatica.getProducoes().keySet()) {
			if (firstNT.get(ladoEsquerdo).contains(ladoEsquerdo)) {
				temRecursao = true;
				break;
			}
		}
		return temRecursao;
	}

	public static boolean firstInterseccaoFollowVazia(Map<Simbolo, VEstrela> first, Map<Simbolo, VEstrela> follow) {
		boolean intersecao = true;
		for (Simbolo s : first.keySet()) {
			if (first.get(s).possuiEpsilon()) {
				VEstrela firstAtual = new VEstrela(first.get(s));
				VEstrela followAtual = new VEstrela(follow.get(s));
				firstAtual.getSimbolos().retainAll(followAtual.getSimbolos());
				if (!firstAtual.getSimbolos().isEmpty()) {
					intersecao = false;
					break;
				}
			}
		}
		return intersecao;
	}
	
	// Usando First para verificar se G esta fatorada;
	// Usando First-NT para verificar se G possui recursão a esquerda;
	public static boolean isGramaticaLL1(Gramatica gramatica) {
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		Map<Simbolo, List<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		boolean LL1 = (!temRecursaoAEsquerda(gramatica, firstNT) && estaFatorada(gramatica, first)
				&& firstInterseccaoFollowVazia(first, follow));

		return LL1;
	}

	
	public static Map<Simbolo, List<VEstrela>> construirTabelaParsing(Gramatica gramatica, Map<Simbolo, VEstrela> first, Map<Simbolo, VEstrela> follow) {
		Map<Simbolo, List<VEstrela>> estruturaParser = new HashMap<>();
		Map<Simbolo, List<VEstrela>> producoes = gramatica.getProducoes();
		
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			List<VEstrela> ladoDireito = producoes.get(ladoEsquerdo);
			List<VEstrela> alfaNumeroLista = new ArrayList<>();
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
				List<VEstrela> lista = estruturaParser.get(ladoEsquerdo);
				lista.add(firstAlfa);
			}
		}
				
		return estruturaParser;
	}
	
	// analise sintatica, precisa da tabela
	public static boolean reconheceSentenca(Gramatica gramatica, Map<Simbolo, List<VEstrela>> estruturaParser,
			List<Simbolo> sentenca) {
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
				List<VEstrela> alfaNumero = estruturaParser.get(simbolo);
				int numeroProducao = -1;
				for (VEstrela par : alfaNumero) {
					if (par.possuiSimbolo(posicaoSentenca)) {
						numeroProducao = par.obterOrdem();
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

	public static Gramatica obterSemCiclos(Gramatica glc) {
		Gramatica gramatica = new Gramatica(glc);
		gramatica = obterEpsilonLivre(glc);
		Map<Simbolo, Set<Simbolo>> N = new HashMap<>();
		Map<Simbolo, Set<Simbolo>> Nmenos1 = new HashMap<>();
		Map<Simbolo, List<VEstrela>> producoes = gramatica.getProducoes();
		if (producoes == null || producoes.isEmpty() || producoes.keySet().isEmpty()) {
			return gramatica;
		}
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			Set<Simbolo> Na = new HashSet<>();
			Set<Simbolo> NaMenos1 = new HashSet<>();
			Na.add(ladoEsquerdo);
			N.put(ladoEsquerdo, Na);
			Nmenos1.put(ladoEsquerdo, NaMenos1);
		}
		
		// Enquanto algum simbolo é adicionado em Na
		do {
			
			// Na anterior recebe Na atual, a E Vn
			for (Simbolo ladoEsquerdo : producoes.keySet()) {
				Set<Simbolo> Naux = N.get(ladoEsquerdo);
				Nmenos1.get(ladoEsquerdo).addAll(Naux);
			}
			
			// Para cada producao P -> A, se A E Vt, adiciona Na em Np
			for (Simbolo ladoEsquerdo : producoes.keySet()) {
				for (VEstrela producao : producoes.get(ladoEsquerdo)) {
					if (producao.getSimbolos().size() == 1) {
						Simbolo s = producao.getSimbolos().get(0);
						if (!s.isTerminal()) {
							Set<Simbolo> Na = N.get(ladoEsquerdo);
							Set<Simbolo> Nb = N.get(s);
							Na.addAll(Nb);
						}
					}
				}
			}
			
		} while (!N.equals(Nmenos1));
		
		// Preparar novas producoes
		Map<Simbolo, List<VEstrela>> novasProducoes = new HashMap<>();
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			List<VEstrela> ladoDireito = new ArrayList<>();
			novasProducoes.put(ladoEsquerdo, ladoDireito);
		}
		
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			List<VEstrela> novaProdA = novasProducoes.get(ladoEsquerdo);
			for (Simbolo ladoEsquerdoNa : N.get(ladoEsquerdo)) {
				for (VEstrela producaoNa : producoes.get(ladoEsquerdoNa)) {
					if (producaoNa.getSimbolos().size() == 1) {
						if (producaoNa.getSimbolos().get(0).isTerminal()) {
							novaProdA.add(producaoNa);
						}
					} else {
						novaProdA.add(producaoNa);
					}
				}
			}
		}
		
		gramatica.setProducoes(novasProducoes);
		
		return gramatica;
	}
	
	public static Gramatica obterEpsilonLivre(Gramatica gramatica) {
		Gramatica glinha = new Gramatica(gramatica);
		Set<Simbolo> Ne = new HashSet<>();
		Set<Simbolo> NeMenos1 = new HashSet<>();
		Map<Simbolo, List<VEstrela>> producoes = gramatica.getProducoes();
		Map<Simbolo, Set<VEstrela>> novasProducoes = new HashMap<>();

		if (producoes == null || producoes.isEmpty() || producoes.keySet().isEmpty()) {
			return gramatica;
		}

		// adicione em Ne todos os simbolos que derivam epsilon diretamente
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			for (VEstrela producao : producoes.get(ladoEsquerdo)) {
				if (producao.derivaEpsilonApenas()) {
					Ne.add(ladoEsquerdo);
					break;
				}
			}
		}
		
		// adiciona em Ne todos os simbolos que derivam epsilon indiretamente
		do {
			NeMenos1.addAll(Ne);
			for (Simbolo ladoEsquerdo : producoes.keySet()) {
				for (VEstrela producao : producoes.get(ladoEsquerdo)) {
					boolean derivaEpsilonIndiretamente = true;
					for (Simbolo s : producao.getSimbolos()) {
						if (!Ne.contains(s)) {
							derivaEpsilonIndiretamente = false;
							break;
						}
					}
					if (derivaEpsilonIndiretamente) {
						Ne.add(ladoEsquerdo);
					}
				}
			}
		} while (!Ne.containsAll(NeMenos1) || !NeMenos1.containsAll(Ne));
		
		// adicionar em P' todas as producoes que nao derivam & diretamente
		for (Simbolo ladoEsquerdo : producoes.keySet()) {
			Set<VEstrela> ladoDireito = new HashSet<>();
			novasProducoes.put(ladoEsquerdo, ladoDireito);
			for (VEstrela producao : producoes.get(ladoEsquerdo)) {
				if (!producao.derivaEpsilonApenas()) {
					ladoDireito.add(producao);
				}
			}
		}
		
		Map<Simbolo, List<VEstrela>> novasProducoesAux = new HashMap<>();
		Map<Simbolo, List<VEstrela>> novasNovasProducoes = new HashMap<>();
		
		// para cada producao P, se P -> aAb e A -*> &, P -> ab
		do {
			if (!novasNovasProducoes.keySet().isEmpty()) {
				for (Simbolo ladoEsquerdo : novasNovasProducoes.keySet()) {
					List<VEstrela> ladoDireito = new ArrayList<>();
					novasProducoesAux.put(ladoEsquerdo, ladoDireito);
					for (VEstrela producao : novasNovasProducoes.get(ladoEsquerdo)) {
						ladoDireito.add(producao);
					}
				}
			}
			for (Simbolo ladoEsquerdo : novasProducoes.keySet()) {
				List<VEstrela> ladoDireito = novasNovasProducoes.get(ladoEsquerdo) ;
				if (ladoDireito == null) {
					ladoDireito = new ArrayList<>();
					novasNovasProducoes.put(ladoEsquerdo, ladoDireito);
				}
				for (VEstrela producao : novasProducoes.get(ladoEsquerdo)) {
					if (!ladoDireito.contains(producao)) {
						ladoDireito.add(producao);
					}
					for (Simbolo s : producao.getSimbolos()) {
						if (Ne.contains(s)) {
							VEstrela producaoNova = new VEstrela(producao, true);
							producaoNova.getSimbolos().remove(s);
							if (!producaoNova.getSimbolos().isEmpty() && !ladoDireito.contains(producaoNova)) {
								ladoDireito.add(producaoNova);
							}
						}
					}
				}
				for (VEstrela producao : novasNovasProducoes.get(ladoEsquerdo)) {
					novasProducoes.get(ladoEsquerdo).add(producao);
				}
			}
		} while (!novasNovasProducoes.equals(novasProducoesAux));

		if (Ne.contains(gramatica.getSimboloInicial())) {
			Simbolo Slinha = new Simbolo("S\'", false);
			glinha.getSimbolosNaoTerminais().add(Slinha);
			List<VEstrela> ladoDireitoSlinha = new ArrayList<>();
			ladoDireitoSlinha.add(new VEstrela(glinha.getSimboloInicial()));
			ladoDireitoSlinha.add(new VEstrela(Simbolo.EPSILON));

			novasNovasProducoes.put(Slinha, ladoDireitoSlinha);			
		}
		
		glinha.setProducoes(novasNovasProducoes);
		
		return glinha;
	}

	public static Gramatica obterSemSimbolosInuteis(Gramatica gramatica) {
		Gramatica glinha = new Gramatica(gramatica);
		glinha = obterSemInferteis(glinha);
		glinha = obterSemInalcancaveis(glinha);
		return glinha;
	}

	public static Gramatica obterSemInferteis(Gramatica gramatica) {
		Gramatica glinha = new Gramatica(gramatica);
		int i = 0;
		Set<Simbolo> Ni = new HashSet<>();
		Set<Simbolo> NiMenos1 = new HashSet<>();
		Map<Simbolo, List<VEstrela>> producoes = glinha.getProducoes();
		
		if (producoes == null || producoes.isEmpty() || producoes.keySet().isEmpty()) {
			return gramatica;
		}
		//descobre os simbolos ferteis (Ni)
		do {
			NiMenos1.addAll(Ni);
			for (Simbolo ladoEsquerdo : producoes.keySet()) {
				for (VEstrela producao : producoes.get(ladoEsquerdo)) {
					//Verifica se a producao é fertil
					boolean producaoFertil = true;
					for (Simbolo s : producao.getSimbolos()) {
						if (!s.isTerminal() && !NiMenos1.contains(s)) {
							producaoFertil = false;
							break;
						}
					}
					//caso for, adicione-a em ni
					if (producaoFertil) {
						Ni.add(ladoEsquerdo);
					}
				}
			}
			
		} while (!Ni.containsAll(NiMenos1) || !NiMenos1.containsAll(Ni));
		
		//os Nao-Terminais encontrados serao os novos Vn da gramatia
		glinha.setSimbolosNaoTerminais(Ni);
		
		//pega todos os simbolos inferteis
		Set<Simbolo> naoFerteis = new HashSet<>();
		naoFerteis.addAll(gramatica.getSimbolosNaoTerminais());
		naoFerteis.removeAll(Ni);
		
		//então remove as produçoes do lado esquerdo com esse simbolo
		Map<Simbolo, List<VEstrela>> novasProducoes = new HashMap<>();
		novasProducoes.putAll(producoes);
		for (Simbolo naoFertil : naoFerteis) {
			novasProducoes.remove(naoFertil);
		}
		
		Map<Simbolo, List<VEstrela>> novasNovasProducoes = new HashMap<>();
		
		Set<Simbolo> NiUVt = new HashSet<>();
		NiUVt.addAll(Ni);
		NiUVt.addAll(gramatica.getSimbolosTerminais());
		NiUVt.add(Simbolo.EPSILON);
		
		//e depois as do lado direito
		for (Simbolo ladoEsquerdo : novasProducoes.keySet()) {
			for (VEstrela producao : novasProducoes.get(ladoEsquerdo)) {
				if (NiUVt.containsAll(producao.getSimbolos())) {
					if (!novasNovasProducoes.containsKey(ladoEsquerdo)) {
						List<VEstrela> ladoDireito = new ArrayList<>();
						if (ladoDireito.contains(producao)) {
							ladoDireito.add(producao);
						}
						novasNovasProducoes.put(ladoEsquerdo, ladoDireito);
					}
					List<VEstrela> ladoDireito = novasNovasProducoes.get(ladoEsquerdo);
					ladoDireito.add(producao);
				}
				
			}
		}
		
		glinha.setProducoes(novasNovasProducoes);
		
		return glinha;
	}

	public static Gramatica obterSemInalcancaveis(Gramatica gramatica) {
		Gramatica glinha = new Gramatica(gramatica);
		Set<Simbolo> Vi = new HashSet<>();
		Set<Simbolo> ViAux = new HashSet<>();
		Vi.add(gramatica.getSimboloInicial());
		Set<Simbolo> ViMenos1 = new HashSet<>();
		Map<Simbolo, List<VEstrela>> producoes = gramatica.getProducoes();
		
		if (producoes == null || producoes.isEmpty() || producoes.keySet().isEmpty()) {
			return gramatica;
		}
		
		do {
			ViMenos1.addAll(Vi);
			for (Simbolo ladoEsquerdo : Vi) {
				for (VEstrela producao : producoes.get(ladoEsquerdo)) {
					for (Simbolo s : producao.getSimbolos()) {
						if (!s.isTerminal()) {
							ViAux.add(s);
						}
					}
				}
			}
			Vi.addAll(ViAux);
		} while (!Vi.containsAll(ViMenos1) || !ViMenos1.containsAll(Vi));
		
		glinha.setSimbolosNaoTerminais(Vi);
		
		//pega todos os simbolos inalcancaveis
		Set<Simbolo> inalcancaveis = new HashSet<>();
		inalcancaveis.addAll(gramatica.getSimbolosNaoTerminais());
		inalcancaveis.removeAll(Vi);
		
		//então remove as produçoes do lado esquerdo com esse simbolo
		Map<Simbolo, List<VEstrela>> novasProducoes = new HashMap<>();
		novasProducoes.putAll(producoes);
		for (Simbolo inalcancavel : inalcancaveis) {
			novasProducoes.remove(inalcancavel);
		}
		
		glinha.setProducoes(novasProducoes);
		
		return glinha;
	}
		
	// sem ciclos
	// & livre
	// sem simbolos inuteis
	public static Gramatica obterPropria(Gramatica gramatica) {
		Gramatica glinha = new Gramatica(gramatica);
		glinha = obterSemCiclos(glinha);
		Gramatica glinha2 = new Gramatica(glinha);
		glinha2 = obterSemSimbolosInuteis(glinha);
		return glinha2;
	}
	
}
