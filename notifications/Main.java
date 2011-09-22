import org.hwdb.srpc.*;
import org.hwdb.srpc.Service;

import java.sql.*;
/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 18/08/2011
 * Time: 11:11
 */
public class Main implements Runnable{
    Service service;
    
    public Main() {
        init();
    }
    public String[] splitString(String string, String delimeter){
	String[] result;
	int last, from, found, records;
	if(string.equals("")) return new String[0];
	from = 0;
	records = 0;
	while(true){
	    found = string.indexOf(delimeter, from);
	    if(found == -1) break;
	    records++;
	    from = found + delimeter.length();
	}
	result = new String[records];
	if(records == 1)result[0] = string;
	else{
	    last = from = found = 0;
	    for(int i = 0; i < records; i++){
		found = string.indexOf(delimeter, from);
		if(found == -1)result[i] = string.substring(last + delimeter.length(), string.length());
		else if(found == 0)result[i] = "";
		else result[i] = string.substring(from, found);
		last = found;
		from = found + delimeter.length();
	    }
	}
	return result;
    }

    public void run() {
	try{
	    Message query;
	    Statement stmt;
	    ResultSet rs;
	    while((query = service.query())!= null){
		String line = query.getContent();
		System.out.println(line);
		query.getConnection().response("OK");	
		String[] lines = line.split("\n");
		String eventData = lines[2];
		String[] eventDataArray = splitString(eventData, "<|>");
		String notificationId = eventDataArray[1];
		String destination = eventDataArray[2];
		String service = eventDataArray[3];
		String message = eventDataArray[4];
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/hw";
		java.sql.Connection dbCon = DriverManager.getConnection(url, "root", "");
		stmt = dbCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if(service.equalsIgnoreCase("any")){
		    rs = stmt.executeQuery(String.format("select * from NotificationRegistrations where Endpoint = \"%s\" order by Priority",destination));
		} else {
		    rs = stmt.executeQuery(String.format("select * from NotificationRegistrations where Endpoint = \"%s\" and Service = \"%s\"", destination, service));
		}		
		while (rs.next()){
		    String userDetails = rs.getString("UserDetails");
		    String className = rs.getString("Service");
		    if(sendNotification(notificationId, className, userDetails, message)) break;
		}
		dbCon.close();
	    }	
	} catch(Exception e){
	    e.printStackTrace();
	}
    }
    org.hwdb.srpc.Connection conn;
    int port;
    SRPC srpc;
    String serviceName = "Handler";
    public void init() {
        try {
	    srpc = new SRPC();
	    service = srpc.offer(serviceName);
	    new Thread(this).start();
	    conn = srpc.connect("localhost", 987, "HWDB");
	    port = srpc.details().getPort();
	    String results = conn.call(String.format("SQL:subscribe EventsLast 127.0.0.1 %d %s", port, serviceName));
        } catch (Exception e){
	    e.printStackTrace();
	}
    }

    public boolean sendNotification(String notificationId, String notification, String userDetails, String eventName) {
	boolean result = false;
        try {
            notification = getFullClassName(notification);
            ClassLoader loader = Main.class.getClassLoader();
            Class c;
            Notify notify;
            c = loader.loadClass(notification);
            notify = (Notify) c.newInstance();
            result = notify.sendNotification(notificationId, userDetails, eventName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
	    return false;
        } catch (InstantiationException e) {
            e.printStackTrace();
	    return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
	    return false;
        }
	return result;
    }

    private String getFullClassName(String name) {
	String temp = name.toLowerCase();
	name = temp.substring(0,1).toUpperCase() + temp.substring(1);
        return name + "Notify";
    }

    public static void main(String[] args) {
        new Main();
    }
}
