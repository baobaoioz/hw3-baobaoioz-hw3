/******************************************************************************
 *
 *  CS 6421 - Simple Conversation
 *  Compilation:  javac ConvServer.java
 *  Execution:    java ConvServer port
 *
 *  % java ConvServer portnum
 ******************************************************************************/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConvServer {

	public static void process(Socket clientSocket) throws IOException {
		// open up IO streams
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		/* Write a welcome message to the client */
		out.println("Welcome, you are connected to a Java-based server");

		/* read and print the client's request */
		// readLine() blocks until the server receives a new line from client
		String userInput;
		if ((userInput = in.readLine()) == null) {
			System.out.println("Error reading message");
			out.close();
			in.close();
			clientSocket.close();
			return;
		}
		System.out.println("Received message: " + userInput);
		// --TODO: add your converting functions here, msg = func(userInput);
		String[] paras = userInput.split(" ");
		if (paras[0].equals("ft") && paras[1].equals("in")) {
			System.out.println(Double.parseDouble(paras[2]) * 12);
			out.println(Double.parseDouble(paras[2]) * 12);
		} else if (paras[0].equals("in") && paras[1].equals("ft")) {
			System.out.println(Double.parseDouble(paras[2]) / 12);
			out.println(Double.parseDouble(paras[2]) / 12);

		} else {
			System.out.println("Wrong arguments.");
		}

		// close IO streams, then socket
		out.close();
		in.close();
		clientSocket.close();
	}

	public static void main(String[] args) throws Exception {

		// check if argument length is invalid
		if (args.length != 1) {
			System.err.println("Usage: java ConvServer port");
			System.exit(-1);
		}
		// create socket
		int port = Integer.parseInt(args[0]);
		ServerSocket serverSocket = new ServerSocket(port);
		System.err.println("Started server on port " + port);

		try {
			Socket notifySocket=new Socket("baobaoioz.koding.io",23456);
			PrintWriter out = new PrintWriter(notifySocket.getOutputStream(),true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					notifySocket.getInputStream()));
			out.println("set ft in baobaoioz.koding.io "+port);
			// close IO streams, then socket
			out.close();
			in.close();
			notifySocket.close();
		} catch (Exception e) {
			System.out.println("Register failed.");
		}
		
		// wait for connections, and process
		try {
			while (true) {
				// a "blocking" call which waits until a connection is requested
				Socket clientSocket = serverSocket.accept();
				System.err.println("\nAccepted connection from client");
				process(clientSocket);
			}

		} catch (IOException e) {
			System.err.println("Connection Error");
		}
		System.exit(0);
	}
}
