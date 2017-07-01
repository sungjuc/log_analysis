package com.linkedin.partial_update.report.common;

import com.linkedin.partial_update.util.FileListFilter;
import java.io.IOException;


public class ProductionLogReport extends GenericReport {
  public static final String FILE_NAME = "production_log";

  public ProductionLogReport(FileListFilter filter) throws IOException {
    init(FILE_NAME + "_" + filter.toString());
  }
}
