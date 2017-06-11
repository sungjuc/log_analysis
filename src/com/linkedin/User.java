package com.linkedin;

public class User {
  int id;
  boolean isPartial = false;
  int fd;
  int sd;
  int d_fd;
  int d_sd;

  public User(int id, boolean b, int fd, int sd, int dfd, int dsd) {
    this.id = id;
    this.isPartial = b;
    this.fd = fd;
    this.sd = sd;
    this.d_fd = dfd;
    this.d_sd = dsd;
  }
}
