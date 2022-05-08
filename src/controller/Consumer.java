package controller;

import model.MultimediaFile;
import model.Value;

import java.io.*;
import java.net.Socket;

import static utils.socketMethods.closeEverything;

public class Consumer{
    private BufferedReader bufferedReader;
    private Socket socket;
    Consumer(Socket socket) throws IOException {
        this.socket = socket;
        try{
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioException) {
            System.out.println("There was a problem in the connection of the client");
            closeEverything(socket, bufferedReader);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader);
                    }
                }
            }
        }).start();
    }


    public String listenForMessageOneTime() throws IOException {
        String msg;
        msg = bufferedReader.readLine();
        return msg;
    }

    public void printListenForMessageOneTime() throws IOException {
      System.out.println(listenForMessageOneTime());
    }

    public final static String
            FILE_TO_RECEIVED = MultimediaFile.FOLDER_SAVE + "ConsumerFile\\" + "new_download.jpeg";
    public final static int FILE_SIZE = 6022386;
    public void listenForImages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket.isConnected()) {

                    int bytesRead;
                    int current = 0;
                    FileOutputStream fos = null;
                    BufferedOutputStream bos = null;
                    System.out.println("Connecting...");
                    // receive file
                    byte[] mybytearray = new byte[FILE_SIZE];
                    InputStream is = null;
                    try {
                        is = socket.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        fos = new FileOutputStream(FILE_TO_RECEIVED);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    bos = new BufferedOutputStream(fos);
                    try {
                        bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    current = bytesRead;
                    do {
                        try {
                            bytesRead =
                                    is.read(mybytearray, current, (mybytearray.length - current));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (bytesRead >= 0) current += bytesRead;
                    } while (bytesRead > -1);

                    try {
                        bos.write(mybytearray, 0, current);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        bos.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(current);
                    System.out.println("File " + FILE_TO_RECEIVED
                            + " downloaded (" + current + " bytes read)");

                }
            }

        }).start();
    }
}
