import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 19/08/2011
 * Time: 15:31
 */
public class PhoneRegistration implements RegistrationInterface {
    private String userDetails = null;
    public String requestDetails() {
        try {
            Register.serverOutput.println("Enter a phone number:");
            String number = Register.serverInput.readLine();
            if(!number.equals("")) userDetails = number;
        } catch (IOException e) {
            Register.serverOutput.println("Something went wrong when attempting to input your phone details.");
            Register.serverOutput.println(e.getMessage());
            return null;
        }
        return userDetails;
    }
}
