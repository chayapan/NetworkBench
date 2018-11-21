package edu.muict.NetworkBench;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * NetworkBench - Network Benchmark for Computer Network class
 *
 */
public class App {
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JTextArea activityLog;
	private JLabel msglabel;

	public App() {

		mainFrame = new JFrame("NetworkBench(Instance:" + getProcessId("000") + ") - ITCS631");
		mainFrame.setSize(400, 400);
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

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);

	      JPanel buttonPanel = new JPanel(new FlowLayout());
	      JButton clientButton = new JButton("Client");
	      JButton serverButton = new JButton("Server");
	      buttonPanel.add(clientButton);
	      buttonPanel.add(serverButton);  
	     
	      
		controlPanel.add("Button", buttonPanel);
	
		activityLog = new JTextArea(20,30);
		controlPanel.add("Log", activityLog);
	    activityLog.insert("Hello\n", 0);
	      
		mainFrame.setVisible(true);
		
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
	            Client client = new Client(activityLog);
	            client.start();
	            serverButton.setEnabled(false);
	         }
	      }); 

	      serverButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { 
	            statusLabel.setText("Running Server Mode...");
	            activityLog.insert("Start server.\n", 0);
	            Server server = new Server(activityLog);
	            server.start();
	            clientButton.setEnabled(false);
	         }
	      }); 
		
		
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
}
