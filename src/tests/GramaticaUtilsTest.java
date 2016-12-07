package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import model.AlfaNumero;
import model.Gramatica;
import model.GramaticaUtils;
import model.Simbolo;
import model.VEstrela;

public class GramaticaUtilsTest {

	@SuppressWarnings("unused")
	@Test
	public void verificarFirst() {
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo mais = new Simbolo("+", true);
		simbolosTerminais.add(mais);
		Simbolo vezes = new Simbolo("*", true);
		simbolosTerminais.add(vezes);
		Simbolo abreP = new Simbolo("(", true);
		simbolosTerminais.add(abreP);
		Simbolo fechaP = new Simbolo(")", true);
		simbolosTerminais.add(fechaP);
		Simbolo id = new Simbolo("id", true);
		simbolosTerminais.add(id);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraE = new Simbolo("E", false);
		simbolosNaoTerminais.add(letraE);
		Simbolo letraD = new Simbolo("D", false);
		simbolosNaoTerminais.add(letraD);
		Simbolo letraT = new Simbolo("T", false);
		simbolosNaoTerminais.add(letraT);
		Simbolo letraS = new Simbolo("S", false);
		simbolosNaoTerminais.add(letraS);
		Simbolo letraF = new Simbolo("F", false);
		simbolosNaoTerminais.add(letraF);		
		
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		Set<VEstrela> ladoDireitoE = new HashSet<>();
		ladoDireitoE.add(new VEstrela(letraT, letraD));
		
		producoes.put(letraE, ladoDireitoE);
		
		Set<VEstrela> ladoDireitoD = new HashSet<>();
		ladoDireitoD.add(new VEstrela(mais, letraT, letraD));
		ladoDireitoD.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraD, ladoDireitoD);
		
		Set<VEstrela> ladoDireitoT = new HashSet<>();
		ladoDireitoT.add(new VEstrela(letraF, letraS));

		producoes.put(letraT, ladoDireitoT);
		
		Set<VEstrela> ladoDireitoS = new HashSet<>();
		ladoDireitoS.add(new VEstrela(vezes, letraF, letraS));
		ladoDireitoS.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraS, ladoDireitoS);
		
		Set<VEstrela> ladoDireitoF = new HashSet<>();
		ladoDireitoF.add(new VEstrela(abreP, letraE, fechaP));
		ladoDireitoF.add(new VEstrela(id));
		
		producoes.put(letraF, ladoDireitoF);
		
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraE);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		
		Map<Simbolo, List<AlfaNumero>> parser = GramaticaUtils.construirTabelaParsing(gramatica, first, follow);
		ArrayList<Simbolo> sentenca = new ArrayList<>();
		sentenca.add(id);
		sentenca.add(mais);
		sentenca.add(id);
		
		GramaticaUtils.reconheceSentenca(gramatica, parser, sentenca);
		
		ArrayList<Simbolo> sentenca2 = new ArrayList<>();
		sentenca2.add(id);
		sentenca2.add(vezes);
		sentenca2.add(abreP);
		sentenca2.add(id);
		sentenca2.add(id);
		sentenca2.add(fechaP);
		
//		GramaticaUtils.reconheceSentenca(gramatica, parser, sentenca2);
		
		GramaticaUtils.estaFatorada(gramatica, first);
	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void verificarFirst2() {
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo sif = new Simbolo("if", true);
		simbolosTerminais.add(sif);
		Simbolo sthen = new Simbolo("then", true);
		simbolosTerminais.add(sthen);
		Simbolo scom = new Simbolo("com", true);
		simbolosTerminais.add(scom);
		Simbolo selse = new Simbolo("else", true);
		simbolosTerminais.add(selse);
		Simbolo sexp = new Simbolo("exp", true);
		simbolosTerminais.add(sexp);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraC = new Simbolo("C", false);
		simbolosNaoTerminais.add(letraC);
		Simbolo letraB = new Simbolo("B", false);
		simbolosNaoTerminais.add(letraB);
		Simbolo letraE = new Simbolo("E", false);
		simbolosNaoTerminais.add(letraE);
		
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		Set<VEstrela> ladoDireitoC = new HashSet<>();
		ladoDireitoC.add(new VEstrela(sif, letraE, sthen, letraC, letraB));
		ladoDireitoC.add(new VEstrela(scom));
		producoes.put(letraC, ladoDireitoC);
		
		Set<VEstrela> ladoDireitoB = new HashSet<>();
		ladoDireitoB.add(new VEstrela(selse, letraC));
		ladoDireitoB.add(new VEstrela(Simbolo.EPSILON));
		producoes.put(letraB, ladoDireitoB);
		
		Set<VEstrela> ladoDireitoE = new HashSet<>();
		ladoDireitoE.add(new VEstrela(sexp));
		producoes.put(letraE, ladoDireitoE);
		
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraC);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		
		assertTrue(GramaticaUtils.firstInterseccaoFollowVazia(first, follow));

		
	}

	@SuppressWarnings("unused")
	@Test
	public void verificarFirst3() {
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo a = new Simbolo("a", true);
		simbolosTerminais.add(a);
		Simbolo b = new Simbolo("b", true);
		simbolosTerminais.add(b);
		Simbolo c = new Simbolo("c", true);
		simbolosTerminais.add(c);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraS = new Simbolo("S", false);
		simbolosNaoTerminais.add(letraS);
		Simbolo letraB = new Simbolo("B", false);
		simbolosNaoTerminais.add(letraB);
		
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
				
		Set<VEstrela> ladoDireitoS = new HashSet<>();
		ladoDireitoS.add(new VEstrela(a, letraS, c));
		ladoDireitoS.add(new VEstrela(letraB));
		producoes.put(letraS, ladoDireitoS);
		
		Set<VEstrela> ladoDireitoB = new HashSet<>();
		ladoDireitoB.add(new VEstrela(letraB, b));
		ladoDireitoB.add(new VEstrela(Simbolo.EPSILON));
		producoes.put(letraB, ladoDireitoB);
		
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraS);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		
		assertTrue(GramaticaUtils.firstInterseccaoFollowVazia(first, follow));

		
		GramaticaUtils.estaFatorada(gramatica, first);

	}
	
	
	
	@SuppressWarnings("unused")
	@Test
	/* G: P 	 -> B Plinha
	 *    Plinha -> ; B Plinha | &
	 *    B		 -> K V C
	 *    K		 -> c K | &
	 *    V		 -> v V | &
	 *    C		 -> b D | Clinha
	 *    D	 	 -> K V ; C e Clinha | C e Clinha
	 *    Clinha -> com Clinha | &
	 * */
	public void verificarFirstG4t1() {
		
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo c = new Simbolo("c", true);
		simbolosTerminais.add(c);
		Simbolo v = new Simbolo("v", true);
		simbolosTerminais.add(v);
		Simbolo b = new Simbolo("b", true);
		simbolosTerminais.add(b);
		Simbolo com = new Simbolo("com", true);
		simbolosTerminais.add(com);
		Simbolo pontoVirgula = new Simbolo(";", true);
		simbolosTerminais.add(pontoVirgula);
		Simbolo e = new Simbolo("e", true);
		simbolosTerminais.add(e);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraP = new Simbolo("P", false);
		simbolosNaoTerminais.add(letraP);
		Simbolo letraPlinha = new Simbolo("P\'", false);
		simbolosNaoTerminais.add(letraPlinha);
		Simbolo letraB = new Simbolo("B", false);
		simbolosNaoTerminais.add(letraB);
		Simbolo letraK = new Simbolo("K", false);
		simbolosNaoTerminais.add(letraK);
		Simbolo letraV = new Simbolo("V", false);
		simbolosNaoTerminais.add(letraV);
		Simbolo letraC = new Simbolo("C", false);
		simbolosNaoTerminais.add(letraC);
		Simbolo letraD = new Simbolo("D", false);
		simbolosNaoTerminais.add(letraD);
		Simbolo letraClinha = new Simbolo("C\'", false);
		simbolosNaoTerminais.add(letraClinha);		

		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		//P -> BP'
		Set<VEstrela> ladoDireitoP = new HashSet<>();
		ladoDireitoP.add(new VEstrela(letraB, letraPlinha));
		
		producoes.put(letraP, ladoDireitoP);
		
		//P' -> ;BP' | &
		Set<VEstrela> ladoDireitoPlinha = new HashSet<>();
		ladoDireitoPlinha.add(new VEstrela(pontoVirgula, letraB, letraPlinha));
		ladoDireitoPlinha.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraPlinha, ladoDireitoPlinha);
		
		//B -> KVC
		Set<VEstrela> ladoDireitoB = new HashSet<>();
		ladoDireitoB.add(new VEstrela(letraK, letraV, letraC));
		
		producoes.put(letraB, ladoDireitoB);
		
		//K -> cK | &
		Set<VEstrela> ladoDireitoK = new HashSet<>();
		ladoDireitoK.add(new VEstrela(c, letraK));
		ladoDireitoK.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraK, ladoDireitoK);
		
		//V -> vV | &
		Set<VEstrela> ladoDireitoV = new HashSet<>();
		ladoDireitoV.add(new VEstrela(v, letraV));
		ladoDireitoV.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraV, ladoDireitoV);
		
		//C -> bD | C'
		Set<VEstrela> ladoDireitoC = new HashSet<>();
		ladoDireitoC.add(new VEstrela(b, letraD));
		ladoDireitoC.add(new VEstrela(letraClinha));
		
		producoes.put(letraC, ladoDireitoC);
		
		//D -> KV;CeC' | CeC'
		Set<VEstrela> ladoDireitoD = new HashSet<>();
		ladoDireitoD.add(new VEstrela(letraK, letraV, pontoVirgula, letraC, e, letraClinha));
		ladoDireitoD.add(new VEstrela(letraC, e, letraClinha));
		
		producoes.put(letraD, ladoDireitoD);
		
		//C' -> com C' | &
		Set<VEstrela> ladoDireitoClinha = new HashSet<>();
		ladoDireitoClinha.add(new VEstrela(com, letraClinha));
		ladoDireitoClinha.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraClinha, ladoDireitoClinha);
				
		//-----------------------------------------------------------------
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraP);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		
		assertFalse(GramaticaUtils.firstInterseccaoFollowVazia(first, follow));
		
		Map<Simbolo, List<AlfaNumero>> parser = GramaticaUtils.construirTabelaParsing(gramatica, first, follow);
		ArrayList<Simbolo> sentenca = new ArrayList<>();
		
		// Sentenca analisada: c v com ; (true)
		sentenca.add(c);
		sentenca.add(v);
		sentenca.add(com);
		sentenca.add(pontoVirgula);
		
		GramaticaUtils.reconheceSentenca(gramatica, parser, sentenca);
		
		assertTrue(GramaticaUtils.estaFatorada(gramatica, first));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void verificarInuteisG1() {
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo a = new Simbolo("a", true);
		simbolosTerminais.add(a);
		Simbolo b = new Simbolo("b", true);
		simbolosTerminais.add(b);
		Simbolo c = new Simbolo("c", true);
		simbolosTerminais.add(c);
		Simbolo d = new Simbolo("d", true);
		simbolosTerminais.add(d);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraS = new Simbolo("S", false);
		simbolosNaoTerminais.add(letraS);
		Simbolo letraA = new Simbolo("A", false);
		simbolosNaoTerminais.add(letraA);
		Simbolo letraB = new Simbolo("B", false);
		simbolosNaoTerminais.add(letraB);
		Simbolo letraC = new Simbolo("C", false);
		simbolosNaoTerminais.add(letraC);
		Simbolo letraD = new Simbolo("D", false);
		simbolosNaoTerminais.add(letraD);
		Simbolo letraE = new Simbolo("E", false);
		simbolosNaoTerminais.add(letraE);
		
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		
		Set<VEstrela> ladoDireitoS = new HashSet<>();
		ladoDireitoS.add(new VEstrela(a, letraS));
		ladoDireitoS.add(new VEstrela(letraB, letraC));
		ladoDireitoS.add(new VEstrela(letraB, letraD));
		
		producoes.put(letraS, ladoDireitoS);
		
		Set<VEstrela> ladoDireitoA = new HashSet<>();
		ladoDireitoA.add(new VEstrela(c, letraC));
		ladoDireitoA.add(new VEstrela(letraA, letraB));
		
		producoes.put(letraA, ladoDireitoA);
		
		Set<VEstrela> ladoDireitoB = new HashSet<>();
		ladoDireitoB.add(new VEstrela(b, letraB));
		ladoDireitoB.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraB, ladoDireitoB);
		
		Set<VEstrela> ladoDireitoC = new HashSet<>();
		ladoDireitoC.add(new VEstrela(a, letraA));
		ladoDireitoC.add(new VEstrela(letraB, letraC));
		
		producoes.put(letraC, ladoDireitoC);
		
		Set<VEstrela> ladoDireitoD = new HashSet<>();
		ladoDireitoD.add(new VEstrela(d, letraD, d));
		ladoDireitoD.add(new VEstrela(c));
		
		producoes.put(letraD, ladoDireitoD);
		
		Set<VEstrela> ladoDireitoE = new HashSet<>();
		ladoDireitoE.add(new VEstrela(c));
		
		producoes.put(letraE, ladoDireitoE);
		
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraS);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Gramatica semInfertil = GramaticaUtils.obterSemInferteis(gramatica);
		Gramatica semInalcancaveis = GramaticaUtils.obterSemInalcancaveis(semInfertil);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		assertFalse(GramaticaUtils.estaFatorada(gramatica, first));
		
		int i = 0;
	}
	
	@SuppressWarnings("unused")
	@Test
	public void verificarInuteisG2() {
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo a = new Simbolo("a", true);
		simbolosTerminais.add(a);
		Simbolo b = new Simbolo("b", true);
		simbolosTerminais.add(b);
		Simbolo c = new Simbolo("c", true);
		simbolosTerminais.add(c);
		Simbolo d = new Simbolo("d", true);
		simbolosTerminais.add(d);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraS = new Simbolo("S", false);
		simbolosNaoTerminais.add(letraS);
		Simbolo letraA = new Simbolo("A", false);
		simbolosNaoTerminais.add(letraA);
		Simbolo letraB = new Simbolo("B", false);
		simbolosNaoTerminais.add(letraB);
		Simbolo letraC = new Simbolo("C", false);
		simbolosNaoTerminais.add(letraC);
		Simbolo letraD = new Simbolo("D", false);
		simbolosNaoTerminais.add(letraD);
		Simbolo letraE = new Simbolo("E", false);
		simbolosNaoTerminais.add(letraE);
		
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		
		Set<VEstrela> ladoDireitoS = new HashSet<>();
		ladoDireitoS.add(new VEstrela(a, letraS));
		ladoDireitoS.add(new VEstrela(letraB, letraC));
		ladoDireitoS.add(new VEstrela(letraB, letraD));
		
		producoes.put(letraS, ladoDireitoS);
		
		Set<VEstrela> ladoDireitoA = new HashSet<>();
		ladoDireitoA.add(new VEstrela(c, letraC));
		ladoDireitoA.add(new VEstrela(letraA, letraB));
		
		producoes.put(letraA, ladoDireitoA);
		
		Set<VEstrela> ladoDireitoB = new HashSet<>();
		ladoDireitoB.add(new VEstrela(b, letraB));
		ladoDireitoB.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraB, ladoDireitoB);
		
		Set<VEstrela> ladoDireitoC = new HashSet<>();
		ladoDireitoC.add(new VEstrela(a, letraA));
		ladoDireitoC.add(new VEstrela(letraB, letraC));
		
		producoes.put(letraC, ladoDireitoC);
		
		Set<VEstrela> ladoDireitoD = new HashSet<>();
		ladoDireitoD.add(new VEstrela(d, letraD, d));
		ladoDireitoD.add(new VEstrela(c));
		
		producoes.put(letraD, ladoDireitoD);
		
		Set<VEstrela> ladoDireitoE = new HashSet<>();
		ladoDireitoE.add(new VEstrela(c));
		ladoDireitoE.add(new VEstrela(letraE));
		
		producoes.put(letraE, ladoDireitoE);
		
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraS);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Gramatica semInfertil = GramaticaUtils.obterSemInferteis(gramatica);
		Gramatica semInalcancaveis = GramaticaUtils.obterSemInalcancaveis(semInfertil);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		assertFalse(GramaticaUtils.estaFatorada(gramatica, first));
		
		int i = 0;
	}
	
	@SuppressWarnings("unused")
	@Test
	public void verificarEpsilonLivreG1() {
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo a = new Simbolo("a", true);
		simbolosTerminais.add(a);
		Simbolo b = new Simbolo("b", true);
		simbolosTerminais.add(b);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraS = new Simbolo("S", false);
		simbolosNaoTerminais.add(letraS);
		Simbolo letraA = new Simbolo("A", false);
		simbolosNaoTerminais.add(letraA);
		Simbolo letraB = new Simbolo("B", false);
		simbolosNaoTerminais.add(letraB);
		
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		
		Set<VEstrela> ladoDireitoS = new HashSet<>();
		ladoDireitoS.add(new VEstrela(letraA, letraB));
		
		producoes.put(letraS, ladoDireitoS);
		
		Set<VEstrela> ladoDireitoA = new HashSet<>();
		ladoDireitoA.add(new VEstrela(a, letraA));
		ladoDireitoA.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraA, ladoDireitoA);
		
		Set<VEstrela> ladoDireitoB = new HashSet<>();
		ladoDireitoB.add(new VEstrela(b, letraB));
		ladoDireitoB.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraB, ladoDireitoB);
		
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraS);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Gramatica semEpsilon = GramaticaUtils.obterEpsilonLivre(gramatica);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		assertTrue(GramaticaUtils.estaFatorada(gramatica, first));
		
		int i = 0;
	}
	
	@SuppressWarnings("unused")
	@Test
	public void verificarEpsilonLivreG2() {
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo a = new Simbolo("a", true);
		simbolosTerminais.add(a);
		Simbolo b = new Simbolo("b", true);
		simbolosTerminais.add(b);
		Simbolo c = new Simbolo("c", true);
		simbolosTerminais.add(c);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraS = new Simbolo("S", false);
		simbolosNaoTerminais.add(letraS);
		Simbolo letraA = new Simbolo("A", false);
		simbolosNaoTerminais.add(letraA);
		Simbolo letraB = new Simbolo("B", false);
		simbolosNaoTerminais.add(letraB);
		Simbolo letraC = new Simbolo("C", false);
		simbolosNaoTerminais.add(letraC);
		
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		
		Set<VEstrela> ladoDireitoS = new HashSet<>();
		ladoDireitoS.add(new VEstrela(c, letraS, c));
		ladoDireitoS.add(new VEstrela(letraB, letraA));
		
		producoes.put(letraS, ladoDireitoS);
		
		Set<VEstrela> ladoDireitoA = new HashSet<>();
		ladoDireitoA.add(new VEstrela(a, letraA));
		ladoDireitoA.add(new VEstrela(letraA, letraB, letraC));
		ladoDireitoA.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraA, ladoDireitoA);
		
		Set<VEstrela> ladoDireitoB = new HashSet<>();
		ladoDireitoB.add(new VEstrela(b, letraB));
		ladoDireitoB.add(new VEstrela(letraC, letraA));
		ladoDireitoB.add(new VEstrela(Simbolo.EPSILON));
		
		producoes.put(letraB, ladoDireitoB);
		
		Set<VEstrela> ladoDireitoC = new HashSet<>();
		ladoDireitoC.add(new VEstrela(c, letraC, c));
		ladoDireitoC.add(new VEstrela(letraA, letraS));
		
		producoes.put(letraC, ladoDireitoC);
		
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraS);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Gramatica semEpsilon = GramaticaUtils.obterEpsilonLivre(gramatica);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		assertFalse(GramaticaUtils.estaFatorada(gramatica, first));
		
		int i = 0;
	}
	
	@SuppressWarnings("unused")
	@Test
	public void verificarprodSimplesG1() {
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo a = new Simbolo("a", true);
		simbolosTerminais.add(a);
		Simbolo b = new Simbolo("b", true);
		simbolosTerminais.add(b);
		Simbolo c = new Simbolo("c", true);
		simbolosTerminais.add(c);
		Simbolo d = new Simbolo("d", true);
		simbolosTerminais.add(d);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraS = new Simbolo("S", false);
		simbolosNaoTerminais.add(letraS);
		Simbolo letraF = new Simbolo("F", false);
		simbolosNaoTerminais.add(letraF);
		Simbolo letraG = new Simbolo("G", false);
		simbolosNaoTerminais.add(letraG);
		Simbolo letraH = new Simbolo("H", false);
		simbolosNaoTerminais.add(letraH);
		
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		
		Set<VEstrela> ladoDireitoS = new HashSet<>();
		ladoDireitoS.add(new VEstrela(letraF, letraG, letraH));
		
		producoes.put(letraS, ladoDireitoS);
		
		Set<VEstrela> ladoDireitoF = new HashSet<>();
		ladoDireitoF.add(new VEstrela(a));
		ladoDireitoF.add(new VEstrela(letraG));
		
		producoes.put(letraF, ladoDireitoF);
		
		Set<VEstrela> ladoDireitoG = new HashSet<>();
		ladoDireitoG.add(new VEstrela(d, letraG));
		ladoDireitoG.add(new VEstrela(letraH));
		ladoDireitoG.add(new VEstrela(b));
		
		producoes.put(letraG, ladoDireitoG);
		
		Set<VEstrela> ladoDireitoH = new HashSet<>();
		ladoDireitoH.add(new VEstrela(c));
		
		producoes.put(letraH, ladoDireitoH);
		
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraS);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Gramatica semCiclos = GramaticaUtils.obterSemCiclos(gramatica);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		assertTrue(GramaticaUtils.estaFatorada(gramatica, first));
		
		int i = 0;
	}
	
	@SuppressWarnings("unused")
	@Test
	public void verificarprodSimplesG2() {
		Set<Simbolo> simbolosTerminais = new HashSet<>();
		Simbolo a = new Simbolo("a", true);
		simbolosTerminais.add(a);
		Simbolo b = new Simbolo("b", true);
		simbolosTerminais.add(b);
		Simbolo c = new Simbolo("c", true);
		simbolosTerminais.add(c);
		Simbolo d = new Simbolo("d", true);
		simbolosTerminais.add(d);
		
		Set<Simbolo> simbolosNaoTerminais = new HashSet<>();
		Simbolo letraS = new Simbolo("S", false);
		simbolosNaoTerminais.add(letraS);
		Simbolo letraF = new Simbolo("F", false);
		simbolosNaoTerminais.add(letraF);
		Simbolo letraG = new Simbolo("G", false);
		simbolosNaoTerminais.add(letraG);
		Simbolo letraH = new Simbolo("H", false);
		simbolosNaoTerminais.add(letraH);
		
		Map<Simbolo, Set<VEstrela>> producoes = new HashMap<>();
		
		
		Set<VEstrela> ladoDireitoS = new HashSet<>();
		ladoDireitoS.add(new VEstrela(a, letraS));
		ladoDireitoS.add(new VEstrela(letraF));
		
		producoes.put(letraS, ladoDireitoS);
		
		Set<VEstrela> ladoDireitoF = new HashSet<>();
		ladoDireitoF.add(new VEstrela(a));
		ladoDireitoF.add(new VEstrela(letraG));
		
		producoes.put(letraF, ladoDireitoF);
		
		Set<VEstrela> ladoDireitoG = new HashSet<>();
		ladoDireitoG.add(new VEstrela(letraH));
		ladoDireitoG.add(new VEstrela(b));
		
		producoes.put(letraG, ladoDireitoG);
		
		Set<VEstrela> ladoDireitoH = new HashSet<>();
		ladoDireitoH.add(new VEstrela(c));
		ladoDireitoH.add(new VEstrela(letraS));
		
		producoes.put(letraH, ladoDireitoH);
		
		Gramatica gramatica = new Gramatica();
		gramatica.setSimboloInicial(letraS);
		gramatica.setSimbolosTerminais(simbolosTerminais);
		gramatica.setSimbolosNaoTerminais(simbolosNaoTerminais);
		gramatica.setProducoes(producoes);
		
		Gramatica semCiclos = GramaticaUtils.obterSemCiclos(gramatica);
		
		Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
		Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
		
		Map<Simbolo, Set<Simbolo>> firstNT = GramaticaUtils.calcularFirstNT(gramatica, first);
		
		int i = 0;
	}
}
