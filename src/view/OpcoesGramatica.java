package view;

import java.awt.Container;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

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
import model.GramaticaUtils;
import utils.GramaticaParser;

public class OpcoesGramatica extends JFrame {

	private static final long serialVersionUID = 1L;

	public OpcoesGramatica() {
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
        JButton first = new JButton("First(A)");
        JButton follow = new JButton("Follow(A)");
        JButton firstNT = new JButton("FirstNT(A)");
        JButton LL1 = new JButton("Verificar LL(1)");
        LL1.addActionListener(event -> {
        	Gramatica gramatica = GramaticaParser.textToGramatica(conteudoGramatica.getText());
        	GramaticaUtils.isGramaticaLL1(gramatica);
        });
        
        JButton tabela = new JButton("Construir Tabela de Parsing");
        JButton analise = new JButton("Fazer análise sintática");
        
        toolbar1.add(newb);
        toolbar1.add(openb);
        toolbar1.add(saveb);
        
        toolbar2.add(propria);
        toolbar2.add(first);
        toolbar2.add(follow);
        toolbar2.add(firstNT);
        toolbar2.add(LL1);
        toolbar2.add(tabela);
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
     	OpcoesGramatica panel = new OpcoesGramatica();
		panel.setTitle("Formais");
//      panel.setSize(1000, 500);
        panel.pack();
        panel.setLocationRelativeTo(null);
        panel.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setVisible(true);
	}
	
}
