import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 21/07/2011
 * Time: 14:14
 * see http://growl.info/documentation/developer/protocol.php for packet requirements
 */
public class GrowlNotificationPacket {
    public static final byte GROWL_PROTOCOL_VERSION = 1;
    public static final byte GROWL_TYPE_NOTIFICATION_NOAUTH = 5;
    String application;
    String notification;
    String title;
    String description;
    int priority;
    boolean sticky;
    int flags;

    public GrowlNotificationPacket(String application, String notification, String title, String description, int priority, boolean sticky) {
        this.application = new String(application.getBytes(Charset.forName("UTF-8")));
        this.notification = new String(notification.getBytes(Charset.forName("UTF-8")));
        this.title = new String(title.getBytes(Charset.forName("UTF-8")));
        this.description = new String(description.getBytes(Charset.forName("UTF-8")));
        this.priority = priority;
        this.sticky = sticky;
    }

    public byte[] payload() {
	ByteArrayOutputStream bout = null;
        try {
            bout = new ByteArrayOutputStream();

            bout.write(GROWL_PROTOCOL_VERSION);
            bout.write(GROWL_TYPE_NOTIFICATION_NOAUTH);

            //flags contains a value from -2 to 2 and a sticky (bool) value in the lowest nibble

            flags = (short) ((this.priority & 0x07) * 2);

            if (this.priority < 0) {
                flags |= 0x08;
            }

            if (this.sticky) {
                flags |= 0x01;
            }

            bout.write(flags >>> 8);
            bout.write(flags & 0xff);

            //data lengths

            bout.write(this.notification.length() >>> 8);
            bout.write(this.notification.length() & 0xff);

            bout.write(this.title.length() >>> 8);
            bout.write(this.title.length() & 0xff);

            bout.write(this.description.length() >>> 8);
            bout.write(this.description.length() & 0xff);

            bout.write(this.application.length() >>> 8);
            bout.write(this.application.length() & 0xff);

            //data

            byte[] bytes = this.notification.getBytes(Charset.forName("UTF-8"));
            bout.write(bytes);

            bytes = this.title.getBytes(Charset.forName("UTF-8"));
            bout.write(bytes);

            bytes = this.description.getBytes(Charset.forName("UTF-8"));
            bout.write(bytes);

            bytes = this.application.getBytes(Charset.forName("UTF-8"));
            bout.write(bytes);
            //must be a byte array
            return bout.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
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
