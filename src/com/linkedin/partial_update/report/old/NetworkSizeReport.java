package com.linkedin.partial_update.report.old;

import com.linkedin.partial_update.common.Record;
import com.linkedin.partial_update.util.Utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class NetworkSizeReport {
  public static void computeNetworkSizeHistogram(String dirName) throws Exception {
    File[] listOfFiles = Utils.getFileList(dirName);

    Map<Integer, Record> globalMap = new HashMap<Integer, Record>();

    for (File file : listOfFiles) {
      if (file.isFile()) {
        List<Record> records = Utils.getEvaluationFromFile(file);
        for (Record record : records) {
          globalMap.put(record.id, record);
        }
      }
    }
    System.out.println("----------------------------------------------------");
    System.out.println("Global Results!!!");
    System.out.println("----------------------------------------------------");
    printNetworkSizeHistogram(globalMap);
    printNetworkSizeBucketHistogram(globalMap);
  }

  public static void computeNetworkSize(String dirName) throws IOException {
    File[] listOfFiles = Utils.getFileList(dirName);

    Map<Integer, Record> globalMap = new HashMap<Integer, Record>();

    for (File file : listOfFiles) {
      if (file.isFile()) {
        List<Record> records = Utils.getEvaluationFromFile(file);
        Map<Integer, Record> fileMap = new HashMap<Integer, Record>();

        for (Record record : records) {
          globalMap.put(record.id, record);
          fileMap.put(record.id, record);
        }
        printNetworkSizeResults(fileMap);
      }
    }
    System.out.println("----------------------------------------------------");
    System.out.println("Global Results!!!");
    System.out.println("----------------------------------------------------");
    printNetworkSizeResults(globalMap);
  }

  private static void printNetworkSizeResults(Map<Integer, Record> userMap) {
    List<Record> recordList = new ArrayList<Record>(userMap.values());

    int totalUniqueUsers = recordList.size();
    if (totalUniqueUsers == 0) {
      return;
    }
    System.out.println("----------------------------------------------------");
    System.out.println("Total Daily Unique Member: " + totalUniqueUsers);
    long fdSum = 0;
    long sdSum = 0;
    for (Record record : recordList) {
      fdSum += record.fd;
      sdSum += record.sd;
    }
    System.out.println("Average Daily Unique Member's first degree size: " + fdSum / totalUniqueUsers);
    System.out.println("Average Daily Unique Member's second degree size: " + sdSum / totalUniqueUsers);
    System.out.println("----------------------------------------------------");
  }

  private static void printNetworkSizeHistogram(Map<Integer, Record> userMap) throws IOException {
    Map<Integer, Integer> fdHisto = new TreeMap<Integer, Integer>();
    Map<Integer, Integer> sdHisto = new TreeMap<Integer, Integer>();
    for (Map.Entry<Integer, Record> entry : userMap.entrySet()) {
      int fd = entry.getValue().fd;
      int sd = entry.getValue().sd;
      Integer fdCount = fdHisto.get(fd);
      if (fdCount == null) {
        fdHisto.put(fd, 1);
      } else {
        fdHisto.put(fd, fdCount + 1);
      }
      Integer sdCount = sdHisto.get(sd);
      if (sdCount == null) {
        sdHisto.put(sd, 1);
      } else {
        sdHisto.put(sd, sdCount + 1);
      }
    }

    String fdHeader = "fd_size   count";
    printHistotoFile(fdHisto, fdHeader, "active-first-degree-histogram");

    String sdHeader = "sd_size   count";
    printHistotoFile(sdHisto, sdHeader, "active-second-degree-histogram");
  }

  private static void printNetworkSizeBucketHistogram(Map<Integer, Record> userMap) throws Exception {
    Map<Integer, Integer> fdHisto = new TreeMap<Integer, Integer>();
    Map<Integer, Integer> sdHisto = new TreeMap<Integer, Integer>();
    for (Map.Entry<Integer, Record> entry : userMap.entrySet()) {
      int fd = entry.getValue().fd;
      int sd = entry.getValue().sd;

      if (fd < 0 || sd < 0) {
        throw new Exception("First and second degree should not be a negative value.");
      }

      fd = (int) Math.floor(Math.log10(fd));

      if (sd > 0) {
        sd = (int) Math.floor(Math.log10(sd));
      }
      Integer fdCount = fdHisto.get(fd);
      if (fdCount == null) {
        fdHisto.put(fd, 1);
      } else {
        fdHisto.put(fd, fdCount + 1);
      }
      Integer sdCount = sdHisto.get(sd);
      if (sdCount == null) {
        sdHisto.put(sd, 1);
      } else {
        sdHisto.put(sd, sdCount + 1);
      }
    }

    String fdHeader = "fd_size   count";
    printHistotoFile(fdHisto, fdHeader, "active-first-degree-histogram-bucket");

    String sdHeader = "sd_size   count";
    printHistotoFile(sdHisto, sdHeader, "active-second-degree-histogram-bucket");
  }

  private static void printHistotoFile(Map<Integer, Integer> histo, String header, String fileName) throws IOException {
    File fdFile = new File(fileName);
    FileOutputStream fos = new FileOutputStream(fdFile);

    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    bw.write(header);
    bw.newLine();
    for (Map.Entry<Integer, Integer> entry : histo.entrySet()) {
      bw.write(entry.getKey() + "  " + entry.getValue());
      bw.newLine();
    }

    bw.flush();
    bw.close();
    fos.close();
  }
}
