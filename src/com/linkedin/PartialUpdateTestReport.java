package com.linkedin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PartialUpdateTestReport {
  public static void generateReport(String dirName) throws IOException {
    LogStorage logStorage = new LogStorage(dirName);

    String ttl = null;
    boolean isPartialUpdate = false;
    String timeBuffer;

    Test test = null;

    String line;

    StringBuilder sb = new StringBuilder();

    sb.append("ttl").append("\t");
    sb.append("isPartialUpdate").append("\t");
    sb.append("timeBuffer").append("\t");
    sb.append("totalRequests").append("\t");
    sb.append("outRequests").append("\t");
    sb.append("duplicates").append("\t");
    sb.append("totalLatency").append("\t");
    sb.append("totalQTime").append("\t");
    sb.append("totalTaskLatency").append("\t");
    sb.append("avgFdSize").append("\t");
    sb.append("avgSDSize").append("\t");
    sb.append("avgFDSaving").append("\t");
    sb.append("avgSDSaving").append("\t");
    sb.append("PartialUpdate").append("\t");

    System.out.println(sb.toString());
    while ((line = logStorage.readLine()) != null) {
      if (line.contains("MemCacheTTL changed: ")) {
        if (test != null) {
          test.wrap();
        }

        // New test starts
        ttl = line.substring(line.indexOf(Constants.NEW_VALUE) + Constants.NEW_VALUE.length()).trim();
      }

      if (line.contains("partialUpdateMode: ")) {
        isPartialUpdate = Boolean.parseBoolean(line.substring(line.indexOf(Constants.NEW_VALUE) + Constants.NEW_VALUE.length()).trim());
      }

      if (line.contains("TimeBuffer changed: ")) {
        timeBuffer = line.substring(line.indexOf(Constants.NEW_VALUE) + Constants.NEW_VALUE.length()).trim();
        test = new Test (ttl, isPartialUpdate, timeBuffer);
        // start new test
      }

      if(test != null) {
        test.process(line);
      }
    }
  }
}
