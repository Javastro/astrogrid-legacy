/*
 * $Id: MakerTest.java,v 1.2 2010/03/25 10:25:52 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import junit.framework.TestCase;
import org.astrogrid.slinger.sourcetargets.URISourceTargetMaker;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;


/**
 * Tests that the homespace/url maker works OK
 * @author M Hill
 */

public class MakerTest extends TestCase {
   
   public void testUrl() throws Exception {
      
      if (!(URISourceTargetMaker.makeSourceTarget("ftp://ftp.etc.etc/path/file") instanceof UrlSourceTarget)) {
         fail("Maker does not return url");
      }
   }

   public void testInvalidUri()  {
      try {
         URISourceTargetMaker.makeSourceTarget("silly://ftp.etc.etc/path/file");
         
         fail("Maker did not fail with bad uri");
      }
      catch (URISyntaxException e) {}  //fine, ignore
      catch (MalformedURLException e) {} //fine, ignore
   }

}



