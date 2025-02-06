package com.Reto2.RetoServer.SocketIO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.lang.reflect.Type;
import java.sql.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
import com.google.gson.reflect.TypeToken;

import jakarta.persistence.NoResultException;

public class SocketIOModule {

	private SocketIOServer server = null;
	SessionFactory sesion = HibernateUtil.getSessionFactory();
	Session session = sesion.openSession();
	Transaction transaction = null;

	public SocketIOModule(SocketIOServer server) {
		super();
		this.server = server;

		// Default events (for control the connection of clients)
		server.addConnectListener(onConnect());
		server.addDisconnectListener(onDisconnect());

		// Custom events
		server.addEventListener(Events.ON_LOGIN.value, String.class, this.login());
		server.addEventListener(Events.ON_REGISTER_ANSWER.value, String.class, this.register());
		server.addEventListener(Events.ON_GET_ALL.value, MessageInput.class, this.getAll());
		server.addEventListener(Events.ON_FILTER_BY_COURSE.value, String.class, this.filterByCourse());
		server.addEventListener(Events.ON_FILTER_BY_CYCLE.value, String.class, this.filterByCycle());
		server.addEventListener(Events.ON_FILTER_BY_SUBJECT.value, String.class, this.filterBySubject());
		server.addEventListener(Events.ON_LOGOUT.value, MessageInput.class, this.logout());
		server.addEventListener(Events.ON_REGISTER_ANSWER.value, String.class, this.register());
		server.addEventListener(Events.ON_GET_EXTERNAL_COURSES.value, String.class, this.getExternalCourses());
		server.addEventListener(Events.ON_CHANGE_PASSWORD.value, String.class, this.changePassword());
		server.addEventListener(Events.ON_GET_REUNIONS.value, String.class, this.getReunions());
		server.addEventListener(Events.ON_ACCEPT_REUNION.value, String.class, this.acceptReunion());
		server.addEventListener(Events.ON_REJECT_REUNION.value, String.class, this.rejectReunion());
		server.addEventListener(Events.ON_FORCE_REUNION.value, String.class, this.forceReunion());
		server.addEventListener(Events.ON_CREATE_REUNION.value, String.class, this.createReunion());
		server.addEventListener(Events.ON_FORGOT_PASSWORD.value, String.class, this.newPassword());

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

				System.out.println(userName + ":" + userPass);

				Client loginClient = sendClient(userName);
				if(loginClient != null) {
				String name = loginClient.getUserName();
				String pass = loginClient.getPass();
				Boolean userType = loginClient.isUserType();
					if (userName.equals(name) && userPass.equals(pass)) {
						if (loginClient.getRegistered() == true) {
							if (userType == true) {
								System.out.println("usuario registrado");
								JsonObject responseJson = new JsonObject();
								Professor professor = getProfessorByUser(userName);
								professor.setName(name);
								responseJson.add("loginClient", gson.toJsonTree(loginClient));
								responseJson.add("professor", gson.toJsonTree(professor));

								String answerMessage = gson.toJson(responseJson);
								MessageOutput messageOutput = new MessageOutput(answerMessage);

								client.sendEvent(Events.ON_LOGIN_SUCCESS.value, messageOutput);
							} else {
								System.out.println("usuario registrado");
								JsonObject responseJson = new JsonObject();
								Student student = getStudentByUser(userName);
								Course course = getUserCourseByMatriculation(loginClient.getUserId());
								responseJson.add("loginClient", gson.toJsonTree(loginClient));
								responseJson.add("student", gson.toJsonTree(student));
								responseJson.add("course", gson.toJsonTree(course));
								String answerMessage = gson.toJson(responseJson);
								MessageOutput messageOutput = new MessageOutput(answerMessage);

								client.sendEvent(Events.ON_LOGIN_SUCCESS.value, messageOutput);
							}
						} else {

							if (userType == true) {
								System.out.println("usuario registrado");
								JsonObject responseJson = new JsonObject();
								Professor professor = getProfessorByUser(userName);
								professor.setName(name);
								responseJson.add("loginClient", gson.toJsonTree(loginClient));
								responseJson.add("professor", gson.toJsonTree(professor));

								String answerMessage = gson.toJson(responseJson);
								MessageOutput messageOutput = new MessageOutput(answerMessage);

								client.sendEvent(Events.ON_REGISTER.value, messageOutput);

							} else {
								System.out.println("usuario no registrado");
								JsonObject responseJson = new JsonObject();
								Student student = getStudentByUser(userName);
								Course course = getUserCourseByMatriculation(loginClient.getUserId());
								responseJson.add("loginClient", gson.toJsonTree(loginClient));
								responseJson.add("student", gson.toJsonTree(student));
								responseJson.add("course", gson.toJsonTree(course));
								String answerMessage = gson.toJson(responseJson);
								MessageOutput messageOutput = new MessageOutput(answerMessage);
								client.sendEvent(Events.ON_REGISTER.value, messageOutput);
							}
						}

					} else {
						client.sendEvent(Events.ON_LOGIN_FAIL.value, "Login incorrecto");
						System.out.println("El usuario no ha podido loguearse: " + userName);
					}
				}else {
					client.sendEvent(Events.ON_LOGIN_FAIL.value, "El usuario no existe en la BBDD");
				}
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_LOGIN_FAIL.value, "Error de servidor");
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
			try {
				System.out.println("Client from " + client.getRemoteAddress() + " wants to register");
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
				if (!jsonObject.has("username") || !jsonObject.has("userpass")) {
					client.sendEvent(Events.ON_LOGIN_FAIL.value, "Formato de datos invalido");
					System.out.println(jsonObject.toString());
					System.out.println("Datos incorrecto");
				} else {
					String userName = jsonObject.get("username").getAsString();
					String userSurname = jsonObject.get("surname").getAsString();
					String userSecondSurname = jsonObject.get("secondsurname").getAsString();
					String userPass = jsonObject.get("userpass").getAsString();
					String userDni = jsonObject.get("dni").getAsString();
					String userDirection = jsonObject.get("direction").getAsString();
					int userTelephone = jsonObject.get("telephone").getAsInt();

					Client loginClient = sendClient(userName);
					String pass = loginClient.getPass();

					System.out.println("datos recogidos");
					if (userPass.equals(pass)) {
						System.out.println("La contraseña es igual que la anterior");
						client.sendEvent(Events.ON_REGISTER_SAME_PASSWORD.value,
								"Escoge una contraseña que sea diferente");
					} else {
						Client newUserData = new Client(userName, userSurname, userSecondSurname, userPass, userDni,
								userDirection, userTelephone, true);
						updateUserData(loginClient.getUserName(), newUserData);
						client.sendEvent(Events.ON_REGISTER_SUCCESS.value, "Has registrado tu usuario correctamente");
						System.out.println("todo correcto");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_REGISTER_FAIL.value, "Error del servidor");
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

	private DataListener<String> getExternalCourses() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				List<Externalcourse> externalCourses = getExternalCoursesForUser();
				String jsonExternalCourses = gson.toJson(externalCourses);
				client.sendEvent(Events.ON_GET_EXTERNAL_COURSES_ANSWER.value, jsonExternalCourses);
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_GET_EXTERNAL_COURSES_ERROR.value, "Error de servidor");
			}
		});
	}

	private DataListener<String> changePassword() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
				if (!jsonObject.has("userId") || !jsonObject.has("newPassword")) {
					client.sendEvent(Events.ON_CHANGE_PASSWORD_FAIL.value, "Formato de datos invalido");
				}
				int userId = jsonObject.get("userId").getAsInt();
				String newPassword = jsonObject.get("newPassword").getAsString();
				if (changeUserPassword(userId, newPassword))
					client.sendEvent(Events.ON_CHANGE_PASSWORD_ANSWER.value, "OK!");
				else
					client.sendEvent(Events.ON_CHANGE_PASSWORD_FAIL.value, "No se ha podido cambiar la contraseña");
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_CHANGE_PASSWORD_FAIL.value, "Error de servidor");
			}
		});
	}

	private DataListener<String> getReunions() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls()
						.setPrettyPrinting().create();
				JsonObject message = gson.fromJson(data, JsonObject.class).getAsJsonObject("message");
				if (!message.has("userId") || message == null) {
					client.sendEvent(Events.ON_GET_REUNIONS_ERROR.value, "Formato de datos invalido");
				}
				int userId = message.get("userId").getAsInt();
				List<Reunion> reunions = getReunions(userId);
				List<Professor> professors = getProfessors();
				for (int i = 0; i < professors.size(); i++) {
					professors.get(i).setName(professors.get(i).getClient().getUserName());
				}
				String professorsMessage = gson.toJson(professors);
				String reunionsMessage = gson.toJson(reunions);
				client.sendEvent(Events.ON_GET_REUNIONS_ANSWER.value, reunionsMessage, professorsMessage);
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_GET_REUNIONS_ERROR.value, "Error de servidor");
			}
		});
	}

	private DataListener<String> forceReunion() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
				if (jsonObject == null || !jsonObject.has("reunionId")) {
					client.sendEvent(Events.ON_FORCE_REUNION_ERROR.value, "Formato de datos invalido");
				}
				int reunionId = jsonObject.get("reunionId").getAsInt();
				if (forceReunionOnDatabase(reunionId))
					client.sendEvent(Events.ON_FORCE_REUNION_ANSWER.value, "OK!");
				else
					client.sendEvent(Events.ON_FORCE_REUNION_ERROR.value, "No se ha podido cambiar la contraseña");
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_FORCE_REUNION_ERROR.value, "Error de servidor");
			}
		});
	}

	private DataListener<String> rejectReunion() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
				if (jsonObject == null || !jsonObject.has("reunionId")) {
					client.sendEvent(Events.ON_REJECT_REUNION_ERROR.value, "Formato de datos invalido");
				}
				int reunionId = jsonObject.get("reunionId").getAsInt();
				if (rejectReunionOnDatabase(reunionId))
					client.sendEvent(Events.ON_REJECT_REUNION_ANSWER.value, "OK!");
				else
					client.sendEvent(Events.ON_REJECT_REUNION_ERROR.value, "No se ha podido cambiar la contraseña");
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_REJECT_REUNION_ERROR.value, "Error de servidor");
			}
		});
	}

	private DataListener<String> acceptReunion() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
				if (jsonObject == null || !jsonObject.has("reunionId")) {
					client.sendEvent(Events.ON_ACCEPT_REUNION_ERROR.value, "Formato de datos invalido");
				}
				int reunionId = jsonObject.get("reunionId").getAsInt();
				if (acceptReunionOnDatabase(reunionId))
					client.sendEvent(Events.ON_ACCEPT_REUNION_ANSWER.value, "OK!");
				else
					client.sendEvent(Events.ON_ACCEPT_REUNION_ERROR.value, "No se ha podido cambiar la contraseña");
			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_ACCEPT_REUNION_ERROR.value, "Error de servidor");
			}
		});
	}

	private DataListener<String> createReunion() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

				if (jsonObject == null || !jsonObject.has("reunionTheme") || !jsonObject.has("reunionReason")
						|| !jsonObject.has("reunionDate") || !jsonObject.has("reunionHour")
						|| !jsonObject.has("reunionClass") || !jsonObject.has("reunionProfessors")
						|| !jsonObject.has("reunionProfessorId")) {
					client.sendEvent(Events.ON_CREATE_REUNION_ERROR.value, "Formato de datos invalido");
					return;
				}

				String title = jsonObject.get("reunionTheme").getAsString();
				String affair = jsonObject.get("reunionReason").getAsString();
				Date day = Date.valueOf(jsonObject.get("reunionDate").getAsString());
				int hour = jsonObject.get("reunionHour").getAsInt();
				String class_ = jsonObject.get("reunionClass").getAsString();
				int reunionState = 5;

				int professorId = jsonObject.get("reunionProfessorId").getAsInt();
				Professor professor = new Professor();
				professor.setUserId(professorId);

				Type professorListType = new TypeToken<List<Professor>>() {
				}.getType();
				List<Professor> professorsList = gson.fromJson(jsonObject.get("reunionProfessors"), professorListType);

				Set<Assistant> assistants = new HashSet<>();
				for (Professor p : professorsList) {
					Assistant assistant = new Assistant();
					assistant.setProfessor(p);
					assistants.add(assistant);
				}

				Reunion newReunion = new Reunion(professor, title, affair, day, class_, reunionState, hour, assistants);

				if (createReunionInDatabase(newReunion) && putAssistants(assistants, newReunion.getReunionId()))
					client.sendEvent(Events.ON_CREATE_REUNION_ANSWER.value, "OK!");
				else
					client.sendEvent(Events.ON_CREATE_REUNION_ERROR.value, "No se ha podido crear la reunion");

			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_CREATE_REUNION_ERROR.value, "Error de servidor");
			}
		});
	}

	private DataListener<MessageInput> getAll() {
		return ((client, data, ackSender) -> {
			try {
				// This time, we simply write the message in data
				System.out.println("Client from " + client.getRemoteAddress() + " wants to getAll");

				// We access to database and... we get a bunch of people
				List<Schedule> clients = getAllClient();
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

	private DataListener<String> newPassword() {
		return ((client, data, ackSender) -> {
			try {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

				if (jsonObject == null || !jsonObject.has("message")) {
					client.sendEvent(Events.ON_FORGOT_PASSWORD_ERROR.value, "Formato de datos invalido");
					return;
				}

				String userName = jsonObject.get("message").getAsString();

				Client user = sendClient(userName);

				if (sendEmail() && updateClient(user))
					client.sendEvent(Events.ON_FORGOT_PASSWORD_ANSWER.value, "OK!");
				else
					client.sendEvent(Events.ON_FORGOT_PASSWORD_ERROR.value, "No se ha podido crear la reunion");

			} catch (Exception e) {
				e.printStackTrace();
				client.sendEvent(Events.ON_FORGOT_PASSWORD_ERROR.value, "Error de servidor");
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
			System.out.println("El usuario:" +loginUserName + "no existe en BBDD");
		}
		return client;
	}

	@SuppressWarnings("deprecation")
	public void updateUserData(String loginUserName, Client client) {
		transaction = session.beginTransaction();
		String hql = "from Client where userName  =:loginUserName";
		Query<Client> q = session.createQuery(hql, Client.class);
		q.setParameter("loginUserName", loginUserName);
		q.setMaxResults(1);

		Client user = (Client) q.getSingleResult();
		if (user != null) {
			user.setUserName(client.getUserName());
			user.setSurname(client.getSurname());
			user.setSecondSurname(client.getSecondSurname());
			user.setPass(client.getPass());
			user.setDirection(client.getDirection());
			user.setDni(client.getDni());
			user.setTelephone(client.getTelephone());
			user.setRegistered(true);
			System.out.println("el usuario no existe");
		}
		session.update(user);
		transaction.commit();

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

	public Professor getProfessorByUser(String registerName) {
		String hql = "from Professor where client.userName =:registerName";
		Query<Professor> query = session.createQuery(hql, Professor.class);
		query.setParameter("registerName", registerName);
		Professor professor = null;
		try {
			professor = query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("No client found with the username: " + registerName);
		}
		return professor;

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
	
	public List<Schedule> getSchedulesSubjects(int userId) {
        String hql = "FROM Schedule WHERE client.id = :userId";
        Query<Schedule> query = session.createQuery(hql, Schedule.class);
        query.setParameter("userId", userId);
        List<Schedule> schedules = null;
        System.out.println("Buscando horarios para el usuario ID: " + userId);
        try {
             schedules = query.getResultList();
        
        } catch (NoResultException e) {
            System.out.println("No se encontraron horarios para el cliente ID: " + userId);
        } catch (Exception e) {
            System.out.println("Error al recuperar los horarios: " + e.getMessage());
        }
        return schedules;
    
    }

	public List<Schedule> getAllClient() {
		List<Schedule> clients = new ArrayList<Schedule>();
		String hql = "from Schedule";
		Query<Schedule> q = session.createQuery(hql, Schedule.class);
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
		query.setParameter("allowedCourse", 3);
		List<Documents> documents = query.list();
		return documents;
	}

	private List<Externalcourse> getExternalCoursesForUser() {
		String hql = "from Externalcourse";
		Query<Externalcourse> query = session.createQuery(hql, Externalcourse.class);
		List<Externalcourse> externalCourse = query.list();
		return externalCourse;
	}

	@SuppressWarnings("deprecation")
	private boolean changeUserPassword(int userId, String newPassword) {
		Transaction tx = null;
		try {
			String query = "from Client as c where userId=:userId";
			Query<Client> queryResult = session.createQuery(query, Client.class);
			queryResult.setParameter("userId", userId);
			queryResult.setMaxResults(1);
			Client client = queryResult.uniqueResult();

			if (client == null) {
				return false;
			}

			client.setPass(newPassword);

			tx = session.beginTransaction();
			session.update(client);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private boolean acceptReunionOnDatabase(int reunionId) {
		Transaction tx = null;
		try {
			String query = "from Reunion as r where reunionId=:reunionId";
			Query<Reunion> queryResult = session.createQuery(query, Reunion.class);
			queryResult.setParameter("reunionId", reunionId);
			queryResult.setMaxResults(1);
			Reunion reunion = queryResult.uniqueResult();

			if (reunion == null) {
				return false;
			}

			// Solo se puede aceptar la reunion si la reunion no esta en un estado relevante
			if (reunion.getReunionState() < 10)
				reunion.setReunionState(reunion.getReunionState() + 1);
			else if (reunion.getReunionState() == 0)
				return false;
			else
				return false;

			tx = session.beginTransaction();
			session.update(reunion);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private boolean rejectReunionOnDatabase(int reunionId) {
		Transaction tx = null;
		try {
			String query = "from Reunion as r where reunionId=:reunionId";
			Query<Reunion> queryResult = session.createQuery(query, Reunion.class);
			queryResult.setParameter("reunionId", reunionId);
			queryResult.setMaxResults(1);
			Reunion reunion = queryResult.uniqueResult();

			if (reunion == null) {
				return false;
			}

			// Solo se puede rechazar si la reunion esta en un estado no relevante
			if (reunion.getReunionState() > 0)
				reunion.setReunionState(reunion.getReunionState() - 1);
			// Si la reunion esta forzada, no se puede rechazar mas veces.
			else if (reunion.getReunionState() == 11)
				return false;
			// Si la reunion ya esta rechazada, no se puede rechazar mas veces.
			else
				return false;

			tx = session.beginTransaction();
			session.update(reunion);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private boolean forceReunionOnDatabase(int reunionId) {
		Transaction tx = null;
		try {
			String query = "from Reunion as r where reunionId=:reunionId";
			Query<Reunion> queryResult = session.createQuery(query, Reunion.class);
			queryResult.setParameter("reunionId", reunionId);
			queryResult.setMaxResults(1);
			Reunion reunion = queryResult.uniqueResult();

			if (reunion == null) {
				return false;
			}

			if (reunion.getReunionState() != 0 || reunion.getReunionState() != 10)
				reunion.setReunionState(11);
			else
				return false;

			tx = session.beginTransaction();
			session.update(reunion);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	private List<Reunion> getReunions(int userId) {
		String hql = "SELECT DISTINCT r FROM Reunion r " + "LEFT JOIN FETCH r.assistants a "
				+ "LEFT JOIN FETCH a.professor " + "WHERE r.reunionId IN ("
				+ "    SELECT a.reunion.reunionId FROM Assistant a " + "    WHERE a.professor.userId = :userId"
				+ ") order by a.reunion.reunionId";

		Query<Reunion> query = session.createQuery(hql, Reunion.class);
		query.setParameter("userId", userId);
		return query.getResultList();

	}

	private List<Professor> getProfessors() {
		String hql = "from Professor";
		Query<Professor> query = session.createQuery(hql, Professor.class);
		return query.getResultList();
	}

	@SuppressWarnings("deprecation")
	private boolean createReunionInDatabase(Reunion reunion) {
		Transaction tx = null;
		try {
			if (reunion == null) {
				return false;
			}
			tx = session.beginTransaction();
			session.save(reunion);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private boolean putAssistants(Set<Assistant> assistants, int reunionId) {
		Transaction tx = null;
		try {
			if (assistants == null) {
				return false;
			}
			Reunion reunion = new Reunion();
			reunion.setReunionId(reunionId);

			tx = session.beginTransaction();
			for (Assistant assistant : assistants) {
				assistant.setReunion(reunion);
				session.save(assistant);
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	private boolean sendEmail() {
		String user = "elorclass2@gmail.com";
		String pass = "zfry gdak cgwo qmwl";
		String to = "liher.chamorropa@elorrieta-errekamari.com";
		String subject = "Contraseña olvidada";
		String message = "Se ha enviado este correo porque has olvidado tu contraseña, esta es tu nueva contraseña: nuevacontraseña.";

		System.out.println("Preparing to send email...");
		return false;
	}

	@SuppressWarnings("deprecation")
	private boolean updateClient(Client user) {
		Transaction tx = null;
		try {
			if (user == null) {
				return false;
			}

			user.setPass("nuevaContraseña");

			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

}