package edu.muict.NetworkBench;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JTextArea;

public class Client extends Thread {
	private static InetAddress host;
	private static final int PORT = 1234;
	private JTextArea activityLog;
	
	public Client() {
		// TODO Auto-generated constructor stub
	}
	
	public Client(JTextArea activityLog) {
		this.activityLog = activityLog;
	}

	public static void main(String[] args) {
		Client c = new Client();
		c.start();
	}
	
	public void run() {
		try {
			host = InetAddress.getLocalHost();
		} catch (UnknownHostException uhEx) {
			System.out.println("Host ID not found!");
			System.exit(1);
		}
		this.accessServer();	
	}

	private void accessServer() {
		Socket link = null;
		try {
			link = new Socket(host, PORT);
			Scanner input = new Scanner(link.getInputStream());
			// Step 2.
			PrintWriter output = new PrintWriter(link.getOutputStream(), true); // Step 2.
			// Set up stream for keyboard entry...
			Scanner userEntry = new Scanner(System.in);

			String message, response;
			do {
				System.out.print("Enter message: ");
				message = userEntry.nextLine();
				this.activityLog.insert(message +"\n", 0);
				output.println(message); // Step 3. response = input.nextLine(); //Step 3. System.out.println("\nSERVER>
											// "+response);
			} while (!message.equals("***CLOSE***"));
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			try {
				System.out.println("\n* Closing connection... *");
				link.close(); // Step 4.
			} catch (IOException ioEx) {
				System.out.println("Unable to disconnect!");
				System.exit(1);
			}
		}
	}
}
