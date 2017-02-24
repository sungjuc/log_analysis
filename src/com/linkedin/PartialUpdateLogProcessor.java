package com.linkedin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PartialUpdateLogProcessor {

    public static void main(String[] args) throws IOException {
        computeNetworkSize(args[0]);

    }

    public static void computeNetworkSize(String dirName) throws IOException {
        System.out.println("Log_Process!!!");
        String workingDirectory = System.getProperty("user.dir");
        String dataDirectoryStr = workingDirectory.substring(0, workingDirectory.lastIndexOf("/") + 1) + dirName;
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

                        /*if (line.contains("PARTIAL")) {
                            processingLine = processingLine.substring(processingLine.indexOf(deltaSizeStartDelimeter) + deltaSizeStartDelimeter.length());
                            System.out.println(processingLine);
                        }*/

                        User user = globalMap.get(memberId);
                        if (user == null) {
                            user = new User();
                            globalMap.put(memberId, user);
                        }
                        user.fd = fd;
                        user.sd = sd;

                        User localUser = fileMap.get(memberId);
                        if (localUser == null) {
                            localUser = new User();
                            fileMap.put(memberId, localUser);
                        }
                        localUser.fd = fd;
                        localUser.sd = sd;
                    }
                }
                printResults(fileMap);
            }
        }
        System.out.println("----------------------------------------------------");
        System.out.println("Global Results!!!");
        System.out.println("----------------------------------------------------");
        printResults(globalMap);
    }

    private static void printResults(Map<Integer, User> userMap) {
        List<User> userList = new ArrayList<User>(userMap.values());

        int totalUniqueUsers = userList.size();
        if (totalUniqueUsers == 0)
            return;
        System.out.println("----------------------------------------------------");
        System.out.println("Total Daily Unique Member: " + totalUniqueUsers);
        long fdSum = 0;
        long sdSum = 0;
        long count = 0;
        for (User user: userList) {
            fdSum += user.fd;
            sdSum += user.sd;
            count++;
        }
        System.out.println("Total Daily Unique Member: " + count);
        System.out.println("Average Daily Unique Member's first degree size: " + fdSum/count);
        System.out.println("Average Daily Unique Member's second degree size: " + sdSum/count);
        System.out.println("----------------------------------------------------");
    }
}
