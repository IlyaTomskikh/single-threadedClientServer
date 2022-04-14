import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws InterruptedException, IOException {

        //Establish the connection to the server
        try(var server = new Socket("localhost", 8080);
            var br = new BufferedReader(new InputStreamReader(System.in));
            var oos = new DataOutputStream(server.getOutputStream());
            var ois = new DataInputStream(server.getInputStream())) {
            System.out.println("Client has connected to the server.\n");
            System.out.println("Please, wait...");

            while(!server.isOutputShutdown()) {
                System.out.print("Enter your message: ");
                Thread.sleep(1000);
                var message = br.readLine();
                System.out.println();
                oos.writeUTF(message);
                oos.flush();
                System.out.println("Client has sent the message {" + message + "} to server.");
                Thread.sleep(1000);
                //awaiting of the response from the server
                if(message.equalsIgnoreCase("/quit/") ||
                        message.equalsIgnoreCase("/end/") ||
                        message.equalsIgnoreCase("/exit/") ||
                        message.equalsIgnoreCase("/close/")){
                    System.out.println("Client is disconnecting...");
                    Thread.sleep(2000);
                    if(ois.read() > -1) {
                        String in = ois.readUTF();
                        System.out.println("Server: " + in);
                    }
                    break;
                }
                System.out.println("Client has sent message & started waiting for data from the server...");
                Thread.sleep(2000);
                if(ois.read() > -1) {
                    var in = ois.readUTF();
                    System.out.println(in);
                }
            }
            br.close();
            oos.close();
            ois.close();
            server.close();
            System.out.println("Closing connections & channels on clients' Side - DONE.");
        }
    }
}