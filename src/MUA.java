import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Base64;
import java.util.StringTokenizer;

public class MUA {
    /**
     * Parse the reply line from the server. Returns the reply code.
     *
     * @param reply
     * @return
     */
    private static int parseReply(String reply) {
        StringTokenizer tokens = new StringTokenizer(reply," ");
        String rc = tokens.nextToken();
        return Integer.parseInt(rc);
    }

    /**
     * Asks user for the necessary information to send an email
     * Opens the socket and takes input from user while reading output from server
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // Mail server
        String mailServer = "exmail.dickinson.edu";

        // Stored information to send an email
        String username, password, sender, recipient, subject, body;

        // Read information from the user
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // Input information
        System.out.print("Enter username: ");
        username = inFromUser.readLine();
        System.out.print("Enter password: ");
        password = inFromUser.readLine();
        System.out.print("Enter sender email: ");
        sender = inFromUser.readLine();
        System.out.print("Enter recipient email: ");
        recipient = inFromUser.readLine();
        System.out.print("Enter subject of email: ");
        subject = inFromUser.readLine();
        System.out.print("Enter body of email: ");
        body = inFromUser.readLine();

        // Create a socket and connect to the mail server
        Socket clientSocket = new Socket(mailServer, 778);

        // Read information that comes in from the server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Send information out to the server
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        // Read a line from server and check that the reply code is 220. If not, throw an IOException
        String text = inFromServer.readLine();
        System.out.println(text);
        if (parseReply(text) != 220)
            throw new IOException("Reply code not 220");

        // SMTP handshake
        String hello = "HELO x\r\n";
        outToServer.writeBytes(hello);
        text = inFromServer.readLine();
        System.out.println(text);

        // Send AUTH LOGIN command
        String login = "auth login \r\n";
        outToServer.writeBytes(login);
        text = inFromServer.readLine();
        System.out.println(text);

        // Send username
        outToServer.writeBytes(Base64.getEncoder().encodeToString(username.getBytes()) + "\r\n");
        text = inFromServer.readLine();
        System.out.println(text);

        // Send password
        outToServer.writeBytes(Base64.getEncoder().encodeToString(password.getBytes()) + "\r\n");
        text = inFromServer.readLine();
        System.out.println(text);

        // Send MAIL FROM command
        String from = "mail from: " + sender + "\r\n";
        outToServer.writeBytes(from);
        text = inFromServer.readLine();
        System.out.println(text);

        // Send RCPT TO command
        String to = "rcpt to: " + recipient + "\r\n";
        outToServer.writeBytes(to);
        text = inFromServer.readLine();
        System.out.println(text);

        // Send DATA command
        String data = "data \r\n";
        outToServer.writeBytes(data);
        text = inFromServer.readLine();
        System.out.println(text);

        // Send subject data
        String sub = "subject: ";
        outToServer.writeBytes(sub + subject + "\r\n" + "\r\n");

        // Send message data
        outToServer.writeBytes(body + "\r\n");

        // End with period
        outToServer.writeBytes(".\r\n");
        text = inFromServer.readLine();
        System.out.println(text);

        // Send QUIT command
        String quit = "quit \r\n";
        outToServer.writeBytes(quit);
        text = inFromServer.readLine();
        System.out.println(text);
    }
}