package chatmultiplosclientes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("localhost", 8189);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		new Thread(new Runnable() {
			@Override
			public void run() {
				String textoEntrada;
				while (socket.isConnected()) {
					try {
						textoEntrada = bufferedReader.readLine();
						System.out.println(textoEntrada);
					} catch (IOException e) {
						System.out.println(e);
					}
				}
			}
		}).start();

		Scanner scanner = new Scanner(System.in);
		while (socket.isConnected()) {
			String textoAEnviar = scanner.nextLine();
			bufferedWriter.write(textoAEnviar);
			bufferedWriter.newLine();
			bufferedWriter.flush();

			if (textoAEnviar.equals("BYE")) {
				break;
			}
		}
		
		scanner.close();
		socket.close();
		bufferedReader.close();
		bufferedWriter.close();
	}

}
