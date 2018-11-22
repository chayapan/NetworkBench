package edu.muict.NetworkBench;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JTextArea;

public class Server extends Thread {

	private Socket link;
	private static ServerSocket serverSocket;
	private static final int PORT = 1234;
	private JTextArea activityLog;

	public Server() {
		// TODO Auto-generated constructor stub
	}
	
	public Server(JTextArea activityLog) {
		this.activityLog = activityLog;
	}


	public static void main(String[] args) {
		Server s = new Server();
		s.start();
	}
	
	public void run() {
		String msg = "Opening port " + PORT + "...\n";
		System.out.println(msg);
		this.activityLog.insert(msg, 0);
		try {
			serverSocket = new ServerSocket(PORT); // Step 1.
		} catch (IOException ioEx) {
			msg = "Unable to attach to port!";
			System.out.println(msg);
			this.activityLog.insert(msg, 0);
			// System.exit(1);
		} catch (Exception e) {
			msg = "Error starting server: " + e.getMessage();
			this.activityLog.insert(msg, 0);
			System.out.println(msg);
		}
		do {
			this.handleClient();
		} while (true);
	}

	private void handleClient() {
		try {
			link = serverSocket.accept();
			Scanner input = new Scanner(link.getInputStream());// Step 3.
			PrintWriter output = new PrintWriter(link.getOutputStream(), true); // Step 3.
			long numMessages = 0;
			String message = input.nextLine(); // Step 4.
			while (!message.equals("***CLOSE***")) {
				numMessages++;
				String msg = " Message " + numMessages + ": " + message + "\n";
				output.println(msg); // Step 4. 
				this.activityLog.insert(msg, 0);
				message = input.nextLine();
			}
			output.println(numMessages + " messages received.");// Step 4.
		}

		catch (IOException ioEx) {
			this.activityLog.insert("Error handleClient.", 0);
			ioEx.printStackTrace();
		} finally {
			try {
				String msg = "\n* Closing connection... *\n";
				System.out.println(msg);
				this.activityLog.insert(msg, 0);
				link.close(); // Step 5.
			} catch (IOException ioEx) {
				System.out.println("Unable to disconnect!");
				System.exit(1);
			}
		}
	}
}
