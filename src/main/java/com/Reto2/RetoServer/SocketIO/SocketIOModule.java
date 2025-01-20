package com.Reto2.RetoServer.SocketIO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.Reto2.RetoServer.Config.Events;
import com.Reto2.RetoServer.Model.MessageInput;
import com.Reto2.RetoServer.Model.MessageOutput;

import com.Reto2.RetoServer.Database.Entity.*;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import jakarta.persistence.NoResultException;

public class SocketIOModule {

	private SocketIOServer server = null;
	SessionFactory sesion = HibernateUtil.getSessionFactory();
	Session session = sesion.openSession();

	public SocketIOModule(SocketIOServer server) {
		super();
		this.server = server;

		// Default events (for control the connection of clients)
		server.addConnectListener(onConnect());
		server.addDisconnectListener(onDisconnect());

		// Custom events
		server.addEventListener(Events.ON_LOGIN.value, String.class, this.login());
		server.addEventListener(Events.ON_GET_ALL.value, MessageInput.class, this.getAll());
		server.addEventListener(Events.ON_LOGOUT.value, MessageInput.class, this.logout());
	}

	private DataListener<String> login() {
	    return ((client, data, ackSender) -> {
	        try {
	            System.out.println("Client from " + client.getRemoteAddress() + " wants to login");
	            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	            JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

	        
	            if (!jsonObject.has("message") || !jsonObject.has("userPass")) {
	                client.sendEvent(Events.ON_LOGIN_FALL.value, "Invalid data format");
	                System.out.println("Invalid login request: Missing required fields");
	            }

	            String userName = jsonObject.get("message").getAsString();
	            String userPass = jsonObject.get("userPass").getAsString();

	            String name = sendClient().getUserName();
	            String pass = sendClient().getPass();
	            System.out.println(pass);

	   
	            if (userName.equals(name) && userPass.equals(pass)) {
	                client.sendEvent(Events.ON_LOGIN_SUCCESS.value, "Login correcto");
	                System.out.println("El usuario ha sido logueado correctamente: " + userName);
	            } else {
	                client.sendEvent(Events.ON_LOGIN_FALL.value, "Login incorrecto");
	                System.out.println("El usuario no ha podido loguearse: " + userName);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            client.sendEvent(Events.ON_LOGIN_FALL.value, "Error de servidor");
	        }
	    });
	}

	private DataListener<MessageInput> logout() {
		return ((client, data, ackSender) -> {
			// This time, we simply write the message in data
			System.out.println("Client from " + client.getRemoteAddress() + " wants to logout");

			// The JSON message from MessageInput
			String message = data.getMessage();

			// We parse the JSON into an JsonObject
			// The JSON should be something like this: {"message": "patata"}
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
			String userName = jsonObject.get("message").getAsString();

			// We do something on dataBase? ¯_(ツ)_/¯

			System.out.println(userName + " loged out");
		});
	}

	private DataListener<MessageInput> getAll() {
		return ((client, data, ackSender) -> {
			try {
				// This time, we simply write the message in data
				System.out.println("Client from " + client.getRemoteAddress() + " wants to getAll");

				// We access to database and... we get a bunch of people
				List<Client> clients = getAllClient();
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				// We parse the answer into JSON
				String answerMessage = gson.toJson(clients);

				// ... and we send it back to the client inside a MessageOutput
				MessageOutput messageOutput = new MessageOutput(answerMessage);
				System.out.println(messageOutput);
				client.sendEvent(Events.ON_GET_ALL_ANSWER.value, messageOutput);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private ConnectListener onConnect() {
		return (client -> {
			client.joinRoom("default-room");
			System.out.println("New connection, Client: " + client.getRemoteAddress());
		});
	}

	private DisconnectListener onDisconnect() {
		return (client -> {
			client.leaveRoom("default-room");
			System.out.println(client.getRemoteAddress() + " disconected from server");
		});
	}

	// Server control

	public void start() {
		server.start();
		System.out.println("Server started...");
		Client client = sendClient();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String answerMessage = gson.toJson(client);

		System.out.println("answerMessage: " + answerMessage);
		
	}

	public void stop() {
		server.stop();
		System.out.println("Server stopped");
	}

	public Client sendClient() {
		String hql = "from Client where userName = 'John'";
		Query<Client> query = session.createQuery(hql, Client.class);
		Client client = null;
		try {
			client = query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("No client found with the username 'John'.");
		}
		return client;
	}
	public List<Client> getAllClient(){
		List<Client> clients = new ArrayList<Client>();
		String hql = "Select * from Client";
		Client client = new Client();
		Query<?> q = session.createQuery(hql);
		List<?> filas = q.list();
	
		for(int i=0; i < filas.size(); i++) {
			client = (Client) filas.get(i);
			clients.add(client);
		}
		return clients;
		
	}
}
