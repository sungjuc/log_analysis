package com.linkedin.partial_update.report.common;

import com.linkedin.partial_update.common.Evaluation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public abstract class Report {
  protected FileOutputStream fos = null;
  protected BufferedWriter bw = null;

  protected void init(String file_name) throws IOException {
    fos = new FileOutputStream(new File(file_name));
    bw = new BufferedWriter(new OutputStreamWriter(fos));
    bw.write(getHeader());
  }

  protected abstract void addReport(Evaluation evaluation) throws IOException;

  public void finish() throws IOException {
    if (fos != null) {
      fos.flush();
      fos.close();
    }
    if (bw != null) {
      bw.close();
    }
  }

  protected abstract String getHeader();
}
