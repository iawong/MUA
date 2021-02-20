import java.io.*;
import java.net.*;

class TCPClient {
    public static void main(String[] args) throws Exception {
        String text;
        String HTTPRequest = "GET " + "/~siddiquf/" + " HTTP/1.1\n"+ "Host: " + "users.dickinson.edu" + "\n\n";

        // Socket to connect to web server
        Socket clientSocket = new Socket("users.dickinson.edu", 80);

        // Input and output to and from the server
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Send request out to web server
        outToServer.writeBytes(HTTPRequest);

        // Returns data from server
        while (true) {
            text = inFromServer.readLine();
            System.out.println("RECEIVED FROM SERVER: " + text);
        }
    }
}