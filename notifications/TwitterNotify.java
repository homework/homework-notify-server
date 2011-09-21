/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 22/07/2011
 * Time: 14:05
 */
public class TwitterNotify implements Notify {
    public boolean sendNotification(String userDetails, String message) {
        NotificationResponse nr = RemoteNotify.sendNotification("twitter", userDetails, message);
	return true;
    }
}
