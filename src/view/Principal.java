package view;

import java.awt.Container;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import model.Gramatica;
import model.Simbolo;
import model.VEstrela;
import utils.GramaticaParser;
import utils.GramaticaUtils;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;

	public Principal() {
		inicializar();
	}

    public void inicializar() {
    	createToolBars();
    }
	
	private void createToolBars() {
        JToolBar toolbar1 = new JToolBar();
        JToolBar toolbar2 = new JToolBar();

        JPanel panelGramatica = new JPanel();

        JLabel titulo = new JLabel("Digite sua Gramática:");
        JTextArea conteudoGramatica = new JTextArea(12, 24);
        conteudoGramatica.setEditable(true);
        conteudoGramatica.setFont(new Font("Times New Roman", Font.PLAIN, 20));
     
        panelGramatica.add(titulo);
        panelGramatica.add(conteudoGramatica);
 
        // ao fazer essas ações, atualizar a gramatica vingente para os algoritmos
        // toda vez que rodar os algoritmos, pegar o conteudo corrente do TextArea!
        JButton newb = new JButton("Nova");
        newb.addActionListener(event -> {
        	conteudoGramatica.setText("");
        });
        
        JButton openb = new JButton("Abrir");
        openb.addActionListener(event -> {
        	JFileChooser openFile = new JFileChooser();
            openFile.showOpenDialog(null);
            File file = openFile.getSelectedFile(); 
            String conteudo;
            try {
                byte bt[]= Files.readAllBytes(file.toPath());   
                conteudo = new String(bt, "UTF-8");
                conteudoGramatica.setText(conteudo);
            } catch (IOException ex) {
            	JOptionPane.showMessageDialog(null, "Erro ao tentar ler o arquivo.");
            }
        });
        
        JButton saveb = new JButton("Salvar");
        saveb.addActionListener(event -> {
        	JFileChooser fileChooser = new JFileChooser();
        	fileChooser.setDialogTitle("Escolha um destino");   
        	int userSelection = fileChooser.showSaveDialog(this);
        	 
        	if (userSelection == JFileChooser.APPROVE_OPTION) {
        	    File fileToSave = fileChooser.getSelectedFile();
        	    try {
					PrintWriter out = new PrintWriter(fileToSave.getAbsolutePath());
					out.print(conteudoGramatica.getText());
					out.close();
				} catch (FileNotFoundException e) {
					 JOptionPane.showMessageDialog(null, "Arquivo não pode ser salvo.");
				}
        	}
        });
        

        JButton propria = new JButton("Gerar Própria");
        propria.addActionListener(event -> {
        	Gramatica gramatica = GramaticaParser.textToGramatica(conteudoGramatica.getText());
        	Gramatica gramaticaPropria = GramaticaUtils.obterPropria(gramatica);
        	String gramaticaText = GramaticaParser.gramaticaToText(gramaticaPropria);
        	conteudoGramatica.setText(gramaticaText);
        	System.out.println(gramatica);
        	System.out.println(gramaticaPropria);
        	
        	System.out.println(gramaticaText);
        	JOptionPane.showMessageDialog(null, "Gramática própria gerada com sucesso.");
        });
        
        JButton firstButton = new JButton("First(A)");
        firstButton.addActionListener(event -> {
        	String conteudoComponente = conteudoGramatica.getText();
        	Gramatica gramatica = GramaticaParser.textToGramatica(conteudoComponente);
        	Map<Simbolo, VEstrela> firstTodagramatica = GramaticaUtils.calcularFirst(gramatica);
        	new PainelGenerico(new TableModelFirst(firstTodagramatica));
        });
        
        JButton followButton = new JButton("Follow(A)");
        followButton.addActionListener(event -> {
        	String conteudoComponente = conteudoGramatica.getText();
        	Gramatica gramatica = GramaticaParser.textToGramatica(conteudoComponente);
        	Map<Simbolo, VEstrela> firstTodaGramatica = GramaticaUtils.calcularFirst(gramatica);
        	Map<Simbolo, VEstrela> followTodaGramatica = GramaticaUtils.calcularFollow(gramatica, firstTodaGramatica);
        	new PainelGenerico(new TabelaModelFollow(followTodaGramatica));
        });
        
        JButton firstNTButton = new JButton("FirstNT(A)");
        firstNTButton.addActionListener(event -> {
        	String conteudoComponente = conteudoGramatica.getText();
        	Gramatica gramatica = GramaticaParser.textToGramatica(conteudoComponente);
        	Map<Simbolo, VEstrela> firstTodaGramatica = GramaticaUtils.calcularFirst(gramatica);
        	Map<Simbolo, List<Simbolo>> firstNTGramatica = GramaticaUtils.calcularFirstNT(gramatica, firstTodaGramatica);
        	new PainelGenerico(new TabelaModelFirstNT(firstNTGramatica));
        });
        
        
        JButton LL1Button = new JButton("Verificar LL(1)");
        LL1Button.addActionListener(event -> {
        	Gramatica gramatica = GramaticaParser.textToGramatica(conteudoGramatica.getText());
        	if (GramaticaUtils.isGramaticaLL1(gramatica)) {
        		JOptionPane.showMessageDialog(null, "A gramática é LL(1).");
        	} else {
        		JOptionPane.showMessageDialog(null, "A gramática não é LL(1).");
        	}
        });
        
        JButton parsingButton = new JButton("Construir Tabela de Parsing");
        parsingButton.addActionListener(event -> {
        	String conteudoComponente = conteudoGramatica.getText();
        	Gramatica gramatica = GramaticaParser.textToGramatica(conteudoComponente);
        	Map<Simbolo, VEstrela> first = GramaticaUtils.calcularFirst(gramatica);
        	Map<Simbolo, VEstrela> follow = GramaticaUtils.calcularFollow(gramatica, first);
        	Map<Simbolo, List<VEstrela>> parsing = GramaticaUtils.construirTabelaParsing(gramatica, first, follow);

        	
        	new PainelGenerico(new TabelaModelParsing(parsing, gramatica.getSimbolosTerminais()));        	
        });
        
        JButton analise = new JButton("Fazer análise sintática");
        
        toolbar1.add(newb);
        toolbar1.add(openb);
        toolbar1.add(saveb);
        
        toolbar2.add(propria);
        toolbar2.add(firstButton);
        toolbar2.add(followButton);
        toolbar2.add(firstNTButton);
        toolbar2.add(LL1Button);
        toolbar2.add(parsingButton);
        toolbar2.add(analise);
        
        createLayout(toolbar1, toolbar2, panelGramatica);
    }

    private void createLayout(JComponent... arg) {
        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addComponent(arg[0], GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(arg[1]).addComponent(arg[2])
        );

        gl.setVerticalGroup(gl.createSequentialGroup().addComponent(arg[0]).addComponent(arg[1]).addComponent(arg[2]));
        
        
    }	
	
	public static void main(String[] args) {
     	Principal panel = new Principal();
		panel.setTitle("Formais");
//      panel.setSize(1000, 500);
        panel.pack();
        panel.setLocationRelativeTo(null);
        panel.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setVisible(true);
	}
	
}
