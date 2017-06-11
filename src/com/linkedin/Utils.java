package com.linkedin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Utils {

  public static File[] getFileList(String dirName) {
    System.out.println("Log_Process!!!");
    String workingDirectory = System.getProperty("user.dir");
    String dataDirectoryStr = workingDirectory.substring(0, workingDirectory.lastIndexOf("/") + 1) + dirName;
    System.out.println(dataDirectoryStr);

    File dataDirectory = new File(dataDirectoryStr);
    return dataDirectory.listFiles();
  }


  public static List<User> getUsersFromFile(File file) throws IOException {
    List<User> results = new ArrayList<User>();

    System.out.println(file.getName());
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line;

    while((line = br.readLine()) != null) {
      if (line.contains("GDB") && line.contains("Request") && !line.contains("Exception")) {
        String processingLine = line;
        // Member ID
        processingLine = processingLine.substring(processingLine.indexOf(Constants.MEMBER_START) + Constants.MEMBER_START.length(), processingLine.length());
        int memberId = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(Constants.MEMBER_END)));
        processingLine = processingLine.substring(processingLine.indexOf(Constants.SIZE_START) + Constants.SIZE_START.length());

        int fd = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(Constants.SIZE_MID)));
        int sd = 0;
        if (fd > 0) {
          processingLine = processingLine.substring(processingLine.indexOf(Constants.SIZE_MID) + Constants.SIZE_END.length());
          try {
            sd = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(Constants.SIZE_END)));
          } catch (Exception e) {
            System.out.println(e);
            System.out.println(line);
            continue;
          }
        } else {
          continue;
        }

        int dfd = 0;
        int dsd = 0;

        boolean isPartialUpdate = false;
        if (line.contains("PARTIAL")) {
          isPartialUpdate = true;
          processingLine = processingLine.substring(processingLine.indexOf(Constants.DELTA_SIZE_START) + Constants.DELTA_SIZE_START.length());
          dfd = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(Constants.DELTA_SIZE_MID)));
          processingLine = processingLine.substring(processingLine.indexOf(Constants.DELTA_SIZE_MID) + Constants.DELTA_SIZE_MID.length());
          dsd = Integer.parseInt(processingLine.substring(1, processingLine.indexOf(Constants.DELTA_SIZE_END)));
        }
        results.add(new User(memberId, isPartialUpdate, fd, sd, dfd, dsd));
      }
    }
    return results;
  }
}
