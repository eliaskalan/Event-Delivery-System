package controller;

import model.MultimediaFile;
import model.Value;
import utils.Config;
import java.lang.*;
import java.io.*;
import java.net.Socket;
import java.security.DigestInputStream;

import static utils.socketMethods.closeEverything;

public class Consumer{
    private BufferedReader bufferedReader;
//    private ObjectInputStream objectInputStream = null;
    private Socket socket;
    private InputStream inputStream;
    Consumer(Socket socket) throws IOException {
        this.socket = socket;
        try{
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioException) {
            System.out.println("There was a problem in the connection of the client");
            closeEverything(socket, bufferedReader);
        }
    }

    public void listenForVideo(){
        System.out.println("listendForVideo()");
        new Thread(new Runnable() {
            @Override
            public void run() {

                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        /*msgFromGroupChat = (String) objectInputStream.readObject();
                        System.out.println(msgFromGroupChat);*/
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
//                        closeEverything(socket, objectInputStream);
                        closeEverything(socket, bufferedReader);
                    }
                }
            }
        }).start();
    }


    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        // @TODO prepei na stelnw prota to idos minimatos(photo/vid h aplo minima)

                        // aplo minima: 2
                        System.out.println("Consumer - listenForMessage()");
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println("eidos minimaos sthn listen: " + msgFromGroupChat);


                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println("minima xrhsth: " + msgFromGroupChat);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                        // photo/vid: 1
                        /*
                        try {

                            String name = bufferedReader.readLine();
                            File file = new File("C:\\Users\\user\\OneDrive - aueb.gr\\Desktop\\elpizw\\foto.jpg\\", name);

                            String size = bufferedReader.readLine();
                            int fileSize;
                            System.out.println("SIZE------------------------------: " + size);
                            try {
                                fileSize = Integer.parseInt(size);
                            } catch (NumberFormatException e) {
                                System.err.println("Error: Malformed file size:" + size);
                                e.printStackTrace();
                                return;
                            }

                            System.out.println("Saving " + file + " from user... (" + fileSize + " bytes)");
                            saveFile(file, socket.getInputStream());
                            System.out.println("Finished downloading " + file + " from user.");
                            if (file.length() != fileSize) {
                                System.err.println("Error: file incomplete");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (socket != null) {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        // end for video
                        */
                }
            }
        }).start();
    }

    private void saveFile(File file, InputStream inStream) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(file);

            byte[] buffer = new byte[Config.CHUNK_SIZE];
            int bytesRead;
            int pos = 0;
            while ((bytesRead = inStream.read(buffer, 0, Config.CHUNK_SIZE)) >= 0) {
                pos += bytesRead;
                System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
                fileOut.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Finished, filesize = " + file.length());
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
    public void listenForImage() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inputStream = socket.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String message_type = null;
                try {
                    message_type = bufferedReader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                int bytesRead;
                int current = 0;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                byte[] mybytearray = new byte[FILE_SIZE];

                try {
                    fos = new FileOutputStream(FILE_TO_RECEIVED);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                bos = new BufferedOutputStream(fos);
                try {
                    bytesRead = inputStream.read(mybytearray, 0, mybytearray.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                current = bytesRead;

                do {
                    try {
                        bytesRead = inputStream.read(mybytearray, current, (mybytearray.length - current));

                        if (bytesRead >= 0) {
                            current += bytesRead;
                        }
                    } catch (IOException e) {
                        bytesRead = -1;
                    }
                } while (bytesRead > -1);

                try {
                    bos.write(mybytearray, 0, current);
                    bos.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(current);
                System.out.println("File " + FILE_TO_RECEIVED
                        + " downloaded (" + current + " bytes read)");
            }
        }).start();

    }

    /*public void listenForVideo(String [] files) throws InterruptedException, IOException {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    int numOfFiles = 0;
                    try {
                        numOfFiles = dataInputStream.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (String file : files) {
                        try {
                            receiveChunk(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    String path_of_transferred_chunks = "C:\\Users\\user\\OneDrive - aueb.gr\\Desktop\\transferredChunks\\";
                    String path_of_joined_file = "C:\\Users\\user\\OneDrive - aueb.gr\\Desktop\\joinFinalFile\\";
                    MultimediaFile mf = new MultimediaFile();
                    mf.JoinVideo("myVid.mkv", path_of_transferred_chunks, path_of_joined_file);
                }
            }
        }).start();
    }*/

    /*private void receiveChunk(String fileName) throws Exception{
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        long size = dataInputStream.readLong();     // read file size
        System.out.println("size: " + size);
        byte[] buffer = new byte[(int)size];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }
        //        fileOutputStream.close();
    }*/
}