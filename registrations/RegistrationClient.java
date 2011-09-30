import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
public class RegistrationClient{
	public static void main(String[] args){
		try{
			InetAddress addr = InetAddress.getByName("10.2.0.2");
			int port = 5678;
			Socket socket = new Socket(addr, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer, fromUser;
			while ((fromServer = in.readLine()) != null){
				System.out.println(fromServer);
				fromUser = stdIn.readLine();
				if(fromUser != null){
					out.println(fromUser);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
