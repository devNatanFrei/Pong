import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Ranking extends JFrame {
    private Font fonte;
    private ArrayList<Jogador> jogadores;
    private JList<String> rankingLista;
    private JButton adicionarBotao; // Movi a declaração para o escopo da classe

    public Ranking(Ponto ponto) {
        jogadores = new ArrayList<>();

        setTitle("Ranking - Fim de Jogo");

        // CORREÇÃO PRINCIPAL: Encerra toda a aplicação ao fechar esta janela.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(400, 500);
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setLayout(new BorderLayout(10, 10));

        JLabel tituloLabel = new JLabel("Ranking dos Melhores", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24));

        rankingLista = new JList<>();
        rankingLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        adicionarBotao = new JButton("Adicione seu nome");
        adicionarBotao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarJogador(ponto);
                atualizarRanking();
            }
        });

        JPanel panelBotao = new JPanel(new FlowLayout());
        panelBotao.add(adicionarBotao);

        // Adiciona um pouco de margem
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(tituloLabel, BorderLayout.NORTH);
        add(new JScrollPane(rankingLista), BorderLayout.CENTER);
        add(panelBotao, BorderLayout.SOUTH);

        configurarFonte();
        load(); // Carrega os dados dos jogadores do arquivo
        atualizarRanking(); // Atualiza o ranking exibido na interface

        setVisible(true); // Torna a janela visível
    }

    private void adicionarJogador(Ponto ponto) {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do jogador:", "Salvar Pontuação", JOptionPane.PLAIN_MESSAGE);

        // Verifica se o usuário inseriu um nome e não clicou em "Cancelar"
        if (nome != null && !nome.trim().isEmpty()) {
            Jogador jogador = new Jogador(nome.trim(), ponto.getPonto());
            jogadores.add(jogador);
            save(); // Salva a lista atualizada
            JOptionPane.showMessageDialog(this, "Pontuação adicionada com sucesso!");

            // MELHORIA: Desabilita o botão para não adicionar o nome novamente
            adicionarBotao.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum nome inserido. Sua pontuação não foi salva.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarRanking() {
        // Classifica a lista de jogadores em ordem decrescente de pontuação
        Collections.sort(jogadores, new JogadorComparator());

        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < Math.min(jogadores.size(), 10); i++) {
            Jogador jogador = jogadores.get(i);
            model.addElement((i + 1) + ". " + jogador.getNome() + " - " + jogador.getPontuacao() + " pontos");
        }

        rankingLista.setModel(model);
    }

    private void configurarFonte() {
        fonte = new Font("Consolas", Font.PLAIN, 18);
        rankingLista.setFont(fonte);
    }

    private void save() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            for (Jogador jogador : jogadores) {
                writer.write(jogador.getNome() + ";" + jogador.getPontuacao()); // Usar ";" como separador é mais seguro que "="
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o ranking: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void load() {
        File rankingFile = new File("output.txt");
        if (!rankingFile.exists()) {
            return; // Se o arquivo não existe, não há nada a carregar.
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rankingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";"); // Divide a linha usando ";"
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int pont = Integer.parseInt(parts[1].trim());
                    jogadores.add(new Jogador(name, pont));
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o ranking: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // A classe Jogador pode ser interna e privada
    private class Jogador {
        private String nome;
        private int pontuacao;

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
    }

    // É melhor ter uma classe separada para o Comparator
    private class JogadorComparator implements Comparator<Jogador> {
        @Override
        public int compare(Jogador j1, Jogador j2) {
            // Compara para ordenar da maior pontuação para a menor
            return Integer.compare(j2.getPontuacao(), j1.getPontuacao());
        }
    }
}