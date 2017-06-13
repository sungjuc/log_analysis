package com.linkedin;

import java.util.ArrayList;
import java.util.List;


public class Test {
  private int ttl = -1;
  private boolean isPartialUpdate = false;
  private int timeBuffer = -1;
  private boolean isDone = false;

  public Test(String ttl, boolean isPartialUpdate, String timeBuffer) {
    int unit = 1;
    String unitString = "s";
    if (ttl.contains("m")) {
      unit = 60;
      unitString = "m";
    }

    ttl = ttl.substring(0, ttl.indexOf(unitString));
    this.ttl = Integer.parseInt(ttl) * unit;

    this.isPartialUpdate = isPartialUpdate;
    unit = 1;
    unitString = "s";
    if (timeBuffer.contains("m")) {
      unit = 60;
      unitString = "m";
    }
    timeBuffer = timeBuffer.substring(0, timeBuffer.indexOf(unitString));
    this.timeBuffer = Integer.parseInt(timeBuffer) * unit;
  }

  private int totalRequests = 0;
  private long totalLatency = 0;
  private long totalQTime = 0;
  private long totalTaskLatency = 0;
  private int duplicates = 0;
  private long totalFDSize = 0;
  private long totalSDSize = 0;
  private long totalFDSaving = 0;
  private long totalSDSaving = 0;
  private int totalSuccessRequests =0;
  private int totalPartialUpdates = 0;
  private int noFDChange = 0;
  private long totalDFDSize = 0;
  private long totalDSDSize = 0;

  private List<User> userList = new ArrayList<User>();

  public void wrap() {
    if (!isDone && !userList.isEmpty()) {
      StringBuilder sb = new StringBuilder();

      sb.append(ttl).append("\t");
      sb.append(isPartialUpdate).append("\t");
      sb.append(timeBuffer).append("\t");
      sb.append(totalSuccessRequests + duplicates).append("\t");
      sb.append(totalSuccessRequests).append("\t");
      sb.append(duplicates).append("\t");
      sb.append(totalLatency).append("\t");
      sb.append(totalQTime).append("\t");
      sb.append(totalTaskLatency).append("\t");
      sb.append(totalFDSize/totalSuccessRequests).append("\t");
      sb.append(totalSDSize/totalSuccessRequests).append("\t");
      if(isPartialUpdate) {
        sb.append((float)totalDFDSize/totalPartialUpdates).append("\t");
        sb.append((float)totalDSDSize/totalPartialUpdates).append("\t");
        sb.append((float)totalFDSaving / totalPartialUpdates).append("\t");
        sb.append((float)totalSDSaving / totalPartialUpdates).append("\t");
      } else {
        sb.append(0).append("\t");
        sb.append(0).append("\t");
        sb.append(0).append("\t");
        sb.append(0).append("\t");
      }

      sb.append(totalPartialUpdates).append("\t");
      sb.append(noFDChange).append("\t");


      System.out.println(sb.toString());
    }
    isDone = true;
  }

  public void process(String line) {
    if (line.contains("Find duplicate worker for ")) {
      duplicates++;
    }

    if (line.contains("GDB") && line.contains("Request") && line.contains("NCM") && line.contains("ASYNC") && !line.contains("Exception")) {
      User user = Utils.processUser(line);
      if(user != null) {
        userList.add(user);
        totalLatency += user.latency;
        totalQTime += user.qTime + user.tqTime;
        totalTaskLatency += user.taskLatency;
        totalFDSize += user.fd;
        totalSDSize += user.sd;
        if(user.isPartial) {
          if (user.d_fd == 0) {
            noFDChange++;
          }
          totalDFDSize += user.fd;
          totalDSDSize += user.sd;
          totalFDSaving += user.d_fd;
          totalSDSaving += user.d_sd;
          totalPartialUpdates++;
        }
        totalSuccessRequests++;
      }
      totalRequests++;
    }
  }
}
