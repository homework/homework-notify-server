import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 19/08/2011
 * Time: 11:21
 */
public class Register {
    public static ServerSocket serverSocket;
    public static PrintWriter serverOutput;
    public static BufferedReader serverInput;
    public static String userDetails;
    public static void main(String[] args) {
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(5678);
            clientSocket = serverSocket.accept();
            serverOutput = new PrintWriter(clientSocket.getOutputStream(), true);
            serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            serverOutput.println("To register, you need to answer some questions.");
            String notification;
	    String name;
	    String priorityString;
	    int priority;

            serverOutput.println("Enter your name: ");
            name = serverInput.readLine();
            serverOutput.println("Enter Notification Service: ");
            notification = serverInput.readLine();
	    serverOutput.println("Enter Priority: ");
	    priorityString = serverInput.readLine();
	    priority = Integer.parseInt(priorityString);
            notification = notification.toLowerCase();
            String temp = notification.substring(0, 1).toUpperCase();
            notification = temp + notification.substring(1);
            String className = getFullClassName(notification);
            ClassLoader loader = Register.class.getClassLoader();
            Class c;
            RegistrationInterface registrationInterface;
            c = loader.loadClass(className);
            registrationInterface = (RegistrationInterface) c.newInstance();
            userDetails = registrationInterface.requestDetails();
            if (userDetails != null) {
                register(name, notification, priority, userDetails);
		System.exit(0);
            } else {
                serverOutput.println("There was a problem getting details required for registration. You have not been registered for " + notification);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static String getFullClassName(String name) {
        return name + "Registration";
    }

    public static void register(String endpoint, String service,int priority, String userDetails) {
	try{
	    Class.forName("com.msql.jdbc.Driver");
	    String url = "jdbc:mysql://localhost:3306/hw";
	    Connection conn = DriverManager.getConnection(url, "root", "");
	    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    ResultSet rs = stmt.executeQuery(String.format("insert into NotificationRegistrations(Endpoint, Service, Priority, UserDetails) values (\"%s\", \"%s\", %i, \"%s\")", endpoint, service, priority, userDetails));
	    conn.close();
	    return;
	} catch (Exception e){
	    e.printStackTrace();
	}
    }
}
