package org.astrogrid.mySpace.delegate.mySpaceManager;

import java.net.URL;
import java.util.Vector;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.astrogrid.mySpace.delegate.MySpaceManagerDelegate;

/**
 * Junit tests for the <code>MySpaceManagerDelegate</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */
public final class MySpaceManagerDelegateTest extends TestCase {
  private boolean fakeWebServices = true;
  private MySpaceManagerDelegate delegate = new MySpaceManagerDelegate();
  private String mssUrl = "http://www.roe.ac.uk/acdwww/index.html";
  private static boolean first = true;

  private String userId = "acd";
  private String communityId = "roe";
  private String credentials = "none whatsoever";

  /**
   * Standard constructor for JUnit test classes.
   * @param name test name
   */

  public MySpaceManagerDelegateTest(final String name) {
    super(name);
  }

  /**
   * Standard method which is invoked before running each of the tests.
   * It attempts to create a new delegate object and to determine whether
   * whether the object is using the real or fake Web service objects.
   * If some misadventure occurs the delegate object is set to null.
   */

  protected void setUp() {
    try { //System.out.println("mssUrl: " +  mssUrl);
      delegate = new MySpaceManagerDelegate(mssUrl);

      Vector allMssUrls = delegate.getAllMssUrl();
      delegate.setQueryMssUrl(allMssUrls);

      if (allMssUrls.size() == 2) {
        String firstUrl = (String) allMssUrls.elementAt(0);
        //            System.out.println("firstUrl: " + firstUrl);
        if (!firstUrl.equals("http://www.roe.ac.uk/acdwww/index.html")) {
          fakeWebServices = false;
        }
      }

      if (first) {
        if (fakeWebServices) {
          System.out.println("Using fake Web service objects.");
        } else {
          System.out.println("Using real Web service objects.");
        }

        first = false;

        int mySpaceIndex;
        for (int loop = 0; loop < allMssUrls.size(); loop++) {
          mySpaceIndex = loop + 1;
          System.out.println(
            "  MySpace system "
              + mySpaceIndex
              + ", URL: "
              + (String) allMssUrls.elementAt(loop));
        }
      }
    } catch (Exception setUpEx) {
      System.out.println("*** Error setting up to run test.");
      setUpEx.printStackTrace();
      delegate = null;
    }
  }

  // ====================================================================

  /**
   * Test the <code>listDataHoldings</code> method.
   */

  public void testListDataHoldings() {
    System.out.println("Testing listDataHoldings...");

    try {
      if (delegate != null) {
        Vector result =
          delegate.listDataHoldings(userId, communityId, credentials, "*");

        //            System.out.println("  no. elements: " + result.size());

        //            Object o = (Object)result.elementAt(0);
        //            String s = o.toString();
        //            System.out.println(s);

        Vector firstResult = (Vector) result.elementAt(0);

        if (fakeWebServices) {
          Assert.assertTrue(result.size() == 2);
          Assert.assertEquals((String) firstResult.elementAt(0), "myfile");
        } else {
          Assert.assertTrue(result.size() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during listDataHoldings.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>listDataHoldingsGen</code> method.
   */

  public void testListDataHoldingsGen() {
    System.out.println("Testing listDataHoldingsGen...");

    try {
      if (delegate != null) {
        Vector result =
          delegate.listDataHoldingsGen(userId, communityId, credentials, "*");

        if (fakeWebServices) {
          Assert.assertTrue(result.size() == 2);

          String resultElement = (String) result.elementAt(0);
          int pos =
            resultElement.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
          Assert.assertTrue(pos > -1);

          resultElement = (String) result.elementAt(1);
          pos =
            resultElement.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
          Assert.assertTrue(pos > -1);
        } else {
          Assert.assertTrue(result.size() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during listDataHoldingsGen.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>listDataHolding</code> method.
   */

  public void testListDataHolding() {
    System.out.println("Testing listDataHolding...");

    try {
      if (delegate != null) {
        String result =
          delegate.listDataHolding(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1");

        if (fakeWebServices) {
          Assert.assertEquals(result, "lookupDataHolderDetails");
        } else {
          Assert.assertTrue(result.length() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during listDataHolding.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>copyDataHolding</code> method.
   */

  public void testCopyDataHolding() {
    System.out.println("Testing copyDataHolding...");

    try {
      if (delegate != null) {
        String result =
          delegate.copyDataHolding(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1",
            "/acd@roe/serv1/file2");

        if (fakeWebServices) {
          Assert.assertEquals(result, "copyDataHolder");
        } else {
          Assert.assertTrue(result.length() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during copyDataHolding.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>copyRemoteDataHolding</code> method.
   */

  public void testCopyRemoteDataHolding() {
    System.out.println("Testing copyRemoteDataHolding...");

    try {
      if (delegate != null) {
        String remoteUrl = delegate.getMssUrl();
        boolean result =
          delegate.copyRemoteDataHolding(
            userId,
            communityId,
            credentials,
            remoteUrl,
            "/acd@roe/serv1/file1",
            "/acd@roe/serv1/file3");

        if (fakeWebServices) {
          Assert.assertTrue(result);
        } else {
          Assert.assertTrue(result);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during copyRemoteDataHolding.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>renameDataHolding</code> method.
   */

  public void testRenameDataHolding() {
    System.out.println("Testing renameDataHolding...");

    try {
      if (delegate != null) {
        String result =
          delegate.renameDataHolding(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1",
            "/acd@roe/serv1/file2");

        if (fakeWebServices) {
          Assert.assertEquals(result, "moveDataHolder");
        } else {
          Assert.assertTrue(result.length() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during moveDataHolding.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>deleteDataHolding</code> method.
   */

  public void testDeleteDataHolding() {
    System.out.println("Testing deleteDataHolding...");

    try {
      if (delegate != null) {
        String result =
          delegate.deleteDataHolding(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1");

        if (fakeWebServices) {
          Assert.assertEquals(result, "deleteDataHolder");
        } else {
          Assert.assertTrue(result.length() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during deleteDataHolding.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>saveDataHolding</code> method.
   */

  public void testSaveDataHolding() {
    System.out.println("Testing saveDataHolding...");

    try {
      if (delegate != null) {
        boolean result =
          delegate.saveDataHolding(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1",
            "contents required.",
            "VOtable",
            "");

        if (fakeWebServices) {
          Assert.assertTrue(result);
        } else {
          Assert.assertTrue(result);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during saveDataHolding.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>saveDataHoldingURL</code> method.
   */

  public void testSaveDataHoldingURL() {
    System.out.println("Testing saveDataHoldingURL...");

    try {
      if (delegate != null) {
        boolean result =
          delegate.saveDataHoldingURL(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1",
            "http://www.roe.ac.uk/acdwww/index.html",
            "VOtable",
            "");

        if (fakeWebServices) {
          Assert.assertTrue(result);
        } else {
          Assert.assertTrue(result);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during saveDataHoldingURL.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>getDataHolding</code> method.
   */

  public void testGetDataHolding() {
    System.out.println("Testing getDataHolding...");

    try {
      if (delegate != null) {
        String result =
          delegate.getDataHolding(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1");

        //            System.out.println(result);

        if (fakeWebServices) {
          int pos = result.indexOf("Davenhall");
          Assert.assertTrue(pos > -1);
        } else {
          Assert.assertTrue(result.length() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during getDataHolding.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>getDataHoldingUrl</code> method.
   */

  public void testGetDataHoldingUrl() {
    System.out.println("Testing getDataHoldingUrl...");

    try {
      if (delegate != null) {
        String result =
          delegate.getDataHoldingUrl(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1");

        //            System.out.println("getDataHoldingUrl: " + result);

        boolean isUrl = true;

        try {
          URL url = new URL(result);
        } catch (Exception eurl) {
          isUrl = false;
        }

        Assert.assertTrue(isUrl);
      }
    } catch (Exception e) {
      System.out.println("*** Error during getDataHoldingUrl.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>extendLease(</code> method.
   */

  public void testExtendLease() {
    System.out.println("Testing extendLease(...");
    final int extensionPeriod = 15;
    try {
      if (delegate != null) {
        String result =
          delegate.extendLease(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1",
            extensionPeriod);

        if (fakeWebServices) {
          Assert.assertEquals(result, "extendLease");
        } else {
          Assert.assertTrue(result.length() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during extendLease.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>publish</code> method.
   */

  public void testPublish() {
    System.out.println("Testing publish...");

    try {
      if (delegate != null) {
        String result =
          delegate.publish(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1");

        if (fakeWebServices) {
          Assert.assertEquals(result, "publish");
        } else {
          Assert.assertTrue(result.length() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during publish.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>createContainer</code> method.
   */

  public void testCreateContainer() {
    System.out.println("Testing createContainer...");

    try {
      if (delegate != null) {
        String result =
          delegate.createContainer(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/newcontainer");

        if (fakeWebServices) {
          Assert.assertEquals(result, "createContainer");
        } else {
          Assert.assertTrue(result.length() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during createContaine.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>createUser</code> method.
   */

  public void testCreateUser() {
    System.out.println("Testing createUser...");

    try {
      if (delegate != null) {
        Vector servers = new Vector();
        servers.add("serv1");
        servers.add("serv2");
        servers.add("serv3");
        servers.add("serv4");

        boolean result =
          delegate.createUser("helen", "troy", credentials, servers);

        if (fakeWebServices) {
          Assert.assertTrue(result);
        } else {
          Assert.assertTrue(result);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during createUser.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>deleteUser</code> method.
   */

  public void testDeleteUser() {
    System.out.println("Testing deleteUser...");

    try {
      if (delegate != null) {
        boolean result = delegate.deleteUser("helen", "troy", credentials);

        if (fakeWebServices) {
          Assert.assertTrue(result);
        } else {
          Assert.assertTrue(result);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during deleteUser.");
      e.printStackTrace();
    }
  }

  /**
   * Test the <code>changeOwner</code> method.
   */

  public void testChangeOwner() {
    System.out.println("Testing changeOwner...");

    try {
      if (delegate != null) {
        String result =
          delegate.changeOwner(
            userId,
            communityId,
            credentials,
            "/acd@roe/serv1/file1",
            "helen");

        if (fakeWebServices) {
          Assert.assertEquals(result, "changeOwner");
        } else {
          Assert.assertTrue(result.length() > 0);
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error during changeOwner.");
      e.printStackTrace();
    }
  }

  // =======================================================================

  /**
   * Main method to run the class.
   * @param argv ignored
   */

  public static void main(final String[] argv) {

    //
    //   If the URL of a MySpace system was supplied as the first argument
    //   then use it, otherwise adopt a fake URL.

    //      if (argv.length == 1)
    //      {  MySpaceManagerDelegateTest.mssUrl = argv[0];
    //      }
    //      else
    //      {  MySpaceManagerDelegateTest.mssUrl =
    //            "http://www.roe.ac.uk/acdwww/index.html";
    //      }

    //      System.out.println(MySpaceManagerDelegateTest.mssUrl);

    //
    //   Run the tests.

    //    junit.textui.TestRunner.run (MySpaceManagerDelegateTest.class);
    junit.swingui.TestRunner.run(MySpaceManagerDelegateTest.class);
  }
}
