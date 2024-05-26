import java.awt.*;

public class Ponto extends Texto{
    public static final int TAMANHO_FONTE = 60;
    public static final Font fonte = new Font("Consolas", Font.PLAIN, TAMANHO_FONTE);

    private short ponto;

    public Ponto() {
        super.setFonte(fonte);
    }

    public short getPonto() {
        return ponto;
    }

    public void setPonto(short ponto) {
        this.ponto = ponto;
    }

    public void add() {
        ponto++;
    }

    @Override
    public void desenha(Graphics2D g) {
        super.desenha(g, Short.toString(ponto), getPx(), getPy());
    }
}
