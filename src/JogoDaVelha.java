import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class JogoDaVelha extends JFrame {

	ImageIcon iconCirculo = new ImageIcon(getClass().getResource("circulo.png"));
	ImageIcon iconX = new ImageIcon(getClass().getResource("x.png"));
	JPanel pTela = new JPanel(new GridLayout(3, 3, 10, 10));

	Bloco[] blocos = new Bloco[9];
	int rodadas = 0;

	final int JOGADOR_1 = 1;
	final int JOGADOR_2 = 2;

	int jogadorVez = JOGADOR_1;

	JLabel lInforma��o = new JLabel("Jogador " + JOGADOR_1);

	public JogoDaVelha() {
		configurarJanela();
		configurarTela();
	}

	public void configurarTela() {
		add(BorderLayout.CENTER, pTela);
		add(BorderLayout.NORTH, lInforma��o);
		pTela.setBackground(Color.BLACK);
		lInforma��o.setFont(new Font("Arial", Font.BOLD, 30));
		lInforma��o.setForeground(new Color(50, 200, 50));
		lInforma��o.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < 9; i++) {
			Bloco bloco = new Bloco();
			blocos[i] = bloco;
			pTela.add(bloco);

		}

	}

	public void mudarVez() {
		if (jogadorVez == 1) {
			jogadorVez = 2;
			lInforma��o.setText("Jogador 2");
			lInforma��o.setForeground(Color.RED);
		} else {
			jogadorVez = 1;
			lInforma��o.setText("Jogador 1");
			lInforma��o.setForeground(new Color(50, 200, 50));
		}
	}

	public boolean testarVitoria(int jog) {
		if (blocos[0].quem == jog && blocos[1].quem == jog && blocos[2].quem == jog) {
			return true;
		}
		if (blocos[3].quem == jog && blocos[4].quem == jog && blocos[5].quem == jog) {
			return true;
		}
		if (blocos[6].quem == jog && blocos[7].quem == jog && blocos[8].quem == jog) {
			return true;
		}
		if (blocos[0].quem == jog && blocos[3].quem == jog && blocos[6].quem == jog) {
			return true;
		}
		if (blocos[1].quem == jog && blocos[4].quem == jog && blocos[7].quem == jog) {
			return true;
		}
		if (blocos[2].quem == jog && blocos[5].quem == jog && blocos[8].quem == jog) {
			return true;
		}
		if (blocos[0].quem == jog && blocos[4].quem == jog && blocos[8].quem == jog) {
			return true;
		}
		if (blocos[6].quem == jog && blocos[4].quem == jog && blocos[2].quem == jog) {
			return true;
		}

		return false;
	}

	public void configurarJanela() {
		setTitle("Jogo da Velha");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public static void main(String[] args) {
		new JogoDaVelha();
	}

	public class Bloco extends JButton {
		int quem = 0;

		public Bloco() {
			setBackground(Color.WHITE);
			addActionListener(e -> {
				//JOptionPane.showMessageDialog(rootPane,"ok");
				if (quem == 0) {
					if (jogadorVez == JOGADOR_1) {

						setIcon(iconCirculo);
						quem = JOGADOR_1;
					} else {
						setIcon(iconX);
						quem = JOGADOR_2;
					}
					if (testarVitoria(quem)) {
						JOptionPane.showMessageDialog(rootPane, "Jogador " + quem + " Ganhou");
						System.exit(0);
					}
					if (rodadas == 9) {
						JOptionPane.showMessageDialog(rootPane, "Deu velha!");
						System.exit(0);
					}
					rodadas++;
					mudarVez();
				}

			});
		}
	}

}
