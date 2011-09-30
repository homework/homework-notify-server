import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 20/07/2011
 * Time: 16:19
 * See http://growl.info/documentation/developer/protocol.php for packet structure details
 */
public class GrowlRegistrationPacket {
    public static byte GROWL_PROTOCOL_VERSION = 1;
    public static byte GROWL_TYPE_REGISTRATION_NOAUTH = 4;
    private String application;
    private ArrayList<String> notifications; //store notifications for the application
    private ArrayList<Integer> defaults; //which ones are enabled

    public GrowlRegistrationPacket(String application) {
        this.application = application;
        this.notifications = new ArrayList<String>();
        this.defaults = new ArrayList<Integer>();
    }

    public void addNotification(String notification, boolean enabled) {
        //add a new notification and add its position to the defaults array to enable it if required
        this.notifications.add(notification);
        if (enabled) this.defaults.add(this.notifications.size() - 1);
    }

    public byte[] payload() {
	ByteArrayOutputStream bout = null;
        try {
            //this is the content of the message that will be sent to register a new application in growl
            //it is needed in a byte array.
            bout = new ByteArrayOutputStream();
            bout.write(GROWL_PROTOCOL_VERSION);
            bout.write(GROWL_TYPE_REGISTRATION_NOAUTH);
            //lengths of data
            byte[] bytes = this.application.getBytes(Charset.forName("UTF-8"));
            bout.write(bytes.length >>> 8);
            bout.write(bytes.length & 0xff);
            bout.write(this.notifications.size());
            bout.write(this.defaults.size());
            bout.write(bytes);
            //add notifications
            for (String notification : this.notifications) {
                bytes = notification.getBytes(Charset.forName("UTF-8"));
                bout.write(bytes.length >>> 8);
                bout.write(bytes.length & 0xff);
                bout.write(bytes);
            }
            //add defaults to specify which notifications are enabled
            for (Integer def : this.defaults) {
                bout.write(def);
            }
            //must be a byte array
            return bout.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
	    if(bout != null){
		try{
		    bout.close();
		} catch (IOException e){
		    e.printStackTrace();
		}
	    }
	}
        return null;
    }
}
