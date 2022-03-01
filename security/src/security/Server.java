package security;

import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;

public class Server {
	private static final int PORT = 8043;

	public static void main(String[] args) throws Exception {
		// set necessary truststore properties - using JKS
		// System.setProperty("javax.net.ssl.trustStore","truststore.jks");
		// System.setProperty("javax.net.ssl.trustStorePassword","changeit");
		// set up key manager to do server authentication
		SSLContext context;
		KeyManagerFactory kmf;
		KeyStore ks;

		// First we need to load a keystore
		char[] passphrase = "123456".toCharArray();
		ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream("E:\\Security_Course\\projekts\\wolvessrv1.jks"), passphrase);

		// Initialize a KeyManagerFactory with the KeyStore
		kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, passphrase);

		// Create an SSLContext to run TLS and initialize it with
		// KeyManagers from the KeyManagerFactory
		context = SSLContext.getInstance("TLS");
		KeyManager[] keyManagers = kmf.getKeyManagers();
		context.init(keyManagers, null, null);

		// Create a SocketFactory that will create SSL server sockets.
		SSLServerSocketFactory ssf = context.getServerSocketFactory();

		// Create socket and Wait for a connection
		ServerSocket ss = ssf.createServerSocket(PORT);

		System.out.println("Server socket created at port: " + PORT);
		System.out.println("Wait while client is connecting...");

		// Listens for a connection to be made to this socket and acceptsit. The method
		// blocks until a connection is made.
		// A new Socket s is created
		Socket s = ss.accept();
		

		// Get the input stream. En/Decryption happens transparently.
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

		// Read through the input from the client and display it to the screen.
		String line = null;
		while (((line = in.readLine()) != null)) {
			System.out.println(line);
		}
		in.close();
		s.close();
	}
}
