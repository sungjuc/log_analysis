package com.linkedin.partial_update.util;

import com.linkedin.partial_update.common.Record;
import java.util.Map;
import java.util.TreeMap;


public class Histogram {

  public Map<Integer, Report> normalHist = new TreeMap<Integer, Report>();
  public Map<Integer, Report> log10Hist = new TreeMap<Integer, Report>();
  public Map<Integer, Report> log2Hist = new TreeMap<Integer, Report>();


  public void process(Record record) {
    int fdSize = record.fd;

    Report normalReport = normalHist.get(fdSize);
    if (normalReport == null) {
      normalReport = new Report(fdSize);
      normalHist.put(fdSize, normalReport);
    }

    normalReport.update(record);

    int fdLog = (int) Math.log10(fdSize);
    Report logReport = log10Hist.get(fdLog);
    if (logReport == null) {
      logReport = new Report(fdLog);
      log10Hist.put(fdLog, logReport);
    }

    logReport.update(record);

    int fd2Log = (int) Math.log(fdSize);
    Report log2Report = log2Hist.get(fd2Log);
    if (log2Report == null) {
      log2Report = new Report(fd2Log);
      log2Hist.put(fd2Log, log2Report);
    }

    log2Report.update(record);
  }

  public class Report {
    int size;
    int naive;
    int partial;

    public int fd;
    public int sd;
    public int d_fd;
    public int d_sd;

    public int latency;
    public int qTime;
    public int tqTime;
    public int taskLatency;

    public Report(int size) {
      this.size = size;
    }

    public void update(Record record) {
      if (record.isPartial) {
        partial++;
        d_fd += record.d_fd;
        d_sd += record.d_sd;

      } else {
        naive++;
      }
    }

    public String toString() {
      return size + "\t" + naive + "\t" + partial + "\t" + (float) partial/(naive + partial) + "\n";
    }
  }
}


