import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestInput2 extends JFrame  {
    private JLabel winnerLabel;
    private JPanel tela;
    private JTextField textField;
    private String winnerName;

    public TestInput2() {
        tela = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(Color.BLACK);
                g.drawString("Parabéns, jogador X venceu, insira seu nome abaixo: ", 30, 10);
            }
        };
        winnerName = "";
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Nickname:");
        textField = new JTextField(20);
        JButton button = new JButton("OK");

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nickname = textField.getText();
                JOptionPane.showMessageDialog(null, "Nickname: " + nickname);
            }
        });


    }


}