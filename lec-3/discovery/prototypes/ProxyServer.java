import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ProxyServer {

	static Map<String, Integer> conversionMap = new HashMap<String, Integer>();

	static String msg = null, tempMsg = null;
	static Socket toAnother = null; // socket that connects to conversion server
	static PrintWriter outToServer = null;
	static BufferedReader inFromServer = null;

	public static void main(String args[]) {
		// check argument
		if (args.length != 1) {
			System.out
					.println("Wrong argument. Run this server by inputing \"java ProxyServer port\"");
					System.exit(-1);
		}
		// host a server
		int port = Integer.parseInt(args[0]);
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Server hosted at port " + port);
		try {
			Socket notifySocket=new Socket("baobaoioz.koding.io",23456);
			PrintWriter out = new PrintWriter(notifySocket.getOutputStream(),true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					notifySocket.getInputStream()));
			out.println("ProxyServer is running at port:"+port);
			// close IO streams, then socket
			out.close();
			in.close();
			notifySocket.close();
		} catch (Exception e) {
			System.out.println("Register failed.");
		}
		try {
			while (true) {
				Socket socket = server.accept();
				System.out.println("Got Connection from:"
						+ socket.getInetAddress() + ":" + socket.getPort());
				convert(socket);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void convert(Socket socket) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("You are connected to the Proxy Server!\n");
			String request = in.readLine();
			if (request == null) {
				// wrong argument input by client
				System.out.println("No input recieved.");
				closeSocket(in, out, socket);
				return;
			}
			// split parameters
			String[] paras = request.split(" ");
			if (paras.length != 3) {
				System.out.println("Wrong argument.");
				out.println("Wrong argument.\nUsage: ft km 1");
				closeSocket(in, out, socket);
				return;
			}
			System.out.println("request is:" + paras[0] + "->" + paras[1]);
			double temp = 0; // used to store result received from conversion
								// server
			try {
				temp = Double.parseDouble(paras[2]);
			} catch (NumberFormatException e) {
				// invalid number
				System.out.println("Invalid number format: " + paras[2]);
				closeSocket(in, out, socket);
				return;
			}
			if (paras[0].equals("ft")) {

				if (paras[1].equals("in")) {
					// convert ft to inch
					temp = convert(temp, "ftin", false);
					// output to client
					out.println(temp + "\n");
				} else if (paras[1].equals("cm")) {
					// convert ft to inch
					temp = convert(temp, "ftin", false);
					// convert in to cm
					temp = convert(temp, "incm", false);
					// output to client
					out.println(temp + "\n");
				} else if (paras[1].equals("m")) {
					// convert ft to inch
					temp = convert(temp, "ftin", false);
					// convert in to cm
					temp = convert(temp, "incm", false);
					// convert cm to m
					temp = convert(temp, "cmm", false);
					// output to client
					out.println(temp + "\n");
				} else if (paras[1].equals("km")) {
					// convert ft to inch
					temp = convert(temp, "ftin", false);
					// convert in to cm
					temp = convert(temp, "incm", false);
					// convert cm to m
					temp = convert(temp, "cmm", false);
					// convert m to km
					temp = convert(temp, "mkm", false);
					// output to client
					out.println(temp + "\n");
				} else {
					System.out.println("Wrong Argument.");
					out.println("Wrong argument.\nUsage: ft km 1");
				}
			} else if (paras[0].equals("in")) {
				if (paras[1].equals("ft")) {
					// convert in to ft
					temp = convert(temp, "ftin", true);
					out.println(temp + "\n");
				} else if (paras[1].equals("cm")) {
					// convert in to cm
					temp = convert(temp, "incm", false);
					out.println(temp + "\n");
				} else if (paras[1].equals("m")) {
					// convert in to cm
					temp = convert(temp, "incm", false);
					// convert cm to m
					temp = convert(temp, "cmm", false);
					out.println(temp + "\n");
				} else if (paras[1].equals("km")) {
					// convert in to cm
					temp = convert(temp, "incm", false);
					// convert cm to m
					temp = convert(temp, "cmm", false);
					// convert m to km
					temp = convert(temp, "mkm", false);
					// output to client
					out.println(temp + "\n");
				} else {
					System.out.println("Wrong Argument.");
					out.println("Wrong argument.\nUsage: ft km 1");
				}
			} else if (paras[0].equals("cm")) {
				if (paras[1].equals("ft")) {
					temp = convert(temp, "incm", true);
					temp = convert(temp, "ftin", true);
					out.println(temp + "\n");
				} else if (paras[1].equals("in")) {
					temp = convert(temp, "incm", true);
					out.println(temp + "\n");
				} else if (paras[1].equals("m")) {
					temp = convert(temp, "cmm", false);
					out.println(temp + "\n");
				} else if (paras[1].equals("km")) {
					temp = convert(temp, "cmm", false);
					temp = convert(temp, "mkm", false);
					out.println(temp + "\n");
				} else {
					System.out.println("Wrong Argument.");
					out.println("Wrong argument.\nUsage: ft km 1");
				}
			} else if (paras[0].equals("m")) {
				if (paras[1].equals("ft")) {
					temp = convert(temp, "cmm", true);
					temp = convert(temp, "incm", true);
					temp = convert(temp, "ftin", true);
					out.println(temp + "\n");
				} else if (paras[1].equals("in")) {
					temp = convert(temp, "cmm", true);
					temp = convert(temp, "incm", true);
					out.println(temp + "\n");
				} else if (paras[1].equals("cm")) {
					temp = convert(temp, "cmm", true);
					out.println(temp + "\n");
				} else if (paras[1].equals("km")) {
					temp = convert(temp, "mkm", false);
					out.println(temp + "\n");
				} else {
					System.out.println("Wrong Argument.");
					out.println("Wrong argument.\nUsage: ft km 1");
				}
			} else if (paras[0].equals("km")) {
				if (paras[1].equals("ft")) {
					temp = convert(temp, "mkm", true);
					temp = convert(temp, "cmm", true);
					temp = convert(temp, "incm", true);
					temp = convert(temp, "ftin", true);
					out.println(temp + "\n");
				} else if (paras[1].equals("in")) {
					temp = convert(temp, "mkm", true);
					temp = convert(temp, "cmm", true);
					temp = convert(temp, "incm", true);
					out.println(temp + "\n");
				} else if (paras[1].equals("cm")) {
					temp = convert(temp, "mkm", true);
					temp = convert(temp, "cmm", true);
					out.println(temp + "\n");
				} else if (paras[1].equals("m")) {
					temp = convert(temp, "mkm", true);
					out.println(temp + "\n");
				} else {
					System.out.println("Wrong Argument.");
					out.println("Wrong argument.\nUsage: ft km 1");
				}
			}

			else {
				System.out.println("Wrong Argument.");
				out.println("Wrong argument.\nUsage: ft km 1");
			}
			// close socket, out and inputStream
			closeSocket(in, out, socket);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	// close socket to the client
	private static void closeSocket(BufferedReader in, PrintWriter out,
			Socket socket) {
		try {
			out.close();
			out.flush();
			in.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param temp
	 *            temp value
	 * @param type
	 *            conversion type
	 * @param rev
	 *            reverse or not
	 * @throws Exception
	 */
	private static double convert(double temp, String type, boolean rev)
			throws Exception {
		String from = null, to = null;
		System.out.println("type="+type);
		switch (conversionMap.get(type)) {
		case 1:
			toAnother = new Socket("baobaoioz.koding.io", 1234);
			if (!rev) {
				from = "ft";
				to = "in";
			} else {
				from = "in";
				to = "ft";
			}
			break;
		case 2:
			toAnother = new Socket("baobaoioz.koding.io", 2345);
			if (!rev) {
				from = "in";
				to = "cm";
			} else {
				from = "cm";
				to = "in";
			}
			break;
		case 3:
			toAnother = new Socket("baobaoioz.koding.io", 3456);
			if (!rev) {
				from = "cm";
				to = "m";
			} else {
				from = "m";
				to = "cm";
			}
			break;
		case 4:
			toAnother = new Socket("baobaoioz.koding.io", 4567);
			if (!rev) {
				from = "m";
				to = "km";
			} else {
				from = "km";
				to = "m";
			}
			break;
		}
		inFromServer = new BufferedReader(new InputStreamReader(
				toAnother.getInputStream()));
		outToServer = new PrintWriter(toAnother.getOutputStream(), true);
		outToServer.println(from + " " + to + " " + temp);
		while ((tempMsg = inFromServer.readLine()) != null) {
			msg = tempMsg;
		}
		System.out.print(temp + " " + from + " = ");
		temp = Double.parseDouble(msg);
		System.out.println(temp + " " + to);
		// close socket, out and inputStream
		toAnother.close();
		outToServer.flush();
		outToServer.close();
		inFromServer.close();
		return temp;
	}

	static {
		// initial conversion types
		conversionMap.put("ftin", 1);
		conversionMap.put("incm", 2);
		conversionMap.put("cmm", 3);
		conversionMap.put("mkm", 4);
	}
}
