package com.linkedin.partial_update.report.log;

import com.linkedin.partial_update.common.Evaluation;
import com.linkedin.partial_update.common.GenericEvaluation;
import com.linkedin.partial_update.common.FileConstants;
import com.linkedin.partial_update.common.TestConstants;
import com.linkedin.partial_update.common.UserGroupEvaluation;
import com.linkedin.partial_update.report.common.UserGroupReport;
import com.linkedin.partial_update.util.FileListFilter;
import com.linkedin.partial_update.util.LogStorage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserGroupReporter {
  public static void createReport(String dir, FileListFilter fileListFilter) throws IOException {
    System.out.println("[UserGroupReporter] Creating Report!!!");
    Map<String, LogStorage> logStorageMap = LogStorage.createLogStorageMap(dir, fileListFilter);
    System.out.println("[UserGroupReporter] Log Storage Set up Done !!!");
    List<Evaluation> evaluationList = new ArrayList<>();
    UserGroupReport report = new UserGroupReport(fileListFilter, TestConstants.USER_SIZE_RANGE);

    String prodLogHostListFilePath = FileConstants.DATA_REPOSITORY_PATH + FileConstants.PROD_LOG_HOST_LIST_FILE;

    File prodLogHostListFile = new File(prodLogHostListFilePath);
    BufferedReader br = new BufferedReader(new FileReader(prodLogHostListFile));
    String line;

    while ((line = br.readLine()) != null) {
      String[] words = line.split("\\s+");
      String host = words[0].substring(0, line.indexOf("."));
      System.out.println("[UserGroupReporter] Processing host: " + host);
      String ttl = words[1].trim();
      String timeBuffer = words[2].trim();
      boolean isPartialUpdate = Boolean.parseBoolean(words[3].trim());

      Evaluation evaluation = new UserGroupEvaluation(ttl, isPartialUpdate, timeBuffer, TestConstants.USER_SIZE_RANGE);
      LogStorage logStorage = logStorageMap.get(host);
      assert(logStorage != null);

      String logLine = logStorage.readLine();

      while(logLine != null) {
        evaluation.process(logLine);
        logLine = logStorage.readLine();
      }

      evaluationList.add(evaluation);
      report.addReport(evaluation);
    }

    report.finish();
  }
}
