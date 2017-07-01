package com.linkedin.partial_update.report.common;

import com.linkedin.partial_update.util.FileListFilter;
import java.io.IOException;
import java.util.Arrays;


public class UserGroupReport extends GenericReport {
  public static final String FILE_NAME = "user_group";

  public UserGroupReport(FileListFilter filter, int[] range) throws IOException {
    init(FILE_NAME + "_" + filter.toString() + "_" + Arrays.toString(range));
  }
}
