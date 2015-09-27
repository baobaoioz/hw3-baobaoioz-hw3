import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class PrintServer {
	public static void main(String[] args) {

		
		  // check argument
		  if (args.length != 1) { 
		      System.out .println(  "Wrong argument. Run this server by inputing \"java PrintServer port\""); 
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
			out.println("You are connected to the PrintServer!\n");
			String request = in.readLine();
			if (request == null) {
				// wrong argument input by client
				System.out.println("No input recieved.");
				closeSocket(in, out, socket);
				return;
			}
			System.out.println("Recv. msg: " + request);
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
