package com.linkedin.partial_update.util;

import com.linkedin.partial_update.common.FileConstants;
import com.linkedin.partial_update.common.Record;
import com.linkedin.partial_update.common.LogConstants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class Utils {
  public static File[] getFileList(String dirName) {
    String dataDirectoryStr = FileConstants.DATA_REPOSITORY_PATH + dirName;
    System.out.println("[Utils.getFileList] Getting file list from: " + dataDirectoryStr);
    File dataDirectory = new File(dataDirectoryStr);
    return dataDirectory.listFiles();
  }

  public static File[] getWhiteListFileList(String dirName, FilenameFilter filter) {
    String dataDirectoryStr = FileConstants.DATA_REPOSITORY_PATH + dirName;
    System.out.println("[Utils.getFileList] Getting file list from: " + dataDirectoryStr);
    File dataDirectory = new File(dataDirectoryStr);
    return dataDirectory.listFiles(filter);
  }

  public static File[] getDirectoryList(String dirName) {
    String dataDirectoryStr = FileConstants.DATA_REPOSITORY_PATH + dirName;
    System.out.println("[Utils.getFileList] Getting file list from: " + dataDirectoryStr);
    File dataDirectory = new File(dataDirectoryStr);

    File[] results = dataDirectory.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.isDirectory();
      }
    });

    return results;
  }

  public static List<Record> getEvaluationFromFile(File file) throws IOException {
    List<Record> recordList = new ArrayList<Record>();

    System.out.println(file.getName());
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line;

    while ((line = br.readLine()) != null) {
      if (line.contains("GDB") && line.contains("Request") && line.contains("NCM") && line.contains("ASYNC")
          && !line.contains("Exception")) {
        Record record = processUser(line);
        if (record != null) {
          recordList.add(record);
        }
      }
    }
    return recordList;
  }

  public static Record processUser(String line) {
    String processingLine = line;
    // Member ID
    processingLine =
        processingLine.substring(processingLine.indexOf(LogConstants.MEMBER_START) + LogConstants.MEMBER_START.length(),
            processingLine.length());
    int memberId = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(LogConstants.MEMBER_END)));
    processingLine =
        processingLine.substring(processingLine.indexOf(LogConstants.SIZE_START) + LogConstants.SIZE_START.length());

    int fd = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(LogConstants.SIZE_MID)));
    int sd = 0;
    if (fd > 0) {
      processingLine =
          processingLine.substring(processingLine.indexOf(LogConstants.SIZE_MID) + LogConstants.SIZE_END.length());
      try {
        sd = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(LogConstants.SIZE_END)));
      } catch (Exception e) {
        System.out.println(e);
        System.out.println(line);
        return null;
      }
    } else {
      return null;
    }

    int dfd = 0;
    int dsd = 0;

    boolean isPartialUpdate = false;
    if (line.contains("PARTIAL")) {
      isPartialUpdate = true;
      processingLine = processingLine.substring(
          processingLine.indexOf(LogConstants.DELTA_SIZE_START) + LogConstants.DELTA_SIZE_START.length());
      dfd = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(LogConstants.DELTA_SIZE_MID)));
      processingLine = processingLine.substring(
          processingLine.indexOf(LogConstants.DELTA_SIZE_MID) + LogConstants.DELTA_SIZE_MID.length());
      dsd = Integer.parseInt(processingLine.substring(1, processingLine.indexOf(LogConstants.DELTA_SIZE_END)));
    }

    int latency = 0;
    int qTime = 0;
    int tqTime = 0;
    int taskLatency = 0;

    processingLine = processingLine.substring(processingLine.indexOf(LogConstants.LATENCY) + LogConstants.LATENCY.length());
    latency = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(LogConstants.QTIME)));
    processingLine = processingLine.substring(processingLine.indexOf(LogConstants.QTIME) + LogConstants.QTIME.length());
    qTime = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(LogConstants.TQTIME)));
    processingLine = processingLine.substring(processingLine.indexOf(LogConstants.TQTIME) + LogConstants.TQTIME.length());
    tqTime = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(LogConstants.TASK_LATENCY)));
    processingLine =
        processingLine.substring(processingLine.indexOf(LogConstants.TASK_LATENCY) + LogConstants.TASK_LATENCY.length());
    taskLatency = Integer.parseInt(processingLine.substring(0, processingLine.indexOf(LogConstants.LATENCY_END)));

    return new Record(memberId, isPartialUpdate, fd, sd, dfd, dsd, latency, qTime, tqTime, taskLatency);
  }

  public static void print(String data, File file) throws IOException {
    FileOutputStream fos = null;
    BufferedWriter bw = null;

    try {
      fos = new FileOutputStream(file);
      bw = new BufferedWriter(new OutputStreamWriter(fos));
      bw.write(data);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (bw != null) {
        bw.flush();
        bw.close();
      }
      if (fos != null) {
        fos.close();
      }
    }
  }
}

