package com.linkedin;

public class User {
  int id;
  boolean isPartial = false;
  int fd;
  int sd;
  int d_fd;
  int d_sd;

  int latency;
  int qTime;
  int tqTime;
  int taskLatency;

  public User(int id, boolean b, int fd, int sd, int dfd, int dsd, int latency, int qTime, int tqTime, int taskLatency) {
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
