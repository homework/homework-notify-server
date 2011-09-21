import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import com.google.gson.Gson;
/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 17/08/2011
 * Time: 11:59
 */
public class PhoneNotify implements Notify {
    public boolean sendNotification(String userDetails, String message) {
        NotificationResponse nr = RemoteNotify.sendNotification("sms", userDetails, message);
	if(nr != null){
	    if(nr.code == 200){
		try{
		    String data = URLEncoder.encode("notification", "UTF-8") + "=" + URLEncoder.encode(nr.notificationId, "UTF-8");
		    HttpURLConnection urlConnection = RemoteNotify.configureConnection("status", data);
		    OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
		    osw.write(data);
		    osw.flush();
		    osw.close();
		    if(urlConnection.getResponseCode() == 200){
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null){
			    builder.append(line);
			}
			String jsonString = builder.toString();
			reader.close();
			Gson gson = new Gson();
			NotificationResponse response = gson.fromJson(jsonString, NotificationResponse.class);
			if(response.code == 200){
			    HWDBResponse.respond(nr.notificationId, true, "Message Sent");
			    return true;
			} else{
			    HWDBResponse.respond(nr.notificationId, false, response.message);
			    return false;
			}
		    } else {
			HWDBResponse.respond(nr.notificationId, false, urlConnection.getResponseMessage());
			return false;
		    }
		} catch (Exception e){
		    HWDBResponse.respond(nr.notificationId, false, "Error getting status");
		    return false;
		}
	    } else{
		HWDBResponse.respond(nr.notificationId, false, nr.message);
		return false;
	    }
	}
	
	return false;
    }
}