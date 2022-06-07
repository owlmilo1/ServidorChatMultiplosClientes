package chatmultiplosclientes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public static void main(String[] args) {
		int i = 1;
		try {
			ServerSocket serverSocket = new ServerSocket(8189);

			while (!serverSocket.isClosed()) {
				Socket incoming = serverSocket.accept();
				System.out.println("A new client has connected!");
				new ThreadedEchoHandler(incoming, i).start();
				i++;
			}
			
			serverSocket.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

class ThreadedEchoHandler extends Thread {

	public static ArrayList<Socket> clientes = new ArrayList<Socket>();
	
	private Socket incoming;
	private int counter;

	public ThreadedEchoHandler(Socket incoming, int i) {
		try {
			this.incoming = incoming;
			counter = i;
			clientes.add(incoming);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void run() {
		try {
			String textoEntrada;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(incoming.getOutputStream()));
			
			while(incoming.isConnected()) {
				textoEntrada = bufferedReader.readLine();
				System.out.println("Cliente " + counter + ": " + textoEntrada);
				for (Socket cliente : clientes) {
					if(!cliente.equals(incoming)) {
						BufferedWriter saida = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
						saida.write("Cliente " + counter + ": " + textoEntrada);
						saida.newLine();
						saida.flush();
					}
				}
				if(textoEntrada.equals("BYE")) {
					break;
				}
			}
			
			incoming.close();
			bufferedReader.close();
			bufferedWriter.close();
			clientes.remove(incoming);
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
