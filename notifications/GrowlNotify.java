import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.IOException;
/**
 * Created by IntelliJ IDEA.
 * uk.ac.homenetworks.notify.User: rob
 * Date: 09/07/2011
 * Time: 15:24
 */
public class GrowlNotify implements Notify{
    private static final int GROWL_UDP_PORT = 9887;

    public boolean sendNotification(String notificationId, String userDetails, String message) {
        OutputStreamWriter osw = null;
	try{
            String inetAddr = userDetails; //address to send to
            GrowlRegistrationPacket packet = new GrowlRegistrationPacket("Homework"); //register application
            packet.addNotification("networkevent", true);//add notification for application
            byte[] packetPayload = packet.payload();//create message to send to host at inetAddr
            sendPacket(packetPayload, inetAddr);//send registration

            GrowlNotificationPacket growlNotificationPacket = new GrowlNotificationPacket("Homework", "networkevent", "Router Notification", message, 1, false);//create a notification
            packetPayload = growlNotificationPacket.payload();//create notification data to send
            if(sendPacket(packetPayload, inetAddr)){ //send notification.
	    	HWDBResponse.respond(notificationId, true, "Growl notification sent");
	    } else {
		HWDBResponse.respond(notificationId, false, "Growl notification was not sent");
		return false;
	    }
	    //Connect to Google App Engine to Log the notification that was sent
	    String data = URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(userDetails, "UTF-8");
	    data += "&" + URLEncoder.encode("body", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
	    HttpURLConnection urlConnection = RemoteNotify.configureConnection("growl", data);
	    osw = new OutputStreamWriter(urlConnection.getOutputStream());
	    osw.write(data);
	    osw.flush();
	    System.out.println(urlConnection.getResponseCode());
	    System.out.println(urlConnection.getResponseMessage());
	    if(urlConnection.getResponseCode() != 200) System.err.println("Failed to log notification");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        } finally{
	    try{
	        osw.close();
	    }catch(IOException ex){
		ex.printStackTrace();
		return false;
	    }
	}
        return true;
    }

    private boolean sendPacket(byte[] packet,String inetAddr){
        try{
            DatagramPacket datagramPacket = new DatagramPacket(packet, packet.length, InetAddress.getByName(inetAddr), GROWL_UDP_PORT);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(datagramPacket);
	    return true;
        } catch (Exception e){
            e.printStackTrace();
	    return false;
        }
    }
}
