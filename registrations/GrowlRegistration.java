import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 19/08/2011
 * Time: 16:44
 */
public class GrowlRegistration implements RegistrationInterface{
    public String requestDetails() {
        String userDetails = null;
        try {
            Register.serverOutput.println("Enter IP Address:");
            String ip = Register.serverInput.readLine();
            if(!ip.equals("")) userDetails = ip;
        } catch (IOException e) {
            Register.serverOutput.println("Something went wrong when attempting to input your Growl Details");
            Register.serverOutput.println(e.getMessage());
            return null;
        }
        return userDetails;
    }
}
