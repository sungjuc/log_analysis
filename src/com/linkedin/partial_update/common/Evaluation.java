package com.linkedin.partial_update.common;

import com.linkedin.partial_update.util.Utils;
import java.util.ArrayList;
import java.util.List;


public abstract class Evaluation {
  public int ttl = -1;
  public boolean isPartialUpdate = false;
  public int timeBuffer = -1;
  public boolean isDone = false;

  public int requests = 0;
  public int duplicates = 0;
  public int exceptions = 0;
  public int outRequests = 0;

  public int partialUpdates = 0;
  public int naiveUpdates = 0;

  public long fdSize = 0;
  public long sdSize = 0;

  public long nFdSize = 0;
  public long nSdSize = 0;

  public long pFdSize = 0;
  public long pSdSize = 0;

  public long pDFdSize = 0;
  public long pDSdSize = 0;

  public long nLatency = 0;
  public long nQTime = 0;
  public long nTqTime = 0;
  public long nTaskLatency = 0;

  public long pLatency = 0;
  public long pQTime = 0;
  public long pTqTime = 0;
  public long pTaskLatency = 0;

  public int noFdChanges = 0;

  public List<Record> _recordList = new ArrayList<Record>();

  protected Evaluation(String ttl, boolean isPartialUpdate, String timeBuffer) {
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

  public void process(String line) {
    if (line.contains("Find duplicate worker for ")) {
      duplicates++;
      return;
    }

    if (line.contains("GDB") && line.contains("Request") && line.contains("NCM") && line.contains("ASYNC")) {
      requests++;
      if (line.contains("Exception")) {
        exceptions++;
        return;
      }
      Record record = Utils.processUser(line);
      if (satisfy(record)) {
        _recordList.add(record);
        //histogram.process(record);
        outRequests++;
        fdSize += record.fd;
        sdSize += record.sd;
        if (record.isPartial) {
          partialUpdates++;
          if (record.d_fd == 0) {
            noFdChanges++;
          }
          pFdSize += record.fd;
          pSdSize += record.sd;
          pDFdSize += record.d_fd;
          pDSdSize += record.d_sd;

          pQTime += record.qTime;
          pTqTime += record.tqTime;
          pLatency += record.latency;
          pTaskLatency += record.taskLatency;
        } else {
          naiveUpdates++;
          nFdSize += record.fd;
          nSdSize += record.sd;

          nQTime += record.qTime;
          nTqTime += record.tqTime;
          nLatency += record.latency;
          nTaskLatency += record.taskLatency;
        }
      }
    }
  }

  public abstract boolean satisfy(Record record);
}
