import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 19/08/2011
 * Time: 16:51
 */
public class PushRegistration implements RegistrationInterface{

    public String requestDetails() {
        String userDetails = null;
        try {
            Register.serverOutput.println("Enter device id:");
            String deviceId = Register.serverInput.readLine();
            if(!deviceId.equals("")) userDetails = deviceId;
        } catch (IOException e) {
            Register.serverOutput.println("Something went wrong when attempting to input your Push details.");
            Register.serverOutput.println(e.getMessage());
            return null;
        }
        return userDetails;
    }
}
