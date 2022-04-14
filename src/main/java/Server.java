import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) throws InterruptedException, IOException {
        try (var server = new ServerSocket(8080);                  //connection to the port 8080
             var client = server.accept();                       //connection waiting
             var out = new DataOutputStream(client.getOutputStream());  //write to the socket
             var in = new DataInputStream(client.getInputStream())){    //read from the socket
            System.out.println("Connection established.");
            System.out.println("DataOutputStream and DataInputStream has been created");

            //start a dialog with the connected client in the loop, until the socket is closed
            while(!client.isClosed()){
                System.out.println("Server is reading from the client");
                var message = in.readUTF();   //as said previously the server is waiting for information from the client
                System.out.println("Client: " + message);
                System.out.println("Server try writing to channel");

                if(message.equalsIgnoreCase("/quit/") ||
                        message.equalsIgnoreCase("/end/") ||
                        message.equalsIgnoreCase("/exit/") ||
                        message.equalsIgnoreCase("/close/")){
                    System.out.println("Client has left the session.");
                    out.writeUTF("Closing the server...");
                    out.flush();
                    Thread.sleep(3000);
                    break;
                }
                out.writeUTF("Response on " + message + " - 200 OK");   //echo response

                //send the response
                out.flush();

            }

            System.out.println("Disconnected");
            System.out.println("Closing connections & channels.");

            //firstly, close channels of the socket
            in.close();
            out.close();

            //then close the socket
            client.close();

            System.out.println("Closing connections & channels - DONE.");
        }
    }
}
