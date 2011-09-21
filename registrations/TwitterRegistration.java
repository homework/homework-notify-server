import java.io.*;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: rob
 * Date: 19/08/2011
 * Time: 12:21
 */
public class TwitterRegistration implements RegistrationInterface {
    private String userDetails;

    public String requestDetails() {
        try {
            Register.serverOutput.println("Please enter the Twitter id that you wish to receive notifications:");
            String twitterId = Register.serverInput.readLine();
            if(!"".equals(twitterId)) userDetails = twitterId;
        } catch (IOException e){
            Register.serverOutput.println("There was a problem getting your Twitter name");
            Register.serverOutput.println(e.getMessage());
            return null;
        }

        return userDetails;
    }
}
