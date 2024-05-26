import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Ranking extends JFrame {
    private boolean rep = true; // Variável de controle para permitir apenas uma adição de jogador
    private Font fonte; // Objeto de fonte para configurar a aparência do texto
    private ArrayList<Jogador> jogadores; // Lista para armazenar os jogadores
    private JList<String> rankingLista; // Componente gráfico para exibir o ranking
    private int i = 0; // Variável de iteração (não utilizada no código atual)

    private Ponto ponto; // Objeto Ponto usado para obter a pontuação

    public Ranking(Ponto ponto) {
        jogadores = new ArrayList<>(); // Inicialização da lista de jogadores

        setTitle("Ranking"); // Configura o título da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configura a ação de fechar a janela
        setSize(600, 600); // Define o tamanho da janela
        setLayout(new BorderLayout()); // Define o layout da janela como BorderLayout
        setVisible(true); // Torna a janela visível

        JLabel Titulo = new JLabel("Ranking"); // Cria um rótulo para o título
        Titulo.setHorizontalAlignment(SwingConstants.CENTER); // Define o alinhamento do rótulo como centralizado

        rankingLista = new JList<>(); // Cria um componente JList para exibir o ranking
        rankingLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Define o modo de seleção como seleção única

        JButton adicionarBotao = new JButton("Adicione seu nome"); // Cria um botão "Adicione seu nome"
        adicionarBotao.addActionListener(new ActionListener() { // Adiciona um ouvinte de ação ao botão
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarJogador(ponto); // Chama o método para adicionar um jogador
                atualizarRanking(); // Atualiza o ranking após adicionar o jogador
            }
        });

        JPanel panelBotao = new JPanel(); // Cria um painel para o botão
        panelBotao.setLayout(new FlowLayout()); // Define o layout do painel como FlowLayout
        panelBotao.add(adicionarBotao); // Adiciona o botão ao painel

        add(Titulo, BorderLayout.NORTH); // Adiciona o rótulo do título à região norte do layout da janela
        add(new JScrollPane(rankingLista), BorderLayout.CENTER); // Adiciona a lista de ranking em um painel de rolagem à região central
        add(panelBotao, BorderLayout.SOUTH); // Adiciona o painel do botão à região sul

        Texto(); // Configura a aparência do texto da lista de ranking
        load(); // Carrega os dados dos jogadores
        atualizarRanking(); // Atualiza o ranking exibido na interface gráfica
    }

    private void adicionarJogador(Ponto ponto) {
        if (rep) { // Verifica se ainda é permitido adicionar jogadores
            String nome = JOptionPane.showInputDialog("Digite o nome do jogador:"); // Solicita o nome do jogador através de uma caixa de diálogo
            rep = false; // Desativa a possibilidade de adicionar mais jogadores
            Jogador jogador = new Jogador(nome, ponto.getPonto()); // Cria um novo objeto Jogador com o nome e a pontuação fornecidos
            jogadores.add(jogador); // Adiciona o jogador à lista de jogadores
            Save(); // Salva os dados dos jogadores
            JOptionPane.showMessageDialog(this, "Pontuação foi adicionada"); // Exibe uma mensagem de confirmação
        }
    }

    private void atualizarRanking() {
        Collections.sort(jogadores, new Jogador("", 0)); // Classifica a lista de jogadores com base na pontuação

        ArrayList<String> rank = new ArrayList<>(); // Cria uma lista para armazenar as informações do ranking
        for (int i = 0; i < Math.min(jogadores.size(), 10); i++) { // Percorre os jogadores com base no tamanho da lista ou no máximo de 10 jogadores
            Jogador jogador = jogadores.get(i); // Obtém o jogador atual
            rank.add(jogador.getNome() + " = " + jogador.getPontuacao()); // Adiciona a informação do jogador à lista de ranking
        }

        rankingLista.setListData(rank.toArray(new String[0])); // Atualiza os dados exibidos na lista de ranking
    }

    private void Texto() {
        fonte = new Font("Consolas", Font.PLAIN, 24); // Cria uma nova fonte com o nome "Consolas", estilo plano e tamanho 24
        rankingLista.setFont(fonte); // Define a fonte do componente JList como a fonte criada
    }

    private void Save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\USER\\Desktop\\Códigos\\JAVA\\PingPong\\output.txt"))) {
            for (Jogador jogador : jogadores) { // Percorre os jogadores da lista
                writer.write(jogador.getNome() + " = " + jogador.getPontuacao()); // Escreve o nome e a pontuação do jogador no arquivo
                writer.newLine(); // Escreve uma nova linha
            }
            JOptionPane.showMessageDialog(this, "Dados salvos"); // Exibe uma mensagem de confirmação
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a pilha de exceções (tratamento de erro)
        }
    }

    private void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\USER\\Desktop\\Códigos\\JAVA\\PingPong\\output.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) { // Lê cada linha do arquivo
                String[] parts = line.split("="); // Divide a linha em duas partes usando o caractere "=" como separador
                String name = parts[0].trim(); // Obtém o nome do jogador removendo espaços em branco
                int pont = Integer.parseInt(parts[1].trim()); // Obtém a pontuação do jogador convertendo para um número inteiro
                Jogador jogador = new Jogador(name, pont); // Cria um novo objeto Jogador com o nome e a pontuação obtidos
                jogadores.add(jogador); // Adiciona o jogador à lista de jogadores
            }
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a pilha de exceções (tratamento de erro)
        }
    }

    private static boolean verificarNomeExistente(String nome, String nomeArquivo) {
        try {
            return Files.lines(Paths.get(nomeArquivo))
                    .anyMatch(line -> line.trim().equalsIgnoreCase(nome));
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a pilha de exceções (tratamento de erro)
            return false;
        }
    }

    private class Jogador implements Comparator<Jogador> {
        private String nome; // Nome do jogador
        private int pontuacao; // Pontuação do jogador

        public Jogador(String nome, int pontuacao) {
            this.nome = nome;
            this.pontuacao = pontuacao;
        }

        public String getNome() {
            return nome;
        }

        public int getPontuacao() {
            return pontuacao;
        }

        @Override
        public int compare(Jogador jogador1, Jogador jogador2) {
            return Integer.compare(jogador2.getPontuacao(), jogador1.getPontuacao()); // Compara as pontuações de dois jogadores para classificação
        }
    }

}
