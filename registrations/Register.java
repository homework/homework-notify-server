import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.Properties;
/**
 * Created by IntelliJ IDEA.
 * User: rjs
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
            String className = temp + notification.substring(1);
            className = getFullClassName(className);
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
	    String data = URLEncoder.encode("service", "UTF-8") + "=" + URLEncoder.encode(service, "UTF-8");
	    data += "&" + URLEncoder.encode("userdetails", "UTF-8") + "=" + URLEncoder.encode(userDetails, "UTF-8");
	    Properties prop = new Properties();
	    prop.load(new FileInputStream("/etc/homework/notification.conf"));
	    String router = prop.getProperty("router_id");
	    String urlString = "https://homework-notify.appspot.com/notify/1/" + router + "/register";
	    URL appURL = new URL(urlString);
	    HttpURLConnection urlConnection = (HttpURLConnection)appURL.openConnection();
	    urlConnection.setRequestMethod("POST");
	    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    urlConnection.setRequestProperty("Content-Length", Integer.toString(data.getBytes("UTF-8").length));
	    urlConnection.setUseCaches(false);
	    urlConnection.setDoInput(true);
	    urlConnection.setDoOutput(true);
	    OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
	    osw.write(data);
	    osw.flush();
	    osw.close();
	    if(urlConnection.getResponseCode() >= 400){
		BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
		String line;
		while((line = reader.readLine()) != null){
		    System.out.println(line);
		}
	    } else if(urlConnection.getResponseCode() == 200){
	        Class.forName("com.mysql.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/Homework";
	        Connection conn = DriverManager.getConnection(url, "homeuser", "homework");
	        Statement stmt = conn.createStatement();
	        stmt.executeUpdate(String.format("insert into NotificationRegistrations(Endpoint, Service, Priority, UserDetails) values (\"%s\", \"%s\", %d, \"%s\")", endpoint.toLowerCase(), service.toLowerCase(), priority, userDetails));
	        conn.close();	
	    } else{
		BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String line;
		while((line = reader.readLine())!=null){
		    System.out.println(line);
		}
	    }
	    urlConnection.getInputStream();
	    return;
	} catch (Exception e){
	}
    }
}
