import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientSocketSwing extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4703724726832449262L;
	private JTextArea taEditor = new JTextArea("Digite aqui a sua Mensagem!");
	private JTextArea taVisor = new JTextArea();
	private JList liUsuarios = new JList();

	public ClientSocketSwing() {
		setTitle("Chat com sockets");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		liUsuarios.setBackground(Color.BLACK);
		taEditor.setBackground(Color.CYAN);

		taEditor.setPreferredSize(new Dimension(300, 40));
		//taVisor.setPreferredSize(new Dimension(350, 100));
		taVisor.setEditable(false);
		liUsuarios.setPreferredSize(new Dimension(50, 140));

		add(taEditor, BorderLayout.SOUTH);
		add(new JScrollPane(taVisor), BorderLayout.CENTER);
		add(liUsuarios, BorderLayout.WEST);

		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		tratarEventos();
		String[] usuarios=new String[] {"elvis","maria"};
		preencherListaUsuarios();
	}

	private void preencherListaUsuarios() {
		// TODO Auto-generated method stub
		
	}

	private void tratarEventos() {
		taEditor.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// inserindo msg na tela
					taVisor.append(taEditor.getText());

					// limpando o editor
					taEditor.setText("");
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	public static void main(String[] args) {
		new ClientSocketSwing();
	}

	public static void main2(String[] args) {

		try {
			final Socket cliente = new Socket("127.0.0.1", 9999);

			// lendo para o servidor
			new Thread() {
				@Override
				public void run() {
					try {
						BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

						while (true) {
							String msg = leitor.readLine();

							if (msg == null || msg.length() == 0) {
								continue;
							}
							System.out.println("Mensagem do servidor: " + msg);
						}
					} catch (IOException e) {
						System.out.println("Impossivel ler msg do servidor");
						e.printStackTrace();
					}
				}

			}.start();

			// escrevendo para o servidor
			PrintWriter escritor = new PrintWriter(cliente.getOutputStream(), true);
			BufferedReader leitorTerm = new BufferedReader(new InputStreamReader(System.in));
			String mensagemTerminal = "";
			while (true) {
				mensagemTerminal = leitorTerm.readLine();
				if (mensagemTerminal == null || mensagemTerminal.length() == 0) {
					continue;
				}

				escritor.println(mensagemTerminal);
				if (mensagemTerminal.equalsIgnoreCase("::SAIR")) {
					System.exit(0);
				}
			}

		} catch (UnknownHostException e) {
			System.err.println("Endereço passado é invalido!!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Servidor pode estar fora do ar!!");
			e.printStackTrace();

		}

	}

}
