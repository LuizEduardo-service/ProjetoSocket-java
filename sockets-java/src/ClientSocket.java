import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientSocket {

	public static void main(String[] args) {
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
							
							if(msg ==null|| msg.length()==0) {
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
				if(mensagemTerminal.equalsIgnoreCase("::SAIR")) {
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
