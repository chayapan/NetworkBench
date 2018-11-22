package edu.muict.NetworkBench;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * NetworkBench - Network Benchmark for Computer Network class
 *
 */
public class App {

	private String targetAddress;
	private String targetPort;
	private Client client;
	private Server server;
	
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JTextArea activityLog;
	private JLabel msglabel;

	public App() {

		mainFrame = new JFrame("NetworkBench(Instance:" + getProcessId("000") + ") - ITCS631");
		mainFrame.setSize(700, 400);
		mainFrame.setLayout(new GridLayout(3, 1));

		headerLabel = new JLabel("", SwingConstants.CENTER);
		statusLabel = new JLabel("", SwingConstants.CENTER);
		statusLabel.setSize(350, 100);

		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());


		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton clientButton = new JButton("Client");
		JButton serverButton = new JButton("Server");

		buttonPanel.add(clientButton);
		buttonPanel.add(serverButton);
		
		controlPanel.add("Button", buttonPanel);

		activityLog = new JTextArea(20, 30);
		activityLog.insert("Program started: " + new Date().toString() + "\n", 0);

		/* Test actions implement here: */
		JPanel testPanel = new JPanel(new FlowLayout());
		JButton sendMsgButton = new JButton("Send msg");
		JButton sendDataButton = new JButton("Send data");
		testPanel.add(sendMsgButton);
		testPanel.add(sendDataButton);

		sendMsgButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { sendMsg(e); }});

		sendDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { sendData(e); }});


		String help1 = "Run from command-line: java -jar NetworkBench-0.0.1-SNAPSHOT.jar";
		InetAddress address;
		String myIP;
		try {
			String host = "localhost";
			// address = InetAddress.getByName(host);
			address = InetAddress.getLocalHost();
			myIP = "Host/IP address: \n" + address.toString();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			myIP = "Unknown.";
		}
		headerLabel.setText(myIP);

		statusLabel.setText("Network test program ...(Instance:" + getProcessId("000") + ")");

		clientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusLabel.setText("Running Client Mode...");
				activityLog.insert("Start client.\n", 0);
				String addr = JOptionPane.showInputDialog("Target Server Address: ");

				System.out.println(addr);
				Integer port = 1234;

				try {
					client = new Client(activityLog, addr, port);
					client.start();
					serverButton.setEnabled(false);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
					activityLog.insert(e1.getMessage(), 0);
				}
			}
		});

		serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusLabel.setText("Running Server Mode...");
				activityLog.insert("Start server.\n", 0);
				server = new Server(activityLog);
				server.start();
				clientButton.setEnabled(false);
			}
		});

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);
		mainFrame.add(activityLog);
		mainFrame.add(testPanel);
		
		mainFrame.setVisible(true);
	}

	public static void main(String[] args) {
		App app = new App();
	}

	private static String getProcessId(final String fallback) {
		// Note: may fail in some JVM implementations
		// therefore fallback has to be provided

		// something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
		final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		final int index = jvmName.indexOf('@');

		if (index < 1) {
			// part before '@' empty (index = 0) / '@' not found (index = -1)
			return fallback;
		}

		try {
			return Long.toString(Long.parseLong(jvmName.substring(0, index)));
		} catch (NumberFormatException e) {
			// ignore
		}
		return fallback;
	}
	
	private void sendMsg(ActionEvent e) {
		String msg = JOptionPane.showInputDialog("Message: ");
		try {
			activityLog.insert("Sending: " + msg +"\n", 0);
			Date d1 = new Date();
			client.sendMessage(msg);
			Date d2 = new Date();
			Long t = d2.getTime() - d1.getTime(); // gives the time difference in milliseconds.
			activityLog.insert("Took " + t + "ms for sending:" + msg + "\n", 0);
		} catch (Exception e1) {
			activityLog.insert("Fail sending" + msg + ": " + e1.getMessage() + "\n", 0);
		}
		
	}
	
	private void sendData(ActionEvent e) {
		Date d1 = new Date();
		client.connect();
		client.sendData();
		Date d2 = new Date();
		Long t = d2.getTime() - d1.getTime(); // gives the time difference in milliseconds.
		activityLog.insert("Took " + t + "ms for" + 10000 + " messages\n", 0);
	}
}
