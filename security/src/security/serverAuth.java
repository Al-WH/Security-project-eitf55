package security;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class serverAuth {
	
		// likely this port number is ok to use
		private static final int PORT = 8043;

		public static void main(String[] args) throws Exception {
			// set necessary truststore properties - using JKS
			System.setProperty("javax.net.ssl.trustStore", "truststore.jks");
			System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
			
			// set up key manager to do server authentication
			SSLContext sslContxt;
			KeyManagerFactory keyManFact;
			KeyStore keyStore;
			
			// First we need to load a keystore
			char[] passphrase = "123456".toCharArray();
			keyStore = KeyStore.getInstance("JKS");
			keyStore.load(new FileInputStream("E:\\Security_Course\\projekts\\wolvessrv1.jks"), passphrase);
			
			// Initialize a KeyManagerFactory with the KeyStore
			keyManFact = KeyManagerFactory.getInstance("SunX509");
			keyManFact.init(keyStore, passphrase);
			
			// Create an SSLContext to run TLS and initialize it with
			// KeyManagers from the KeyManagerFactory
			sslContxt = SSLContext.getInstance("TLS");
			KeyManager[] keyManagers = keyManFact.getKeyManagers();
			sslContxt.init(keyManagers, null, null);
			
			// Create a SocketFactory that will create SSL server sockets.
			SSLServerSocketFactory ssf = sslContxt.getServerSocketFactory();
			
			// Create socket and Wait for a connection
			ServerSocket ss = ssf.createServerSocket(PORT);

			// Print the port for server socket and a text for client connecting
			// ------------------------------------------------------------------
			System.out.println("Server socket created at port: " + PORT);
			System.out.println("Wait while client is connecting...");

			
			SSLSocket s = (SSLSocket)ss.accept();
			// Get the input stream. En/Decryption happens transparently.
			s.setNeedClientAuth(true);
			
			
			String pickedCiphers[] = { "TLS_RSA_WITH_AES_128_CBC_SHA"};
			s.setEnabledCipherSuites(pickedCiphers);
			System.out.println("Server socket is connected to the port: " + s.getPort());
			// Test the Connection
			if (s.isConnected()) {				
				System.out.println("The client is connected");
			} else {
				System.out.println("The client could not connect");
			}

			// Get the input stream. En/Decryption happens transparently.
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			// Read through the input from the client and display it to the screen.
			String line = null;
			String valueFClient= in.readLine();
			while (((line = valueFClient) != null)) {
				System.out.println(line);
			}
			in.close();
			s.close();
		}
	}


