package org.astrogrid.mySpace.mySpaceUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import junit.framework.TestCase;


/**
 * JUnit tests for the FileTransfer class.  A single test-method
 * is used that performs multiple transfers.  JUnit will report
 * this as one test, but it's really a sequence of test cases.
 *
 * The set of transfers attempted is defined in the file
 * transfer-test-sources.txt, which must be in the current
 * directory during the test.  The file is a text file.  Each
 * line of the file lists URLs, as a space-separate list, to
 * be used in one transfer, and each line causes a separate
 * transfer.  The data in each transfer are put in the file
 * transfer-tests-result.data in the current directory.
 *
 * The test fails if any invocation of FileTransfer.transfer
 * throws an exception.  No other checks of correctness
 * are made.
 *
 * @author Guy Rixon
 */
public class FileTransferTest extends TestCase {

  private BufferedReader testSources;

  /**
   * Sets up test fixtures, opening the stream
   * from the which the lists of test data-sources
   * will be read.
   *
   * @throws FileNotFoundException if transfer-tests-sources.txt
   * is not available in the current directory.
   */
  public void setUp () throws Exception {
	FileReader f = new FileReader("transfer-tests-sources.txt");
    this.testSources = new BufferedReader(f);
  }

  /**
   * Tests all transfer modes.  Sets of source URLs is
   * read from a file (see @link{readTestFromFile}) and
   * a transfer is attempted for each set.  The test is
   * passed if the transfer succeeds and failed otherwise.
   * The coverage and strength of the test depends on the
   * source URLS set in the file.
   *
   * All these transfer are expected to work.  See below
   * for transfers that are expected to fail.
   */
  public void testGoodTransfers () throws Exception {
	while (true) {
	  FileTransferTestCase tc = new FileTransferTestCase(this.testSources);
	  if (tc.isEndOfFile()) break;
	  if (!tc.isUsefulTestCase()) continue;
	  String[] s = tc.getSourceUrls();
	  File f = new File("transfer-tests-result.dat");
      f.delete();
	  FileTransfer ft;
	  if (s.length == 1 ) {
        ft = new FileTransfer(s[0], "transfer-tests-result.dat");
      }
      else {
	    ft = new FileTransfer(s, "transfer-tests-result.dat");
	  }
	  ft.transfer();
      String u = ft.getChosenUrl();
      assertEquals(u, s[tc.getUrlIndex()]);
      Exception[] errors = ft.getErrors();
      assertEquals(errors.length, s.length);
      assertEquals(ft.getChosenUrl(),s[tc.getUrlIndex()]);
      assertTrue(f.exists());
    }
  }

  /**
   * Test the error handling when all mirrors for a transfer
   * fail to give a valid download.
   */
  public void testBadTransfers () throws Exception {
	String[] mirrors = {"http://plausible/but/not/real",
	                    "bogus:protocol",
	                    "wibble:bling.bling.bling/woof/woof",
	                    "totally invalid syntax"};
    FileTransfer ft = new FileTransfer(mirrors, "transfer-test-result.dat");
    try {
      ft.transfer();
      fail("FileTransfer raised no exception for an impossible download.");
    }
    catch (FileTransferException fte) {
      System.out.println("FileTransferException (expected): "
                         + fte.getMessage());
    }
    catch (Exception e) {
      fail ("FileTransfer raised an Exception "
            + "that was not a FileTransferException.");
    }
  }

}