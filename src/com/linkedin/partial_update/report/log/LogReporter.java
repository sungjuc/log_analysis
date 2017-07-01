package com.linkedin.partial_update.report.log;

import com.linkedin.partial_update.common.Evaluation;
import com.linkedin.partial_update.common.GenericEvaluation;
import com.linkedin.partial_update.common.FileConstants;
import com.linkedin.partial_update.report.common.ProductionLogReport;
import com.linkedin.partial_update.util.FileListFilter;
import com.linkedin.partial_update.util.LogStorage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LogReporter {

  public static void createReport(String dir, FileListFilter fileListFilter) throws IOException {
    System.out.println("[LogReporter] Creating Report!!!");
    Map<String, LogStorage> logStorageMap = LogStorage.createLogStorageMap(dir, fileListFilter);
    System.out.println("[LogReporter] Log Storage Set up Done !!!");
    List<Evaluation> genericEvaluationList = new ArrayList<>();
    ProductionLogReport report = new ProductionLogReport(fileListFilter);
    String prodLogHostListFilePath = FileConstants.DATA_REPOSITORY_PATH + FileConstants.PROD_LOG_HOST_LIST_FILE;

    File prodLogHostListFile = new File(prodLogHostListFilePath);
    BufferedReader br = new BufferedReader(new FileReader(prodLogHostListFile));
    String line;

    while ((line = br.readLine()) != null) {
      String[] words = line.split("\\s+");
      String host = words[0].substring(0, line.indexOf("."));
      System.out.println("[LogReporter] Processing host: " + host);
      String ttl = words[1].trim();
      String timeBuffer = words[2].trim();
      boolean isPartialUpdate = Boolean.parseBoolean(words[3].trim());

      Evaluation evaluation = new GenericEvaluation(ttl, isPartialUpdate, timeBuffer);
      LogStorage logStorage = logStorageMap.get(host);
      assert(logStorage != null);

      String logLine = logStorage.readLine();

      while(logLine != null) {
        evaluation.process(logLine);
        logLine = logStorage.readLine();
      }

      genericEvaluationList.add(evaluation);
      report.addReport(evaluation);
    }

    report.finish();
  }
}
