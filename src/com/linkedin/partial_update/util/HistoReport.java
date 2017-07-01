package com.linkedin.partial_update.util;

import com.linkedin.partial_update.common.Evaluation;
import com.linkedin.partial_update.common.GenericEvaluation;
import com.linkedin.partial_update.report.common.Report;
import java.io.IOException;
import java.util.Map;


public class HistoReport extends Report {
  public static final String FILE_NAME = "production_log_histo";

  public HistoReport() throws IOException {
    init(FILE_NAME);
  }

  public HistoReport(FileListFilter filter) throws IOException {
    init(FILE_NAME+ "_" + filter.toString());
  }

  @Override
  public void addReport(Evaluation evaluation) throws IOException {
    System.out.println(evaluation.ttl + "\t" + evaluation.isPartialUpdate + "\t" + evaluation.timeBuffer);
    System.out.println(transform(evaluation));
  }

  @Override
  protected String getHeader() {
    return "";
  }

  private String transform(Evaluation evaluation) {
    StringBuilder sb = new StringBuilder();

    //for (Map.Entry<Integer, Histogram.Report> entry: evaluation.histogram.log2Hist.entrySet()) {
      //sb.append(entry.getValue().toString());
    //}

    return sb.toString();
  }
}
