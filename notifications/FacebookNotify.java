/**
 * Created by IntelliJ IDEA.
 * uk.ac.homenetworks.notify.User: rob
 * Date: 07/07/2011
 * Time: 16:27
 */

public class FacebookNotify implements Notify{

    public boolean sendNotification(String userDetails, String body) {
        NotificationResponse nr = RemoteNotify.sendNotification("facebook", userDetails, body);
	if(nr != null){
	    if(nr.code == 200){
		HWDBResponse.respond(nr.notificationId, true, "App Engine Sent Notification");
	    }
	}
	HWDBResponse.respond(nr.notificationId, false, nr.message);
        return false;

    }
}
