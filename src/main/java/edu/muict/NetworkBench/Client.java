package edu.muict.NetworkBench;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JTextArea;

public class Client extends Thread {
	private Boolean connected;
	public Socket link;
	private static InetAddress host;
	private static final int PORT = 1234;
	private JTextArea activityLog;
	
	public Client() {
		try {
			host = InetAddress.getLocalHost();
		} catch (UnknownHostException uhEx) {
			System.out.println("Host ID not found!");
			activityLog.insert(uhEx.getMessage(), 0);
		}
	}
	
	public Client(JTextArea activityLog, String addr, Integer port) throws UnknownHostException {
		this.connected = false;
		this.activityLog = activityLog;
		try {
			host = InetAddress.getByName(addr);
		} catch (UnknownHostException uhEx) {
			System.out.println("Host ID not found!");
			activityLog.insert(uhEx.getMessage(), 0);
			System.exit(1);
		}		
	}

	public static void main(String[] args) {
		Client c = new Client();
		c.start();
	}
	
	public void run() {
		connect(); // Start connection.
		activityLog.insert("Client thread on: " + host.toString() + "\n", 0);
		// this.accessServer();	
	}
	
	public Boolean connect() {
		if (this.connected) {
			activityLog.insert("Already conneced to " + host.toString(), 0);
			return false;
		} else {
			try {
				link = new Socket(host, PORT);
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
				activityLog.insert(ioEx.getMessage(), 0);
			} finally {
				activityLog.insert("Connection opened to " + host.toString(), 0);
			}
			this.connected = true;
			return true;
		}
	}

	private void accessServer() {
		try {
			Scanner input = new Scanner(link.getInputStream());
			// Step 2.
			PrintWriter output = new PrintWriter(link.getOutputStream(), true);
			Scanner userEntry = new Scanner(System.in);

			String message, response;
			do {
				System.out.print("Enter message: ");
				message = userEntry.nextLine();
				this.activityLog.insert(message +"\n", 0);
				output.println(message);
				response = input.nextLine();
				System.out.println("\nSERVER>"+response);
			} while (!message.equals("***CLOSE***"));
			this.connected = false;
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
			activityLog.insert(ioEx.getMessage(), 0);
		} finally {
			try {
				String msg = "\n* Closing connection... *";
				System.out.println(msg);
				activityLog.insert(msg, 0);
				link.close(); // Step 4.
			} catch (IOException ioEx) {
				System.out.println("Unable to disconnect!");
				System.exit(1);
			}
		}
	}

	public void sendMessage(String msg) {
		if (!connected) {
			connect(); // open connection to server if not already done so.
		}
		
		try {
			Scanner input = new Scanner(link.getInputStream());
			PrintWriter output = new PrintWriter(link.getOutputStream(), true);
			
			output.println(msg);
			activityLog.insert("Response: " + input.nextLine() +"\n", 0);

			// output.println("***CLOSE***"); // Finish connection.
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			activityLog.insert("Error: " + e1.getMessage() +"\n", 0);
		}
		
	}
	
	public void sendData() {
		/* Send 10,000 messages.
		 * 
		 * - With new input/output every time: 12084ms for 10,000 messages
		 * Took 63ms for 10000 messages
		 * Took 35888ms for10000 messages 1 MB random string
		 * * Took 692ms for10000 messages 1 KB random string
		 *  */

		try {
			Scanner input = new Scanner(link.getInputStream());
			PrintWriter output = new PrintWriter(link.getOutputStream(), true);
			int sentCount = 0;
			for (int i = 0; i < 1000; i++) {
				String msg = generateData(10); //message content
				output.println(msg);
				sentCount += 1;
				activityLog.insert("Wrote: "+ sentCount+"\n", 0);
			}
			//new DataInputStream(link.getInputStream()).readFully(b4);
			activityLog.insert("Response:" + input.nextLine() +"\n", 0);
			// output.println("***CLOSE***"); // Finish connection.

		} catch (IOException e) {
			activityLog.insert("Error: " + e.getMessage() +"\n", 0);
			e.printStackTrace();
		}
	}
	
	private String generateData(int kb) {
		Random r = new Random();
		byte[] b = new byte[kb * 1024];
		r.nextBytes(b);
		return b.toString();
	}
}
