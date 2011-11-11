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
    public boolean sendNotification(String notificationId, String userDetails, String message) {
        NotificationResponse nr = RemoteNotify.sendNotification("phone", userDetails, message);
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
			    return true;
			} else{
			    return false;
			}
		    } else {
			return false;
		    }
		} catch (Exception e){
		    return false;
		}
	    } else{
		return false;
	    }
	}
	
	return false;
    }
}
