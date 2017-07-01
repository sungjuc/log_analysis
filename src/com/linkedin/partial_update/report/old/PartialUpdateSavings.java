package com.linkedin.partial_update.report.old;

import com.linkedin.partial_update.common.Record;
import com.linkedin.partial_update.util.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PartialUpdateSavings {
  public static void computePartialUpdateSavings(String dirName) throws Exception {
    File[] listOfFiles = Utils.getFileList(dirName);

    List<Record> globalList = new ArrayList<Record>();

    for (File file : listOfFiles) {
      if (file.isFile()) {
        List<Record> records = Utils.getEvaluationFromFile(file);
        globalList.addAll(records);
        printSavingResults(records);
      }
    }
    System.out.println("----------------------------------------------------");
    System.out.println("Global Results!!!");
    System.out.println("----------------------------------------------------");
    printSavingResults(globalList);
  }

  private static void printSavingResults(List<Record> records) {
    int totalRequests = records.size();
    int totalPartialUpdate = 0;
    int totalNormalUpdate = 0;

    long totalFdSize = 0;
    long totalSdSize = 0;

    long totalPUFdSize = 0;
    long totalPUSdSize = 0;

    long totalFdSaving = 0;
    long totalSdSaving = 0;

    for (Record record : records) {
      totalFdSize += record.fd;
      totalSdSize += record.sd;
      if (record.isPartial) {
        totalPartialUpdate++;
        totalPUFdSize += record.fd;
        totalPUSdSize += record.sd;
        totalFdSaving += record.fd - record.d_fd;
        totalSdSaving += record.sd - record.d_sd;
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
