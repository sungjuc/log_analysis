package com.linkedin.partial_update.common;

public class GenericEvaluation extends Evaluation{
  public GenericEvaluation(String ttl, boolean isPartialUpdate, String timeBuffer) {
    super(ttl, isPartialUpdate, timeBuffer);
  }

  @Override
  public boolean satisfy(Record record) {
    return record != null;
  }
}
