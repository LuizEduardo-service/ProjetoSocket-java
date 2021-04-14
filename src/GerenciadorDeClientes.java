import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorDeClientes extends Thread {

	private Socket cliente;
	private String nomeCliente;
	private BufferedReader leitor;
	private PrintWriter escritor;
	private static final Map<String, GerenciadorDeClientes> clientes = new HashMap<String, GerenciadorDeClientes>();

	public GerenciadorDeClientes(Socket cliente) {
		this.cliente = cliente;
		start();

	}

	@Override
	public void run() {
		try {
			leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			escritor = new PrintWriter(cliente.getOutputStream(), true);
			escritor.println(comandos.LOGIN);
				efetuarLogin();
				String msg;

			while (true) {
				 msg = leitor.readLine();
				if (msg.equalsIgnoreCase(comandos.SAIR)) {
					this.cliente.close();
				} else if (msg.startsWith(comandos.MENSAGEM)) {
					String nomeDestinatario = msg.substring(comandos.MENSAGEM.length(), msg.length());
					System.out.println("Enviado para " + nomeDestinatario);
					GerenciadorDeClientes destinatario = clientes.get(nomeDestinatario);

					if (destinatario == null) {
						escritor.println("O cliente informado não existe!");
					} else {

						destinatario.getEscritor().println(this.nomeCliente + " disse: " + leitor.readLine());
					}
					// lista os nomes dos clientes
				} else if (msg.equals(comandos.LISTA_USUARIOS)) {
					atualizarListaUsuarios(this);
				} else {
					escritor.println(this.nomeCliente + ", disse: " + msg);
				}

			}
		} catch (IOException e) {
			System.err.println("Conexão fechada!");
			e.printStackTrace();
		}
	}
	
	private void efetuarLogin() throws IOException {
		String msg = leitor.readLine();
		this.nomeCliente = msg.toLowerCase().replaceAll(",", "");
		escritor.println(comandos.LOGIN_ACEITO);
		escritor.println("ola: " + this.nomeCliente);
		// this.nomeCliente
		clientes.put(this.nomeCliente, this);
		
		for(String cliente: clientes.keySet()) {
			atualizarListaUsuarios(clientes.get(cliente));
		}
		
	}

	private void atualizarListaUsuarios(GerenciadorDeClientes gerenciadorDeClientes) {
		StringBuffer str = new StringBuffer();
		for (String c : clientes.keySet()) {
			str.append(c);
			str.append(",");
		}
		str.delete(str.length() - 1, str.length());
		gerenciadorDeClientes.getEscritor().println(comandos.LISTA_USUARIOS);
		gerenciadorDeClientes.getEscritor().println(str.toString());

		
	}

	public PrintWriter getEscritor() {
		return escritor;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

}
