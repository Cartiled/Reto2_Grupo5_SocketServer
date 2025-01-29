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
		server.addEventListener(Events.ON_REGISTER_AWNSER.value, String.class, this.register());
	}

	private DataListener<String> login() {
		return ((client, data, ackSender) -> {
			try {
				System.out.println("Client from " + client.getRemoteAddress() + " wants to login");
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

				if (!jsonObject.has("message") || !jsonObject.has("userPass")) {
					client.sendEvent(Events.ON_LOGIN_FALL.value, "Formato de datos invalido");
					System.out.println("Datos incorrecto");
				}

				String userName = jsonObject.get("message").getAsString();
				String userPass = jsonObject.get("userPass").getAsString();

				Client loginClient = sendClient(userName);
				Student student = getStudentByUser(userName);
				String name = loginClient.getUserName();
				String pass = loginClient.getPass();

				if (loginClient.getRegistered() == true) {
					System.out.println("usuario registrado");
					if (userName.equals(name) && userPass.equals(pass)) {
						System.out.println(loginClient.toString());

						String answerMessage = gson.toJson(loginClient);
						MessageOutput messageOutput = new MessageOutput(answerMessage);
						client.sendEvent(Events.ON_LOGIN_SUCCESS.value, messageOutput);
						System.out.println("El usuario ha sido logueado correctamente: " + userName);
					} else {
						client.sendEvent(Events.ON_LOGIN_FALL.value, "Login incorrecto");
						System.out.println("El usuario no ha podido loguearse: " + userName);
					}
				} else {
					System.out.println("usuario no registrado");
					client.sendEvent(Events.ON_REGISTER.value, "Registrate porfavor");
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

	private DataListener<String> register() {
		return ((client, data, ackSender) -> {
			System.out.println("Client from " + client.getRemoteAddress() + " wants to register");
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

			if (!jsonObject.has("username") || !jsonObject.has("userpass")) {
				client.sendEvent(Events.ON_LOGIN_FALL.value, "Formato de datos invalido");
				System.out.println(jsonObject.toString());
				System.out.println("Datos incorrecto");
			} else {
				System.out.println(jsonObject.toString());
			}
			String userName = jsonObject.get("username").getAsString();
			String userPass = jsonObject.get("userpass").getAsString();
			String userSurname = jsonObject.get("surname").getAsString();
			String userSecondSurname = jsonObject.get("secondsurname").getAsString();
			String userDni = jsonObject.get("dni").getAsString();
			String userDirection = jsonObject.get("direction").getAsString();
			int userTelephone = jsonObject.get("telephone").getAsInt();
			char userYear = jsonObject.get("year").getAsCharacter();
			String userCourseName = jsonObject.get("courseName").getAsString();
			Boolean userDual = jsonObject.get("dual").getAsBoolean();

			Client loginClient = sendClient(userName);
			String pass = loginClient.getPass();

			Student student = getStudentByUser(userName);

			Course course = getUserCourseByMatriculation(loginClient.getUserId());

			if (userPass.equals(pass)) {
				System.out.println("La contraseña es igual que la anterior");
				client.sendEvent(Events.ON_REGISTER_SAME_PASSWORD.value, "Escoge una contraseña que sea diferente");
			} else {
				if (userName.equals(loginClient.getUserName()) && userSurname.equals(loginClient.getSurname())
						&& userSecondSurname.equals(loginClient.getSecondsurname())
						&& userDni.equals(loginClient.getDni()) && userDirection.equals(loginClient.getDirection())
						&& userTelephone == loginClient.getTelephone() && userYear == student.getUserYear()
						&& userCourseName.equals(course.getTitle()) && userDual == student.isIntensiveDual()) {
					client.sendEvent(Events.ON_REGISTER_SUCCESS.value, "Has registrado tu usuario correctamente");
				} else {
					client.sendEvent(Events.ON_REGISTER_FALL.value,
							"Por favor, comprueba los datos que estan correctos");
				}

			}

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
		Course course = getUserCourseByMatriculation(1);
		System.out.println(course.getTitle());
		System.out.println("Server started...");

	}

	public void stop() {
		server.stop();
		System.out.println("Server stopped");
	}

	public Client sendClient(String loginUserName) {
		String hql = "from Client where userName  =:loginUserName";
		Query<Client> query = session.createQuery(hql, Client.class);
		query.setParameter("loginUserName", loginUserName);
		Client client = null;
		try {
			client = query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("No client found with the username: " + loginUserName);
		}
		return client;
	}

	public Student getStudentByUser(String registerName) {
		String hql = "from Student where client.userName =:registerName";
		Query<Student> query = session.createQuery(hql, Student.class);
		query.setParameter("registerName", registerName);
		Student student = null;
		try {
			student = query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("No client found with the username: " + registerName);
		}
		return student;

	}

	public Course getUserCourseByMatriculation(int id) {
		String hql = "select c from Course c join c.matriculations m join m.student s where s.userId =:id";
		Query<Course> query = session.createQuery(hql, Course.class);
		query.setParameter("id", id);
		Course course = null;
		try {
			course = query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("No client found with the username: " + id);
		}
		return course;

	}

	public List<Client> getAllClient() {
		List<Client> clients = new ArrayList<Client>();
		String hql = "Select * from Client";
		Client client = new Client();
		Query<?> q = session.createQuery(hql);
		List<?> filas = q.list();
		for (int i = 0; i < filas.size(); i++) {
			client = (Client) filas.get(i);
			clients.add(client);
		}
		return clients;
	}
}
