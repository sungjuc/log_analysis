package com.linkedin.partial_update.common;

public class Record {
  public int id;
  public boolean isPartial = false;
  public int fd;
  public int sd;
  public int d_fd;
  public int d_sd;

  public int latency;
  public int qTime;
  public int tqTime;
  public int taskLatency;

  public Record(int id, boolean b, int fd, int sd, int dfd, int dsd, int latency, int qTime, int tqTime, int taskLatency) {
    this.id = id;
    this.isPartial = b;
    this.fd = fd;
    this.sd = sd;
    this.d_fd = dfd;
    this.d_sd = dsd;
    this.latency = latency;
    this.qTime = qTime;
    this.tqTime = tqTime;
    this.taskLatency = taskLatency;
  }
}
