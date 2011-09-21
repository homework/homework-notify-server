import org.hwdb.srpc.*;
import org.hwdb.srpc.Service;
public class HWDBResponse{
    public static void respond(String notificationId, boolean status, String message){
	try{
	    String serviceName = "Handler";
	    SRPC srpc = new SRPC();
	    Service service = srpc.offer(serviceName);
	    Connection conn = srpc.connect("localhost", 987, "HWDB");
	    int port = srpc.details().getPort();
	    conn.call(String.format("SQL:insert into NotificationResponse (NotificationId, Status, Message) values(\"%s\", %b, \"%s\")\n %d %s", notificationId, status, message, port, serviceName));
	} catch (Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
    }
}
