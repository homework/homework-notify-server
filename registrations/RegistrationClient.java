import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
public class RegistrationClient{
	public static void main(String[] args){
		try{
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter router address");			
       			InetAddress addr = InetAddress.getByName(stdIn.readLine());
			int port = 5678;
			Socket socket = new Socket(addr, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
