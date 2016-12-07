package view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Gramatica;
import model.Simbolo;
import model.VEstrela;
import utils.GramaticaUtils;

public class PainelGenerico extends JFrame {

	private JTable tabela;
	private JScrollPane scrollPane;

	public PainelGenerico(DefaultTableModel model) {
		this.tabela = new JTable(model);
		this.scrollPane = new JScrollPane(this.tabela);
		this.iniciar();
	}

	public void iniciar() {
		this.setTitle("Resultado");
		this.setSize(500, 500);
		// this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);

		JPanel tabelaPane = new JPanel();
		tabelaPane.setLayout(new BoxLayout(tabelaPane, BoxLayout.Y_AXIS));
		tabelaPane.add(this.scrollPane);

		this.getContentPane().add(this.scrollPane, BorderLayout.CENTER);
	}

	public void adicionarBotao(Gramatica gramatica) {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		JButton reconhecer = new JButton("Fazer análise");
		reconhecer.addActionListener(event -> {
			String sentencaDigitada = JOptionPane.showInputDialog(this, "Digite a sentença, com os simbolos separados por espaço: ");
			if (!sentencaDigitada.equals("")) {
				Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
				Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
				Map<Simbolo, List<VEstrela>> parsing = GramaticaUtils.construirTabelaParsing(gramatica, first, follow);

				List<String> sentencaDividida = new ArrayList<>(Arrays.asList(sentencaDigitada.split(" ")));
				List<Simbolo> sentenca = new ArrayList<>();

				for (String letra : sentencaDividida) {
					sentenca.add(new Simbolo(letra, true));
				}

				if (GramaticaUtils.reconheceSentenca(gramatica, parsing, sentenca)) {
					JOptionPane.showMessageDialog(null, "A sentença digitada é gerada pela gramática.");
				} else {
					JOptionPane.showMessageDialog(null, "A sentença digitada não é gerada pela gramática.");
				}

			}
		});
		buttonPane.add(reconhecer);

		this.getContentPane().add(buttonPane, BorderLayout.PAGE_END);
	}

}
