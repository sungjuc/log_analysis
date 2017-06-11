package com.linkedin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PartialUpdateSavings {
  public static void computePartialUpdateSavings(String dirName) throws Exception {
    File[] listOfFiles = Utils.getFileList(dirName);

    List<User> globalList = new ArrayList<User>();

    for (File file : listOfFiles) {
      if (file.isFile()) {
        List<User> users = Utils.getUsersFromFile(file);
        globalList.addAll(users);
        printSavingResults(users);
      }
    }
    System.out.println("----------------------------------------------------");
    System.out.println("Global Results!!!");
    System.out.println("----------------------------------------------------");
    printSavingResults(globalList);
  }

  private static void printSavingResults(List<User> users) {
    int totalRequests = users.size();
    int totalPartialUpdate = 0;
    int totalNormalUpdate = 0;

    long totalFdSize = 0;
    long totalSdSize = 0;

    long totalPUFdSize = 0;
    long totalPUSdSize = 0;

    long totalFdSaving = 0;
    long totalSdSaving = 0;

    for (User user : users) {
      totalFdSize += user.fd;
      totalSdSize += user.sd;
      if (user.isPartial) {
        totalPartialUpdate++;
        totalPUFdSize += user.fd;
        totalPUSdSize += user.sd;
        totalFdSaving += user.fd - user.d_fd;
        totalSdSaving += user.sd - user.d_sd;
      } else {
        totalNormalUpdate++;
      }
    }

    System.out.println("Total Requests: " + totalRequests + " , Total Partial Updates: " + totalPartialUpdate
        + " , Total Naive Updates: " + totalNormalUpdate);

    System.out.println("Total fd size: " + totalFdSize + " , Total sd size: " + totalSdSize);
    System.out.println("Total fd savings: " + totalFdSaving + " , Total sd savings: " + totalSdSaving);
    System.out.println("fd savings rate:  " + (100 * totalFdSaving / totalPUFdSize) + " , Total sd savings rate: " + (
        100 * totalSdSaving / totalPUSdSize));
    System.out.println(
        "Total fd savings rate:  " + (100 * totalFdSaving / totalFdSize) + " , Total sd savings rate: " + (
            100 * totalSdSaving / totalSdSize));

    System.out.println("Rate: " + (100 * totalPartialUpdate / totalRequests));
  }
}
