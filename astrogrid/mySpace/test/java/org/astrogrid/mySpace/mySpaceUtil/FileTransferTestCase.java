package org.astrogrid.mySpace.mySpaceUtil;

import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * A description of one test case for use with
 * org.astrogrid.mySpace.mySpaceUtil.FileTransferTest.
 *
 * In each test, the SUT attempts to download a data-set from
 * list of mirrors, stopping when it has a successful download
 * or when there are no more mirrors to try. The test metadata consist
 * in a set of source URLs for the mirrors and an index indicating
 * which mirror is expected to produce a successful download.
 *
 * To use this class, the client must supply, to the constructor,
 * a BufferedReader that emits lines from a test specification.
 * Each line must be blank, a comment (i.e. # in the first column)
 * or a test case.  A test-case line must be a space-separated
 * list of tokens in which the first is an index number and the
 * rest are URLs for mirrors of the test data-set.  The index
 * identifies the URL, by position in the list, counted from zero,
 * which is expected to produce a valid download.  All URls
 * earlier in the list must cause transfers to fail; all
 * URLs later in the list are ignored.
 *
 * The client should have a loop in each pass of which it
 * constructs and object of this class, tests {@link isUsefulTestCase}
 * and calls {@link getSourceUrls} and {@link getUrlIndex} if the
 * test case is good.  The client should test {@link isEndOfFile} to
 * decide when to end the loop.
 *
 * @author Guy Rixon
 */
class FileTransferTestCase {

  /**
   * The list of mirrors to use in the test.
   */
  private String[] urls;

  /**
   * The index of {@link urls} containing the mirror that is
   * expected to give a valid download.
   */
  private int      urlIndex  = 0;

  /**
   * True if the end of end of the test specification was reached.
   */
  private boolean  endOfFile = false;

  /**
   * True if the object contains a test case after construction.
   */
  private boolean  hasTests  = false;


  /**
   * Constructs a FileTransferTestCase by parsing the next line from
   * a given BufferedReader reading a test specification.
   *
   * @param b the source of the test metadata.
   *
   * @throws Exception if the test specification cannot be read or parsed.
   */
  public FileTransferTestCase (final BufferedReader b) throws Exception {
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
          urls[i] = (String) v.elementAt(i);
        }
        this.hasTests = true;
      }
    }
  }


  /**
   * Returns the set of source URLs obtained during construction.
   * This array may be null if the constructor found no more data
   * to parse.
   *
   * @return the list of source URLs.
   */
  public final String[] getSourceUrls () {
    return this.urls;
  }


  /**
   * Returns the index of the URL expected to complete
   * the transfer.  The index appies to the String array
   * returned by {@link getSourceUrls} and is thus counted
   * from zero.
   *
   * @return the URL index.
   */
  public final int getUrlIndex () {
    return this.urlIndex;
  }


  /**
   * Indicates if the end of the file of test cases has been reached.
   *
   * @return true if the end of the file has been reached, false otherwise.
   */
  public final boolean isEndOfFile () {
    return this.endOfFile;
  }


  /**
   * Indicates whether this object has data for a test case.
   * Normally, constructing the object reads in data for one test
   * case from the next line of the file.  If the next line is
   * blank or a comment, or if there is no next line, then the
   * object has no useful test data and must be discarded.
   *
   * @return true if this object has test metadata, false otherwise.
   */
  public final boolean isUsefulTestCase () {
    return this.hasTests;
  }

}
