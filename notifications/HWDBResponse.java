import java.io.IOException;
import org.hwdb.srpc.Connection;
import org.hwdb.srpc.SRPC;
import org.hwdb.srpc.Service;
public class HWDBResponse{
    public static void respond(String notificationId, boolean status, String message){
	Connection conn = null;
	try{
	    String serviceName = "Handler";
	    SRPC srpc = new SRPC();
	    Service service = srpc.offer(serviceName);
	    conn = srpc.connect("localhost", 987, "HWDB");
	    int port = srpc.details().getPort();
	    conn.call(String.format("SQL:insert into NotificationResponse (NotificationId, Status, Message) values(\"%s\", %b, \"%s\")\n %d %s", notificationId, status, message, port, serviceName));
	} catch (IOException e){
	    e.printStackTrace();
	    System.exit(1);
	} 
    }
}
