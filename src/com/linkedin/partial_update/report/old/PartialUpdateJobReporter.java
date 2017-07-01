package com.linkedin.partial_update.report.old;

import com.linkedin.partial_update.common.GenericEvaluation;
import com.linkedin.partial_update.common.LogConstants;
import com.linkedin.partial_update.report.common.GenericReport;
import com.linkedin.partial_update.util.HistoReport;
import com.linkedin.partial_update.util.FileListFilter;
import com.linkedin.partial_update.util.LogStorage;
import java.io.IOException;


/**
 * PartialUpdate GenericEvaluation report. This report consider a set of application logs with same TTL, PartialUpdateMode and
 * TimeBuffer as a single job.
 */
public class PartialUpdateJobReporter {
  public static boolean generalReportGenerated = false;

  /**
   * Creating the general report.
   * @param dirName
   * @throws IOException
   */
  public static void createReport(String dirName, FileListFilter fileListFilter) throws IOException {
    LogStorage logStorage = new LogStorage(dirName, fileListFilter);

    String ttl = null;
    boolean isPartialUpdate = false;
    String timeBuffer;

    GenericEvaluation genericEvaluation = null;

    String line;

    GenericReport generalJobReport = new GenericReport();
    HistoReport histoReport = new HistoReport();

    while ((line = logStorage.readLine()) != null) {
      if (line.contains("MemCacheTTL changed: ")) {
        if (genericEvaluation != null) {
          generalJobReport.addReport(genericEvaluation);
          histoReport.addReport(genericEvaluation);
        }
        ttl = line.substring(line.indexOf(LogConstants.NEW_VALUE) + LogConstants.NEW_VALUE.length()).trim();
      }

      if (line.contains("partialUpdateMode: ")) {
        isPartialUpdate = Boolean.parseBoolean(
            line.substring(line.indexOf(LogConstants.NEW_VALUE) + LogConstants.NEW_VALUE.length()).trim());
      }

      if (line.contains("TimeBuffer changed: ")) {
        timeBuffer = line.substring(line.indexOf(LogConstants.NEW_VALUE) + LogConstants.NEW_VALUE.length()).trim();
        genericEvaluation = new GenericEvaluation(ttl, isPartialUpdate, timeBuffer);
      }

      if (genericEvaluation != null) {
        genericEvaluation.process(line);
      }
    }

    generalJobReport.finish();
  }
}
