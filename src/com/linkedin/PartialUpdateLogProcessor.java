package com.linkedin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PartialUpdateLogProcessor {

    public static void main(String[] args) throws IOException {
        System.out.println("Log_Process!!!");
        String workingDirectory = System.getProperty("user.dir");
        String dataDirectoryStr = workingDirectory.substring(0, workingDirectory.lastIndexOf("/") + 1) + args[0];
        System.out.println(dataDirectoryStr);

        File dataDirectory = new File(dataDirectoryStr);

        File[] listOfFiles = dataDirectory.listFiles();

        String memberStartDelimeter = "member = ";
        String memberEndDelimeter = ":";

        String sizeStartDelimeter = "size = (";
        String sizeMidDelimeter = ",";
        String sizeEndDelimeter = "),";

        String deltaSizeStartDelimeter = "deltaSize = (";

        Map<Integer, User> globalMap = new HashMap<Integer, User>();

        for (File file: listOfFiles) {
            if (file.isFile()) {
                Map<Integer, User> fileMap = new HashMap<Integer, User>();

                System.out.println(file.getName());
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line;

                while((line = br.readLine()) != null) {
                    if (line.contains("GDB") && line.contains("Request") && !line.contains("Exception")) {
                        String processingLine = line;
                        // Member ID
                        processingLine = processingLine.substring(processingLine.indexOf(memberStartDelimeter) + memberStartDelimeter.length(), processingLine.length());
                        int memberId = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(memberEndDelimeter)));
                        //System.out.println(processingLine);
                        processingLine = processingLine.substring(processingLine.indexOf(sizeStartDelimeter) + sizeStartDelimeter.length());

                        int fd = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(sizeMidDelimeter)));
                        int sd = 0;
                        if (fd > 0) {
                            processingLine = processingLine.substring(processingLine.indexOf(sizeMidDelimeter) + sizeEndDelimeter.length());
                            try {
                                sd = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(sizeEndDelimeter)));
                            } catch (Exception e) {
                                System.out.println("XXXXX");
                                System.out.println(line);
                                continue;
                            }
                        }

                        if (line.contains("PARTIAL")) {
                            processingLine.substring(processingLine.indexOf(sizeEndDelimeter) + sizeEndDelimeter.length());
                            System.out.println(processingLine);
                        }

                    }
                }
            }
        }
    }
}
