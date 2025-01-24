package com.Reto2.RetoServer;


import com.Reto2.RetoServer.SocketIO.SocketIOModule;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

public class APP {
	
	private static final String HOST_NAME = "0.0.0.0";
	private static final int PORT = 2888;


	public static void main(String[] args) {
		
		// Server configuration 
		Configuration config = new Configuration ();
		config.setHostname(HOST_NAME);
		config.setPort(PORT);
		
		
		// We start the server
		SocketIOServer server = new SocketIOServer(config);
		SocketIOModule module = new SocketIOModule(server);
		module.start();
		
		System.out.println("Server started on " + config.getPort());
		

	}

}
