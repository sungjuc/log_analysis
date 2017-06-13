package com.linkedin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PartialUpdateLogProcessor {

  public static void main(String[] args) throws Exception {
    //NetworkSizeReport.computeNetworkSize(args[0]);
    //NetworkSizeReport.computeNetworkSizeHistogram(args[0]);
    //PartialUpdateSavings.computePartialUpdateSavings(args[0]);
    PartialUpdateTestReport.generateReport(args[0]);
  }
}
