package org.astrogrid.mySpace.mySpaceUtil;

import junit.framework.TestCase;


/**
 * JUnit tests for the {@link FileTransferExceptionclass}.
 *
 * @author Guy Rixon
 */
public class FileTransferExceptionTest extends TestCase {

  /**
   * Tests the no-argument constructor.
   */
  public void testConstructor0 () {
    FileTransferException fte1
        = new FileTransferException();
    assertEquals(null, fte1.getMessage());
    assertEquals(null, fte1.getCause());
  }

  /**
   * Tests the one-argument constructor.
   */
  public void testConstructor1 () {

    FileTransferException fte2
        = new FileTransferException("Testing...");
    assertEquals(null, fte2.getCause());
    assertEquals("Testing...", fte2.getMessage());
  }

  /**
   * Tests the two-argument constructor.
   */
  public void testConstructor2 () {
    Exception e = new Exception("oops!");
    FileTransferException fte3
        = new FileTransferException("Testing...testing...", e);
    System.out.println("***" + fte3.getMessage() + "***");
    assertEquals("Testing...testing...", fte3.getMessage());
    assertEquals(fte3.getCause(), e);
  }

}
