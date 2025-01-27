package com.Reto2.RetoServer.SocketIO;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

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
		server.addEventListener(Events.ON_FILTER_BY_COURSE.value, String.class, this.filterByCourse());
		server.addEventListener(Events.ON_FILTER_BY_CYCLE.value, String.class, this.filterByCycle());
		server.addEventListener(Events.ON_FILTER_BY_SUBJECT.value, String.class, this.filterBySubject());
		server.addEventListener(Events.ON_LOGOUT.value, MessageInput.class, this.logout());
		server.addEventListener(Events.ON_REGISTER_ANSWER.value, String.class, this.register());
	}

	private DataListener<String> login() {
		return ((client, data, ackSender) -> {
			try {
				System.out.println("Client from " + client.getRemoteAddress() + " wants to login");
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

				if (!jsonObject.has("message") || !jsonObject.has("userPass")) {
					client.sendEvent(Events.ON_LOGIN_FAIL.value, "Formato de datos invalido");
					System.out.println("Datos incorrecto");
				}

				String userName = jsonObject.get("message").getAsString();
				String userPass = jsonObject.get("userPass").getAsString();

				Client loginClient = sendClient(userName);
<<<<<<< HEAD
				Student student = getStudentByUser(userName);
				String name = loginClient.getUserName();
				String pass = loginClient.getPass();

				KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");
				key.initialize(1024);
				KeyPair keyPair = key.generateKeyPair();
				PublicKey publicKey = keyPair.getPublic();
				PrivateKey privateKey = keyPair.getPrivate();

				String msg = pass;

				encriptar(null, publicKey, data);
				desencriptar(null, privateKey, data);

				byte[] msgEncryptedBytes = encriptar(msg.getBytes(), publicKey, key.getAlgorithm());
				System.out.println("Texto encriptado -> " + new String(msgEncryptedBytes));

				byte[] msgDecryptedBytes = desencriptar(msgEncryptedBytes, privateKey, key.getAlgorithm());
				System.out.println("Texto desencriptado -> " + new String(msgDecryptedBytes));

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
<<<<<<< HEAD
				client.sendEvent(Events.ON_LOGIN_FALL.value, "Error de servidor");
			}
		});
	}

	public static byte[] encriptar(byte[] inputBytes, PublicKey publicKey, String algorithm) {

		try {

			/*
			 * Using getIstance in static to create object with its RSA
			 */
			Cipher cipher = Cipher.getInstance(algorithm);

			// Encryps public key
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			/*
			 * Convert text to a inputByte
			 */
			return cipher.doFinal(inputBytes);

		} catch (Exception ex) {
			System.out.print(ex);
		}

		return null;
	}

	public static byte[] desencriptar(byte[] inputBytes, PrivateKey privateKey, String algorithm) {

		try {

			Cipher cipher = Cipher.getInstance(algorithm);

			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(inputBytes);

		} catch (Exception ex) {
			System.out.print(ex);
		}

		return null;
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

			if (!jsonObject.has("userName") || !jsonObject.has("userPass")) {
				client.sendEvent(Events.ON_LOGIN_FAIL.value, "Formato de datos invalido");
				System.out.println(jsonObject.toString());
				System.out.println("Datos incorrecto");
			} else {
				System.out.println(jsonObject.toString());
			}
			String userName = jsonObject.get("username").getAsString();
			String userPass = jsonObject.get("userpass").getAsString();

			Client loginClient = sendClient(userName);
			String name = loginClient.getUserName();
			String pass = loginClient.getPass();
			String surname = loginClient.getSurname();
			String secondSurname = loginClient.getSecondSurname();
			String direction = loginClient.getDirection();
			String dni = loginClient.getDni();
			int telephone = loginClient.getTelephone();

			Student student = getStudentByUser(userName);
			char userCourseYear = student.getUserYear();
			Boolean dual = student.isIntensiveDual();

			if (userPass.equals(pass)) {
				System.out.println("La contraseña es igual que la anterior");
				client.sendEvent(Events.ON_REGISTER_SAME_PASSWORD.value, "Escoge una contraseña que sea diferente");
			} else {

			}

		});

	}

	private DataListener<String> filterBySubject() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject message = gson.fromJson(data, JsonObject.class).getAsJsonObject("message");
				if (message == null || !message.has("userId")) {
					client.sendEvent(Events.ON_FILTER_ERROR.value, "Formato de datos invalido");
					System.out.println("Datos incorrectos");
				} else {
					int userId = message.get("userId").getAsInt();
					List<Documents> documents = getDocumentsBySubject(userId);
					List<String> links = new ArrayList<>();
					for (Documents document : documents) {
						links.add(document.getLink());
					}
					String jsonDocuments = gson.toJson(links);
					System.out.println(jsonDocuments);
					client.sendEvent(Events.ON_FILTER_BY_SUBJECT_RESPONSE.value, jsonDocuments);
				}
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_FILTER_ERROR.value, "Error de servidor");
			}
		});
	}

	private DataListener<String> filterByCycle() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject message = gson.fromJson(data, JsonObject.class).getAsJsonObject("message");
				if (message == null || !message.has("userId")) {
					client.sendEvent(Events.ON_FILTER_ERROR.value, "Formato de datos invalido");
					System.out.println("Datos incorrectos");
				} else {
					int userId = message.get("userId").getAsInt();
					List<Documents> documents = getDocumentsByCycle(userId);
					List<String> links = new ArrayList<>();
					for (Documents document : documents) {
						links.add(document.getLink());
					}
					String jsonDocuments = gson.toJson(links);
					client.sendEvent(Events.ON_FILTER_BY_SUBJECT_RESPONSE.value, jsonDocuments);
				}
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_FILTER_ERROR.value, "Error de servidor");
			}
		});
	}

	private DataListener<String> filterByCourse() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject message = gson.fromJson(data, JsonObject.class).getAsJsonObject("message");
				if (message == null || !message.has("userId")) {
					client.sendEvent(Events.ON_FILTER_ERROR.value, "Formato de datos invalido");
					System.out.println("Datos incorrectos");
				} else {
					int userId = message.get("userId").getAsInt();
					List<Documents> documents = getDocumentsByCourse(userId);
					List<String> links = new ArrayList<>();
					for (Documents document : documents) {
						links.add(document.getLink());
					}
					String jsonDocuments = gson.toJson(links);
					client.sendEvent(Events.ON_FILTER_BY_SUBJECT_RESPONSE.value, jsonDocuments);
				}
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_FILTER_ERROR.value, "Error de servidor");
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
		Query<Client> q = session.createQuery(hql, Client.class);
		clients = q.list();
		return clients;
	}

	public List<Documents> getDocumentsBySubject(int userId) {
		String hqlForStudentCourse = "select m.course.courseId from Matriculation as m where m.student.userId =:userId";
		Query<Integer> queryForCourseId = session.createQuery(hqlForStudentCourse, Integer.class);
		queryForCourseId.setParameter("userId", userId);
		int courseId = queryForCourseId.getSingleResult();

		String hqlForSubjectId = "select s.subjectId from Subject as s where s.course.courseId =:courseId";
		Query<Integer> queryForSubjectId = session.createQuery(hqlForSubjectId, Integer.class);
		queryForSubjectId.setParameter("courseId", courseId);
		int subjectId = queryForSubjectId.getSingleResult();

		String hql = "from Documents where subject.subjectId =:subjectId";
		Query<Documents> query = session.createQuery(hql, Documents.class);
		query.setParameter("subjectId", subjectId);
		List<Documents> documents = query.list();
		return documents;
	}

	public List<Documents> getDocumentsByCourse(int userId) {
		String hqlForStudentCourse = "select m.course.courseId from Matriculation as m where m.student.userId =:userId";
		Query<Integer> queryForCourseId = session.createQuery(hqlForStudentCourse, Integer.class);
		queryForCourseId.setParameter("userId", userId);
		int courseId = queryForCourseId.getSingleResult();

		String hql = "from Documents where course.courseId =:courseId";
		Query<Documents> query = session.createQuery(hql, Documents.class);
		query.setParameter("courseId", courseId);
		List<Documents> documents = query.list();
		return documents;
	}

	public List<Documents> getDocumentsByCycle(int userId) {
		String hql = "from Documents where allowedCourse =:allowedCourse";
		Query<Documents> query = session.createQuery(hql, Documents.class);
		query.setParameter("allowedCourse", 1);
		List<Documents> documents = query.list();
		return documents;
	}
}
