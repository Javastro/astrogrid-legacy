package org.astrogrid.mySpace.mySpaceUtil;

import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.util.Vector;

class FileTransferTestCase {

  private String[] urls;
  private int      urlIndex  = 0;
  private boolean  endOfFile = false;
  private boolean  hasTests  = false;


  /**
   * Constructs a FileTransferTestCase by parsing the next line from
   * a given BufferedReader.
   */
  public FileTransferTestCase (BufferedReader b) throws Exception {
    String l = b.readLine();
    if (l == null) {
	  this.endOfFile = true;
    }
    else {
      System.out.println();
      System.out.println("Test sources: " + l);
      Vector v = new Vector();
      StringTokenizer t = new StringTokenizer(l, " ");

      // The first token can be null (blank line), hash
      // (comment line) or an integer (indicates the index
      // counted from zero of the URL in the following list that is
      // expected to perform the transfer successfully.
      String q;
      try {
        q = t.nextToken();
      }
      catch (Exception e) {
		q = null;
      }
      if (q != null && !"#".equals(q)) {
        this.urlIndex = Integer.parseInt(q);

        // The subsequent tokens are all URLS to be tried in the transfer.
        while (t.hasMoreTokens()) {
          String u = t.nextToken();
          v.addElement(u);
        }
        urls = new String[v.size()];
        for (int i = 0; i < urls.length; i++) {
          urls[i] = (String)v.elementAt(i);
        }
        this.hasTests = true;
	  }
    }
  }


  /**
   * Returns the set of source URLs obtained during construction.
   * This array may be null if the constructor found no more data
   * to parse.
   */
  public String[] getSourceUrls () {
	return this.urls;
  }


  /**
   * Returns the index of the URL expected to complete
   * the transfer.  The index appies to the String array
   * returned by {@link getSourceUrls} and is thus counted
   * from zero.
   */
  public int getUrlIndex () {
	return this.urlIndex;
  }


  /**
   * Indicates if the end of the file of test cases has been reached.
   *
   * @return true if the end of the file has been reached, false otherwise.
   */
  public boolean isEndOfFile () {
	return this.endOfFile;
  }


  /**
   * Indicates whether this object has data for a test case.
   * Normally, constructing the object reads in data for one test
   * case from the next line of the file.  If the next line is
   * blank or a comment, or if there is no next line, then the
   * object has no useful test data and must be discarded.
   */
  public boolean isUsefulTestCase () {
	return this.hasTests;
  }

}