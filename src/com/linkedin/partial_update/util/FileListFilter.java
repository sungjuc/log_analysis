package com.linkedin.partial_update.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;


public class FileListFilter implements FilenameFilter {
  private final String[] whiteList;

  public FileListFilter(String[] whiteList) {
    this.whiteList = whiteList;
  }

  @Override
  public boolean accept(File dir, String name) {
    System.out.println("[FileListFilter] dir: " + dir + "\tname: " + name);
    for (String white: whiteList) {
      if (name.contains(white)) {
        return true;
      }
    }
    return false;
  }

  public String toString() {
    return Arrays.toString(whiteList);
  }
}
