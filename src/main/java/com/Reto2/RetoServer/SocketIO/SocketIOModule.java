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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
		server.addEventListener(Events.ON_LOGIN.value, MessageInput.class, this.login());
		server.addEventListener(Events.ON_GET_ALL.value, MessageInput.class, this.getAll());
		server.addEventListener(Events.ON_LOGOUT.value, MessageInput.class, this.logout());
	}

	private DataListener<MessageInput> login() {
		return ((client, data, ackSender) -> {

			try {
				System.out.println("Client from " + client.getRemoteAddress() + " wants to login");

				String message = data.getMessage();
				Gson gson = new Gson();
				JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

				String userName = jsonObject.get("message").getAsString();

				System.out.println("userName: " + userName);
				
				Client newClient = sendClient();
				System.out.println("newClient: " + newClient.toString());
				String answerMessage = gson.toJson(newClient);
				
				System.out.println("answerMessage: " + answerMessage);
				
				MessageOutput messageOutput = new MessageOutput(answerMessage);
				client.sendEvent(Events.ON_LOGIN_ANSWER.value, messageOutput);
				
				System.out.println("event sent!!");
			} catch (Exception e) {
				System.out.println(e);
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
				List<Client> students = new ArrayList<Client>();
				Client newClient = sendClient();
				students.add(newClient);
				// We parse the answer into JSON
				String answerMessage = new Gson().toJson(students);

				// ... and we send it back to the client inside a MessageOutput
				MessageOutput messageOutput = new MessageOutput(answerMessage);
				System.out.println(messageOutput.toString());
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
		Client newClient = sendClient();

		System.out.println(newClient.getUserId());
		System.out.println(newClient.getUserName());
		System.out.println(newClient.getSurname());
		System.out.println(newClient.getPass());
	}

	public void stop() {
		server.stop();
		System.out.println("Server stopped");
	}

	public Client sendClient() {
		String hql = "from Client where userName = 'John'";
		Client client = new Client();
		Query<?> q = session.createQuery(hql);
		List<?> filas = q.list();

		for (int i = 0; i < filas.size(); i++) {
			client = (Client) filas.get(i);
		}
		return client;
	}
}
