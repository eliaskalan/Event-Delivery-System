package controller;
import java.io.*;
import java.net.*;
import java.util.List;

public class Node{
    public ServerSocket socket;
    Socket connection = null;
    public List<Broker> brokers;
    private int port;
    private String ip;
    Node(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    public void connect(String customMessage) {
        try {
            socket = new ServerSocket(port, 10);
            System.out.println(customMessage);
            while (true) {
                System.out.println("A new connection");
                Socket connection = socket.accept();
                System.out.println(connection);
                ClientHandler clientSock = new ClientHandler(connection);
                new Thread(clientSock).start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
                disconnect();
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public int getPort(){
        return this.port;
    }

    public String getIp(){
        return this.ip;
    }


    public void updateNodes() {
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            try {

                // get the outputstream of client
                out = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                // get the inputstream of client
                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {

                    // writing the received message from
                    // client
                    System.out.printf(
                            " Sent from the client: %s\n",
                            line);
                    out.println(line);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}