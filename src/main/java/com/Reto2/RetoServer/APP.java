package com.Reto2.RetoServer;

import com.corundumstudio.socketio.Configuration;

public class APP {
	
	private static final String HOST_NAME = "localhost";
	private static final int PORT = 2888;


	public static void main(String[] args) {
		
		// Server configuration 
		Configuration config = new Configuration ();
		config.setHostname(HOST_NAME);
		config.setPort(PORT);
		
		

	}

}
