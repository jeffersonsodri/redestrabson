package redestrabson;
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
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Oi
 */
public class Proxy implements Runnable{


	// Main method for the program
	public static void main(String[] args) {
		// Create an instance of Proxy and begin listening for connections
		final int port;
		final int maxSize;
		port = Integer.parseInt(args[0]);
		maxSize =  Integer.parseInt(args[1]);
		Proxy myProxy = new Proxy(port,maxSize);
		myProxy.listen();	
	}
	
	/**
	 * Logger, usado para exceptions
	 */
	
	private static final Logger LOGGER = Logger.getLogger( Proxy.class.getName() );
	
	/**
	 * Socket do servidor
	 */
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
	//protected static LRUCache<String, Data> lru; 


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
	//	lru = new LRUCache<>(size);
		
		// Create array list to hold servicing threads
		servicingThreads = new ArrayList<>();

		// Start dynamic manager on a separate thread.
		new Thread(this).start();	// Starts overriden run() method at bottom
		

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
			LOGGER.log( Level.SEVERE, e.toString(), e );
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
					System.out.println("Quantidade em CHACHE " + RequestHandler.lru.maxMemorySize + " Uhull funcionouu!!");
					System.out.println("---Imprimindo Cache-----------------");
					System.out.println(RequestHandler.lru.snapshot());
					
					
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
