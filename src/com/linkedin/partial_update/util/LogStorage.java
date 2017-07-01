package com.linkedin.partial_update.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LogStorage {
  private File[] fileList;
  private int currentFile = 0;
  private boolean hasRead = false;
  private BufferedReader br = null;
  public final String path;

  public LogStorage(String path, FilenameFilter fileListFilter) {
    this.path = path;
    fileList = Utils.getWhiteListFileList(path, fileListFilter);
    Arrays.sort(fileList);
    System.out.println("[LogStorage] fileList.size: " + fileList.length);
    assert (fileList != null && fileList.length != 0);
    try {
      br = new BufferedReader(new FileReader(fileList[currentFile]));
    } catch (FileNotFoundException e) {
      System.out.println("Error during reading " + currentFile + "!!!");
    }
  }

  private boolean swapFile() throws IOException {
    if (currentFile++ == fileList.length - 1) {
      System.out.println("No more file!!!");
      return false;
    }

    br.close();
    System.out.println(currentFile + 1 + "/" + fileList.length + " file: " + fileList[currentFile].getName());
    br = new BufferedReader(new FileReader(fileList[currentFile]));
    return true;
  }

  public String readLine() throws IOException {
    if(!hasRead) {
      System.out.println(currentFile + 1 + "/" + fileList.length + " file: " + fileList[currentFile].getName());
    }
    String result = br.readLine();
    hasRead = true;
    if (result == null) {
      if (swapFile()) {
        result = readLine();
      }
    }
    return result;
  }

  public static List<LogStorage> createLogStorageList(String storagePath, FileListFilter fileListFilter) {
    File[] hostList = Utils.getFileList(storagePath);
    List<LogStorage> logStorageList = new ArrayList<>();

    for (File host : hostList) {
      logStorageList.add(new LogStorage(storagePath + "/" + host.getName(), fileListFilter));
    }

    return logStorageList;
  }

  public static Map<String, LogStorage> createLogStorageMap(String storagePath, FileListFilter fileListFilter) {
    File[] hostList = Utils.getDirectoryList(storagePath);
    Map<String, LogStorage> logStorageMap = new HashMap<>();

    for (File host : hostList) {
      String hostName = host.getName();
      logStorageMap.put(hostName, new LogStorage(storagePath + "/" + hostName, fileListFilter));
    }

    return logStorageMap;
  }
}
