package com.linkedin.partial_update.common;

public class UserGroupEvaluation extends Evaluation{
  private final double min;
  private final double max;

  public UserGroupEvaluation(String ttl, boolean isPartialUpdate, String timeBuffer, int[] range) {
    super(ttl, isPartialUpdate, timeBuffer);
    this.min= range[0];
    this.max = range[1];
  }

  @Override
  public boolean satisfy(Record record) {
    if (record == null)
      return false;
    double logSize = record.fd;
    return (min <= logSize && logSize < max);
  }
}
