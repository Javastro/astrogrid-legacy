package org.astrogrid.mySpace.mySpaceUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import junit.framework.TestCase;


/**
 * A command-line client for tests of file transfers.
 * Each invocation of this client copies a data-set from one
 * of a given set of source URLs that are mirrors of the same
 * data and leaves the copy in a local file.  The source and 
 * destination of the data are given on the command line.  The
 * first argument must be the name of the destination file.
 * The second and subsequent arguments are taken as the names of
 * URLs mirroring the data source.
 *
 * To successfully use GridFTP in the transfer, the user running
 * this class must have previously created a proxy id-certficate,
 * of the kind used with the Globus Toolkit, in the standard
 * location where the Java CoG kit looks for such certficates.
 * Running the grid-proxy-init script that comes with Java CoG
 * is the normal way to do this.
 * 
 * @author Guy Rixon.
 */
public class FileTransferClient {

  public static void main (String[] args) throws Exception {

    // Check and extract the command-line arguments.
    if (args.length < 2) {
      throw new Exception("Usage: FileTransferClient "
                          + "<destination file> <source URL> "
                          + "[<source URL> ...]");
    }
    String outFileName = args[0];
    String[] inUrls = new String[args.length-1];
    for (int i = 1; i < args.length; i++) {
      inUrls[i-1] = args[i];
    }

    // Invoke the transfer.
    FileTransfer ft = new FileTransfer(inUrls, outFileName);
    ft.transfer();

    // Report what really happened.
    // Since this is a test programme, report errors that were handled
    // silently, even if the transfer eventually succeeded. 
    System.out.println("Success!");
    System.out.println("URL that achieved the transfer: "
                       + ft.getChosenUrl());
    System.out.println("Errors, if any, for each URL:");
    Exception[] errors = ft.getErrors();
    for (int i = 0; i < inUrls.length; i++) {
       String error = (errors[i] == null)? "" : errors[i].getMessage();
       System.out.println(inUrls[i] + " : " + error);
    }
  }

}
