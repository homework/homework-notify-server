public class NotificationResponse{
    public NotificationResponse(){

    }

    public NotificationResponse(int c, String m, String n){
	this.code = c;
	this.message = m;
	this.notificationId = n;
    }

    public int code;
    public String message;
    public String notificationId;
}
