import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 19/08/2011
 * Time: 16:24
 */
public class FacebookRegistration implements RegistrationInterface{
    public String requestDetails() {
        String userDetails = null;
        Register.serverOutput.println("Enter email address to receive notifications:");
        try {
            String email = Register.serverInput.readLine();
            if(email != null & !email.equals("")) userDetails = email;
        } catch (IOException e) {
            Register.serverOutput.println("An error occurred when obtaining your email address.");
            Register.serverOutput.println(e.getMessage());
        }
        return userDetails;
    }
}
