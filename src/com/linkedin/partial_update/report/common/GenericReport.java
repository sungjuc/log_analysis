package com.linkedin.partial_update.report.common;

import com.linkedin.partial_update.common.Evaluation;
import com.linkedin.partial_update.common.GenericEvaluation;
import java.io.IOException;


public class GenericReport extends Report {
  public static final String FILE_NAME = "generic_job_report";

  public GenericReport() throws IOException {
    init(FILE_NAME);
  }

  public void addReport(Evaluation evaluation) throws IOException {
    bw.newLine();
    bw.write(transform(evaluation));
    bw.flush();
  }

  public String getHeader() {
    StringBuilder sb = new StringBuilder();

    sb.append("ttl").append("\t");
    sb.append("isPartialUpdate").append("\t");
    sb.append("timeBuffer").append("\t");

    sb.append("Requests").append("\t");
    sb.append("Duplicates").append("\t");
    sb.append("Exceptions").append("\t");

    sb.append("OutRequests").append("\t");
    sb.append("NaiveUpdates").append("\t");
    sb.append("PartialUpdates").append("\t");
    sb.append("PartialUpdateRate").append("\t");

    sb.append("AvgFdSize").append("\t");
    sb.append("AvgSdSize").append("\t");

    sb.append("AvgNFdSize").append("\t");
    sb.append("AvgNSdSize").append("\t");

    sb.append("AvgPFdSize").append("\t");
    sb.append("AvgPSdSize").append("\t");
    sb.append("AvgPDFdSize").append("\t");
    sb.append("AvgPDSdSize").append("\t");
    sb.append("NoFDchanges").append("\t");

    sb.append("AvgLatency").append("\t");
    sb.append("AvgTaskLetency").append("\t");
    sb.append("AvgQTime").append("\t");
    sb.append("AvgTQTime").append("\t");

    sb.append("NAvgLatency").append("\t");
    sb.append("NAvgTaskLetency").append("\t");
    sb.append("NAvgQTime").append("\t");
    sb.append("NAvgTQTime").append("\t");

    sb.append("PAvgLatency").append("\t");
    sb.append("PAvgTaskLetency").append("\t");
    sb.append("PAvgQTime").append("\t");
    sb.append("PAvgTQTime").append("\t");

    return sb.toString();
  }

  protected String transform(Evaluation genericEvaluation) {
    StringBuilder sb = new StringBuilder();

    sb.append(genericEvaluation.ttl).append("\t");
    sb.append(genericEvaluation.isPartialUpdate).append("\t");
    sb.append(genericEvaluation.timeBuffer).append("\t");

    sb.append(genericEvaluation.requests).append("\t");
    sb.append(genericEvaluation.duplicates).append("\t");
    sb.append(genericEvaluation.exceptions).append("\t");

    sb.append(genericEvaluation.outRequests).append("\t");
    sb.append(genericEvaluation.naiveUpdates).append("\t");
    sb.append(genericEvaluation.partialUpdates).append("\t");
    sb.append((float) genericEvaluation.partialUpdates / genericEvaluation.outRequests).append("\t");

    sb.append((float) genericEvaluation.fdSize / genericEvaluation.outRequests).append("\t");
    sb.append((float) genericEvaluation.sdSize / genericEvaluation.outRequests).append("\t");

    sb.append((float) genericEvaluation.nFdSize / genericEvaluation.naiveUpdates).append("\t");
    sb.append((float) genericEvaluation.nSdSize / genericEvaluation.naiveUpdates).append("\t");

    if (genericEvaluation.isPartialUpdate) {
      sb.append((float) genericEvaluation.pFdSize / genericEvaluation.partialUpdates).append("\t");
      sb.append((float) genericEvaluation.pSdSize / genericEvaluation.partialUpdates).append("\t");
      sb.append((float) genericEvaluation.pDFdSize / genericEvaluation.partialUpdates).append("\t");
      sb.append((float) genericEvaluation.pDSdSize / genericEvaluation.partialUpdates).append("\t");
    } else {
      sb.append(0).append("\t");
      sb.append(0).append("\t");
      sb.append(0).append("\t");
      sb.append(0).append("\t");
    }
    sb.append(genericEvaluation.noFdChanges).append("\t");

    sb.append(((float) genericEvaluation.nLatency + genericEvaluation.pLatency) / genericEvaluation.outRequests).append("\t");
    sb.append(((float) genericEvaluation.nTaskLatency + genericEvaluation.pTaskLatency) / genericEvaluation.outRequests).append("\t");
    sb.append(((float) genericEvaluation.nQTime + genericEvaluation.pQTime) / genericEvaluation.outRequests).append("\t");
    sb.append(((float) genericEvaluation.nTqTime + genericEvaluation.pTqTime) / genericEvaluation.outRequests).append("\t");

    sb.append(((float) genericEvaluation.nLatency) / genericEvaluation.naiveUpdates).append("\t");
    sb.append(((float) genericEvaluation.nTaskLatency) / genericEvaluation.naiveUpdates).append("\t");
    sb.append(((float) genericEvaluation.nQTime) / genericEvaluation.naiveUpdates).append("\t");
    sb.append(((float) genericEvaluation.nTqTime) / genericEvaluation.naiveUpdates).append("\t");

    if (genericEvaluation.isPartialUpdate) {
      sb.append(((float) genericEvaluation.pLatency) / genericEvaluation.partialUpdates).append("\t");
      sb.append(((float) genericEvaluation.pTaskLatency) / genericEvaluation.partialUpdates).append("\t");
      sb.append(((float) genericEvaluation.pQTime) / genericEvaluation.partialUpdates).append("\t");
      sb.append(((float) genericEvaluation.pTqTime) / genericEvaluation.partialUpdates).append("\t");
    } else {
      sb.append(0).append("\t");
      sb.append(0).append("\t");
      sb.append(0).append("\t");
      sb.append(0).append("\t");
    }

    return sb.toString();
  }
}
