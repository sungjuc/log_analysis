package com.linkedin.partial_update;

import com.linkedin.partial_update.common.FileConstants;
import com.linkedin.partial_update.common.TestConstants;
import com.linkedin.partial_update.report.common.UserGroupReport;
import com.linkedin.partial_update.report.log.LogReporter;
import com.linkedin.partial_update.report.log.UserGroupReporter;
import com.linkedin.partial_update.report.old.PartialUpdateJobReporter;
import com.linkedin.partial_update.util.FileListFilter;


public class PartialUpdateLogProcessor {

  public static void main(String[] args) throws Exception {
    //NetworkSizeReport.computeNetworkSize(args[0]);
    //NetworkSizeReport.computeNetworkSizeHistogram(args[0]);
    //PartialUpdateSavings.computePartialUpdateSavings(args[0]);
    //PartialUpdateJobReporter.createReport(args[0]);
    //LogReporter.createReport(FileConstants.PROD_LOG_PATH, new FileListFilter(TestConstants.TEST_DATE));
    UserGroupReporter.createReport(FileConstants.PROD_LOG_PATH, new FileListFilter(TestConstants.TEST_DATE));
  }
}
