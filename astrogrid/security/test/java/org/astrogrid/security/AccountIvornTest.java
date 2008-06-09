package org.astrogrid.security;

import java.net.URI;
import java.net.URISyntaxException;
import junit.framework.TestCase;
import java.security.Principal;

/**
 * JUnit tests for AccountIvorn.
 *
 * @author Guy Rixon
 */
public class AccountIvornTest extends TestCase {
  
  public void testGoodName1() throws Exception {
    AccountIvorn sut = new AccountIvorn("ivo://FredHoyle@IoA/ioa-community");
    assertEquals("ivo://FredHoyle@IoA/ioa-community", sut.toString());
  }
  
  public void testGoodName2() throws Exception {
    AccountIvorn sut = new AccountIvorn(new URI("ivo://FredHoyle@IoA/ioa-community"));
    assertEquals("ivo://FredHoyle@IoA/ioa-community", sut.toString());
  }
  
  public void testRejectBadName1() throws Exception {
    try {
      AccountIvorn sut = new AccountIvorn("ivo://IoA/ioa-community");
      fail("Should have thrown a URISyntaxException.");
    }
    catch (URISyntaxException e) {
      // Expected.
    }
  }
  
  public void testRejectBadName2() throws Exception {
    try {
      AccountIvorn sut = new AccountIvorn("http://FredHoyle@IoA/ioa-community");
      fail("Should have thrown a URISyntaxException.");
    }
    catch (URISyntaxException e) {
      // Expected.
    }
  }
  
  public void testRejectBadName3() throws Exception {
    try {
      AccountIvorn sut = new AccountIvorn("moomoowoof");
      fail("Should have thrown a URISyntaxException.");
    }
    catch (URISyntaxException e) {
      // Expected.
    }
  }

  public void testGetName() throws Exception {
    AccountIvorn sut = new AccountIvorn("ivo://FredHoyle@IoA/ioa-community");
    assertEquals("ivo://FredHoyle@IoA/ioa-community", sut.getName());
  }
  
  public void testToString() throws Exception {
    String name = "ivo://FredHoyle@IoA";
    AccountIvorn sut = new AccountIvorn(name);
    assertEquals(name, sut.toString());
  }
  
  
  public void testGetUserName() throws Exception {
    AccountIvorn sut = new AccountIvorn("ivo://FredHoyle@IoA");
    assertEquals("FredHoyle", sut.getUserName());
  }
  
  public void testGetCommunityIvorn() throws Exception {
    AccountIvorn sut = new AccountIvorn("ivo://FredHoyle@IoA/ioa-community");
    assertEquals(new URI("ivo://IoA/ioa-community"), sut.getCommunityIvorn());
  }
  
  public void testToUri() throws Exception {
    AccountIvorn sut = new AccountIvorn("ivo://FredHoyle@IoA/ioa-community");
    assertEquals(new URI("ivo://FredHoyle@IoA/ioa-community"), sut.toUri());
  }
  
}
