package redes;
/*
 * 	Student:		Stefano Lupo
 *  Student No:		14334933
 *  Degree:			JS Computer Engineering
 *  Course: 		3D3 Computer Networks
 *  Date:			02/04/2017
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;


/**
 * The Proxy creates a Server Socket which will wait for connections on the specified port.
 * Once a connection arrives and a socket is accepted, the Proxy creates a RequestHandler object
 * on a new thread and passes the socket to it to be handled.
 * This allows the Proxy to continue accept further connections while others are being handled.
 * 
 * The Proxy class is also responsible for providing the dynamic management of the proxy through the console 
 * and is run on a separate thread in order to not interrupt the acceptance of socket connections.
 * This allows the administrator to dynamically block web sites in real time. 
 * 
 * The Proxy server is also responsible for maintaining cached copies of the any websites that are requested by
 * clients and this includes the HTML markup, images, css and js files associated with each webpage.
 * 
 * Upon closing the proxy server, the HashMaps which hold cached items and blocked sites are serialized and
 * written to a file and are loaded back in when the proxy is started once more, meaning that cached and blocked
 * sites are maintained.
 *
 */
public class Proxy implements Runnable{


	// Main method for the program
	public static void main(String[] args) {
		// Create an instance of Proxy and begin listening for connections
		int port = 80;
		int maxSize = -1;
		port = Integer.parseInt(args[0]);
		maxSize =  Integer.parseInt(args[1]);
		Proxy myProxy = new Proxy(port,maxSize);
		myProxy.listen();	
	}

	private ServerSocket serverSocket;

	/**
	 * Semaphore for Proxy and Console Management System.
	 */
	protected volatile boolean running = true;


	/**
	 * Data structure for constant order lookup of cache items.
	 * Key: URL of page/image requested.
	 * Value: File in storage associated with this key.
	 */
	protected static int size; 
	protected static LRUCache<String, Data> lru; 


	/**
	 * ArrayList of threads that are currently running and servicing requests.
	 * This list is required in order to join all threads on closing of server
	 */
	static ArrayList<Thread> servicingThreads;



	/**
	 * Create the Proxy Server
	 * @param port Port number to run proxy server from.
	 */
	public Proxy(int port, int maxSize) {
		//Variável do tamanho da cache
		size = maxSize;
		//Cria uma chache
		lru = new LRUCache<>(size);
		
		// Create array list to hold servicing threads
		servicingThreads = new ArrayList<>();

		// Start dynamic manager on a separate thread.
		//new Thread(this).start();	// Starts overriden run() method at bottom
		

		try {
			// Create the Server Socket for the Proxy 
			serverSocket = new ServerSocket(port);

			// Set the timeout
			//serverSocket.setSoTimeout(100000);	// debug
			System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "..");
			//System.out.println("using cache maximum size "+maxSize);
			running = true;
		} 

		// Catch exceptions associated with opening socket
		catch (SocketException se) {
			System.out.println("Socket Exception when connecting to client");
			se.printStackTrace();
		}
		catch (SocketTimeoutException ste) {
			System.out.println("Timeout occured while connecting to client");
		} 
		catch (IOException io) {
			System.out.println("IO exception when connecting to client");
		}
	}


	/**
	 * Listens to port and accepts new socket connections. 
	 * Creates a new thread to handle the request and passes it the socket connection and continues listening.
	 */
	public void listen(){

		while(running){
			try {
				// serverSocket.accpet() Blocks until a connection is made
				Socket socket = serverSocket.accept();
				
				// Create new Thread and pass it Runnable RequestHandler
				Thread thread = new Thread(new RequestHandler(socket));
				
				// Key a reference to each thread so they can be joined later if necessary
				if(socket.isConnected()) {					
					servicingThreads.add(thread);
				}
				
				thread.start();	
			} catch (SocketException e) {
				// Socket exception is triggered by management system to shut down the proxy 
				System.out.println("Server closed");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Saves the blocked and cached sites to a file so they can be re loaded at a later time.
	 * Also joins all of the RequestHandler threads currently servicing requests.
	 */
	private void closeServer(){
		System.out.println("\nClosing Server..");
		running = false;
		try{
			try{
				// Close all servicing threads
				for(Thread thread : servicingThreads){
					if(thread.isAlive()){
						System.out.print("Waiting on "+  thread.getId()+" to close..");
						thread.join();
						System.out.println(" closed");
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			// Close Server Socket
			try{
				System.out.println("Terminating Connection");
				serverSocket.close();
			} catch (Exception e) {
				System.out.println("Exception closing proxy's server socket");
				e.printStackTrace();
			}
			
		}catch(Exception e) {
			System.out.println("Exception closing proxy's server socket");
			e.printStackTrace();
		}

	}
		
	

		/**
		 * Creates a management interface which can dynamically update the proxy configurations
		 *  	cached	: Lists currently cached sites
		 *  	close	: Closes the proxy server
		 *  	*		: Adds * to the list of blocked sites
		 */
		@Override
		public void run() {
			Scanner scanner = new Scanner(System.in);

			String command;
			while(running){
				System.out.println("Enter \"cached\" to see cached sites, or \"close\" to close server.");
				command = scanner.nextLine();
				
				if(command.toLowerCase().equals("cached")){
					//Tem que aparecer a Cache Aqui
					System.out.println("Quantidade em CHACHE " + lru.maxMemorySize + " Uhull funcionouu!!");
					System.out.println("---Imprimindo Cahce-----------------");
					if(lru.toString()==null) {
						System.out.println("Cache vazia");
					}else {
						lru.toString();
					}
					
					System.out.println("------------------------------------");
				}


				else if(command.equals("close")){
					running = false;
					closeServer();
				}

				
			}
			scanner.close();
		} 

	}
