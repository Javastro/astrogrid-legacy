package org.astrogrid.warehouse.ogsadai;

import junit.framework.TestCase;
import org.gridforum.ogsi.ExtensibilityType;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Exercise ... methods.
 * Live tests are performed against a grid service.
 */
public class GdsQueryDelegateTest extends TestCase {

  private GdsQueryDelegate gdsQueryDelegate = null;
  private OutputStream output = null;

  private String testRegistryUrl = 
  // HYDRA
      "http://hydra.star.le.ac.uk:8082" +
      "/gdw/services/ogsadai/DAIServiceGroupRegistry";	
  // CASS123
  //   "http://astrogrid.ast.cam.ac.uk:4040" +
  //   "/gdw/services/ogsadai/DAIServiceGroupRegistry";

  private String testRegistryUrlBad1 = 
      "http://bogus.star.le.ac.uk:8082" +
      "/gdw/services/ogsadai/DAIServiceGroupRegistry";	

  private String testRegistryUrlBad2 = 
      "http://hydra.star.le.ac.uk:8082" +
      "/bogus/services/ogsadai/DAIServiceGroupRegistry";	

  private String query1 = "SELECT * FROM first WHERE DECL &gt; 59.9";
  private String query2 = "SELECT * FROM first WHERE RA &lt; 0.5";
  private String queryBad = "this is not an sql query";

/*
  String factoryGsh2
      = "http://hydra.star.le.ac.uk:8082/gdw/services/"
      + "ogsadai/GridDataServiceFactory";

  private ExtensibilityType document = null;
  private ExtensibilityType result = null;	
*/	

  public GdsQueryDelegateTest(String name) {
    super(name);
  }
		
  public void setUp() {
    try {
      gdsQueryDelegate = new GdsQueryDelegate();
    }
    catch (Exception e) {
      fail("Could not construct GdsQueryDelegate: " + e);
    }
    //Write to a bytearray stream for now
    // (might need to use a file eventually for big output)
    output = new ByteArrayOutputStream();
  }

  public void tearDown() {
  }

/*
  // Test against real warehouse, using good query 1
  public void testDoRealQueryGood1() throws Exception {
    Document result = null;
    try {
      result = gdsQueryDelegate.doRealQuery(
                 query1, testRegistryUrl, output);
    }
    catch (Exception e) {
      fail("doRealQuery() failed: " + e.getMessage());
    }
  }

  // Test against real warehouse, using good query 2
  public void testDoRealQueryGood2() throws Exception {
    Document result = null;
    try {
      result = gdsQueryDelegate.doRealQuery(
                 query2, testRegistryUrl, output);
    }
    catch (Exception e) {
      fail("doRealQuery() failed: " + e.getMessage());
    }
  }

  // Test against real warehouse, checking consistency of query
  public void testDoRealQuerySame() throws Exception {
    Document result1 = null;
    Document result2 = null;
    try {
      result1 = gdsQueryDelegate.doRealQuery(
                 query1, testRegistryUrl, output);
      result2 = gdsQueryDelegate.doRealQuery(
                 query1, testRegistryUrl, output);
    }
    catch (Exception e) {
      fail("doRealQuery() failed: " + e.getMessage());
    }
    //assertEqual(result1,result2);
  }

  // Test against real warehouse, comparing results of 2 good queries
  // Queries should produce different results
  public void testDoRealQueryDifferent() throws Exception {
    Document result1 = null;
    Document result2 = null;
    try {
      result1 = gdsQueryDelegate.doRealQuery(
                 query1, testRegistryUrl, output);
      result2 = gdsQueryDelegate.doRealQuery(
                 query2, testRegistryUrl, output);
    }
    catch (Exception e) {
      fail("doRealQuery() failed: " + e.getMessage());
    }
    //assertNotEqual("Got same results for different queries",result1,result2);
  }


  // Test against bogus host in registry url
  public void testDoRealQueryBad1() throws Exception {
    Document result = null;
    try {
      result = gdsQueryDelegate.doRealQuery(
                  query1, testRegistryUrlBad1, output);
      fail("doRealQuery() returned without " +
           "exception for a bogus registry-GSH.");
    }
    catch (Exception e) {}
    assertNull(result);
  }

  // Test against bogus webapp in registry url 
  public void testDoRealQueryBad2() throws Exception {
    Document result = null;
    try {
      result = gdsQueryDelegate.doRealQuery(
                  query1, testRegistryUrlBad2, output);
      fail("doRealQuery() returned without " +
           "exception for a bogus registry-GSH.");
    }
    catch (Exception e) {}
    assertNull(result);
  }

  // Test against bogus SQL query 
  public void testDoRealQueryBad3() throws Exception {
    Document result = null;
    try {
      result = gdsQueryDelegate.doRealQuery(
                  queryBad, testRegistryUrl, output);
      fail("doRealQuery() returned without " +
           "exception for bogus SQL query.");
    }
    catch (Exception e) {}
    assertNull(result);
  }
  */
}
