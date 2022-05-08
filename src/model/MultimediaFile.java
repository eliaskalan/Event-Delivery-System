package model;
import java.io.*;
import java.util.Arrays;
public class MultimediaFile {
    String multimediaFileName;
    String profileName;
    String dateCreated;
    String length;
    String framerate;
    String frameWidth;
    String frameHeight;
    byte[] multimediaFileChunk;
    public static final String FOLDER_SAVE = "C:\\Users\\elias\\Documents\\";
    public static final String SAVED_FILES_PATH = "C:/Users/user/Videos/vidsForProject/";
    public static final String PATH_OFF_ORIGINAL_FILE = "C:/Users/user/Videos/vidsForProject/";

    public void MultimediaFile()
    {
        System.out.println(SAVED_FILES_PATH+"Despicable Me 2 - Trailer (HD) - YouTube.mp4");
    }
    public void SplitFile(String fileName, String DESTINATION_FOLDER_FOR_CHUNKS) {
        try {
            File file = new File(PATH_OFF_ORIGINAL_FILE + fileName);//File read from Source folder to Split.
            if (file.exists()) {

                //String videoFileName = file.getName().substring(0, file.getName().lastIndexOf(".")); // Name of the videoFile without extension
                // "C:/Users/user/Videos/vidsForProject/" + "Videos_Split/" + "myVid.mkv"
                File splitFile = new File(DESTINATION_FOLDER_FOR_CHUNKS);//Destination folder to save.
                if (!splitFile.exists()) {
                    splitFile.mkdirs();
                    System.out.println("Directory Created -> " + splitFile.getAbsolutePath());
                }
                int i = 01;// Files count starts from 1
                InputStream inputStream = new FileInputStream(file);
                String videoFile = splitFile.getAbsolutePath() + "/" + String.format("%02d", i) + "_" + file.getName();// Location to save the files which are Split from the original file.
                OutputStream outputStream = new FileOutputStream(videoFile);
                System.out.println("File Created Location: " + videoFile);
                System.out.println(inputStream.available());
                int totalPartsToSplit = 20;// Total files to split.
                int splitSize = 512000;
                int streamSize = 0;
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    if (splitSize == streamSize) {
                        if (i != totalPartsToSplit) {
                            i++;
                            String fileCount = String.format("%02d", i); // output will be 1 is 01, 2 is 02
                            videoFile = splitFile.getAbsolutePath() + "/" + fileCount + "_" + file.getName();
                            outputStream = new FileOutputStream(videoFile);
                            System.out.println("File Created Location: " + videoFile);
                            streamSize = 0;
                        }
                    }
                    outputStream.write(read);
                    streamSize++;
                }
                inputStream.close();
                outputStream.close();
                System.out.println("Total files Split ->" + totalPartsToSplit);
            } else {
                System.err.println(file.getAbsolutePath() + " File Not Found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String SPLITTED_FILES_PATH = "C:\\Users\\user\\Documents\\DS-Project\\distributed-systems\\out\\production\\distributed-systems\\transferredFiles\\";
    public void JoinVideo(String fileName, String PATH_OF_CHUNKS, String PUTH_OF_JOINED_FILE) {
        try {
            File splitFiles = new File(PATH_OF_CHUNKS);// get all files which are to be join
            if (splitFiles.exists()) {
                File[] files = splitFiles.getAbsoluteFile().listFiles();
                if (files.length != 0) {
                    System.out.println("Total files to be join: "+ files.length);
                    String joinFileName = Arrays.asList(files).get(0).getName();
                    System.out.println("Join file created with name -> "+ joinFileName);

                    //String fileName = joinFileName.substring(0, joinFileName.lastIndexOf("."));// video fileName without extension
                    File fileJoinPath = new File(PUTH_OF_JOINED_FILE);// merge video files saved in this location

                    if (!fileJoinPath.exists()) {
                        fileJoinPath.mkdirs();
                        System.out.println("Created Directory -> "+ fileJoinPath.getAbsolutePath());
                    }
                    OutputStream outputStream = new FileOutputStream(fileJoinPath.getAbsolutePath() +"/"+ joinFileName);
                    for (File file : files) {
                        System.out.println("Reading the file -> "+ file.getName());
                        InputStream inputStream = new FileInputStream(file);
                        int readByte = 0;
                        while((readByte = inputStream.read()) != -1) {
                            outputStream.write(readByte);
                        }
                        inputStream.close();
                    }
                    System.out.println("Join file saved at -> "+ fileJoinPath.getAbsolutePath() +"/"+ joinFileName);
                    outputStream.close();
                } else {
                    System.err.println("No Files exist in path -> "+ splitFiles.getAbsolutePath());
                }
            } else {
                System.err.println("This path doesn't exist -> "+ splitFiles.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}