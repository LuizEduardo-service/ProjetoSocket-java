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

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
	private PrintWriter escritor;
	private BufferedReader leitor;

	public ClientSocketSwing() {
		setTitle("Chat com sockets");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		liUsuarios.setBackground(Color.BLACK);
		taEditor.setBackground(Color.CYAN);

		taEditor.setPreferredSize(new Dimension(300, 40));
		// taVisor.setPreferredSize(new Dimension(350, 100));
		taVisor.setEditable(false);
		liUsuarios.setPreferredSize(new Dimension(80, 140));

		add(taEditor, BorderLayout.SOUTH);
		add(new JScrollPane(taVisor), BorderLayout.CENTER);
		add(new JScrollPane(liUsuarios), BorderLayout.WEST);

		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		iniciarEscritor();
		String[] usuarios = new String[] { "elvis", "maria" };
		preencherListaUsuarios(usuarios);
	}

	// PREENCHE A LISTA DE CLIENTES
	private void preencherListaUsuarios(String[] usuarios) {
		DefaultListModel modelo = new DefaultListModel();
		liUsuarios.setModel(modelo);
		for (String usuario : usuarios) {
			modelo.addElement(usuario);
		}

	}

	// INICIA ESCRITOR
	private void iniciarEscritor() {
		taEditor.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					// escrevendo para o servidor
					if (taVisor.getText().isEmpty()) {
						return;
					}

					Object usuario = liUsuarios.getSelectedValue();

					if (usuario != null) {
						// inserindo msg na tela
						taVisor.append(taEditor.getText());

						escritor.println(comandos.MENSAGEM + usuario);
						escritor.println(taVisor.getText());

						// limpando o editor
						taEditor.setText("");

					} else {
						if (taVisor.getText().equalsIgnoreCase(comandos.SAIR)) {
							System.exit(0);
						}
						JOptionPane.showMessageDialog(ClientSocketSwing.this, "Selecione um usuario");
						return;
					}

				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void iniciarChat() {

		try {
			final Socket cliente = new Socket("127.0.0.1", 9999);
			escritor = new PrintWriter(cliente.getOutputStream(), true);
			leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

		} catch (UnknownHostException e) {
			System.err.println("Endereço passado é invalido!!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Servidor pode estar fora do ar!!");
			e.printStackTrace();

		}

	}

	public static void main(String[] args) {
		final ClientSocketSwing cliente = new ClientSocketSwing();
		cliente.iniciarChat();
		cliente.iniciarEscritor();
		
		cliente.atualizarListaUsuario();
		cliente.iniciarLeitor();

	}

	private void atualizarListaUsuario() {
		escritor.println(comandos.LISTA_USUARIOS);

	}

	private void iniciarLeitor() {
		try {
			while (true) {
				String mensagem;
				mensagem = leitor.readLine();
				if (mensagem == null || mensagem.isEmpty())
					continue;

				// valida se oque foi escrito é um comando / recebe o texto
				if (mensagem.equals(comandos.LISTA_USUARIOS)) {
					String[] usuarios =leitor.readLine().split(",");
					preencherListaUsuarios(usuarios);
				} else if (mensagem.equals(comandos.LOGIN)) {
					String login = JOptionPane.showInputDialog("Qual o seu Login? ");
					escritor.println(login);
					
				}else if(mensagem.equals(comandos.LOGIN_ACEITO)){
					atualizarListaUsuario();	
				}
				else {
					taVisor.append(mensagem);
					taVisor.append("\n");
				}
			}
		} catch (IOException e) {
			System.out.println("Impossivel ler a mensagem do servidor!");
			e.printStackTrace();
		}

	}

	private DefaultListModel getListaUsuarios() {
		return (DefaultListModel) liUsuarios.getModel();
	}

}
