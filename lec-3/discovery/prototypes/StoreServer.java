import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class StoreServer {
	
	private static String value=null;
	
	public static void main(String[] args) {
		  // check argument
		  if (args.length != 1) { 
		      System.out .println(  "Wrong argument. Run this server by inputing \"java SimpleServer port\""); 
		      System.exit(-1); } 
		  // host a server 
		  int port =  Integer.parseInt(args[0]);
		 

		
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Server hosted at port " + port);
		try {
			while (true) {
				Socket socket = server.accept();
				System.out.println("Got Connection from:"
						+ socket.getInetAddress() + ":" + socket.getPort());
				process(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void process(Socket socket) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("You are connected to the StoreServer!\n");
			String request = in.readLine();
			System.out.println("Recv. msg: " + request);
			if (request == null) {
				// wrong argument input by client
				System.out.println("No input recieved.");
				closeSocket(in, out, socket);
				return;
			}
			processCertainAction(request, out);
			closeSocket(in, out, socket);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * do certain action requested by client
	 * 
	 * @param request
	 */
	private static void processCertainAction(String request, PrintWriter out) {
		if (request.toLowerCase().startsWith("set ")) {
			// store address of server
			value=request.substring(4);
		} else if (request.toLowerCase().startsWith("get")) {
			// get address for client
			out.println(value);
		} else {
			out.println("Unsupported command");
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
}
