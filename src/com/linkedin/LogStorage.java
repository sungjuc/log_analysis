package com.linkedin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class LogStorage {
  private File[] fileList;
  private int currentFile = 0;
  private BufferedReader br = null;

  public LogStorage(String dirName) {
    fileList = Utils.getFileList(dirName);
    assert(fileList != null && fileList.length != 0);
    try {
      System.out.println(currentFile + 1 + "/" + fileList.length + " file: " + fileList[currentFile].getName());
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
    String result = br.readLine();
    if (result == null) {
      if (swapFile()) {
        result = readLine();
      }
    }
    return result;
  }
}
