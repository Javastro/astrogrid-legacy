/*
 * $Id: VoSpaceResolverTest.java,v 1.6 2004/04/22 08:58:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.local.LocalFileStore;


/**
 * Tests resolver
 *
 * @author M Hill
 */

public class VoSpaceResolverTest extends TestCase {

   public static final String ACCOUNT_FILE_KEY = "roe.ac.uk/mch";
   public static final String ACCOUNT_FILE_IVORN = "ivo://"+ACCOUNT_FILE_KEY+"#mch@roe.ac.uk/famous/data/file.txt";
   
   public static final String ACCOUNT_STOREPOINT_AGSL = "astrogrid:store:myspace:http://grendel12.roe.ac.uk/something";
   public static final String ACCOUNT_FILE_AGSL = ACCOUNT_STOREPOINT_AGSL+"#mch@roe.ac.uk/famous/data/file.txt";

   public static final String LOCALSTORE_KEY = "localmyspace";
   public static final String LOCALSTORE_ROOT = "resolverTestStore";
   public static final String LOCALSTORE_AGSL = "astrogrid:store:file://"+LOCALSTORE_ROOT;
   
                                             
   /** Tests that the shortcut works - ie that the local configuration can be used
    * to override registry/community resolving
    */
   public void testShortcut() throws IOException, MalformedURLException, URISyntaxException
   {
      // make sure it works
      SimpleConfig.getSingleton().setProperty(ACCOUNT_FILE_KEY, ACCOUNT_STOREPOINT_AGSL);
      
      Agsl resolvedAgsl = VoSpaceResolver.resolveAgsl(new Ivorn(ACCOUNT_FILE_IVORN));
      
      assertEquals(resolvedAgsl, new Agsl(ACCOUNT_FILE_AGSL));
      
      // make sure it doesn't
      SimpleConfig.getSingleton().setProperty(ACCOUNT_FILE_KEY, "Something wrong");
      
      try {
         resolvedAgsl = VoSpaceResolver.resolveAgsl(new Ivorn(ACCOUNT_FILE_IVORN));
         
         fail("Should have failed with badly formed Agsl");
      } catch (MalformedURLException mue) {
         //ignore - should happen
      }
    
//    assertFalse(new Agsl(ACCOUNT_FILE_AGSL).toString().equals(resolvedAgsl.toString()));
   }

   /**
    * Tests to see if we can resolve an input stream correctly */
   public void testFileResolve() throws IOException, URISyntaxException
   {
      String testContents = "A bit of text to make sure it gets created on the "+new Date();
      String filename = "resolverTestfile.txt";
      
      LocalFileStore store = new LocalFileStore(LOCALSTORE_ROOT);
      
      store.putString(testContents, filename, false);

      //add AGSL file location to configuration
      SimpleConfig.getSingleton().setProperty(LOCALSTORE_KEY, LOCALSTORE_AGSL);
      
      Reader reader = new InputStreamReader(VoSpaceResolver.resolveInputStream(Account.ANONYMOUS.toUser(), new Ivorn("ivo://"+LOCALSTORE_KEY+"#"+filename)));
      StringWriter writer = new StringWriter();
      Piper.pipe(reader, writer);

      assertSame(testContents, writer.toString());
      
   }

   /** Tests that the softwired ivorn -> agsl for auto-integration works OK */
   public void testAutoIntMySpaceResolver() throws IOException, URISyntaxException {
      Agsl agsl = VoSpaceResolver.resolveAgsl(new Ivorn("ivo://"+VoSpaceResolver.AUTOINT_MYSPACE_IVOKEY));
      
      assertEquals(agsl, new Agsl(VoSpaceResolver.AUTOINT_MYSPACE_AGSL));

      agsl = VoSpaceResolver.resolveAgsl(new Ivorn("ivo://"+VoSpaceResolver.AUTOINT_MYSPACE_IVOKEY+"#frog/results.txt"));
      
      assertTrue(agsl.toString().equals(new Agsl(VoSpaceResolver.AUTOINT_MYSPACE_AGSL+"#frog/results.txt").toString()));
   
   }

   public static void main(String[] args)  {
      junit.textui.TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(VoSpaceResolverTest.class);
   }

}

/*
 $Log: VoSpaceResolverTest.java,v $
 Revision 1.6  2004/04/22 08:58:35  mch
 Fixes to tests etc

 Revision 1.5  2004/04/21 10:35:29  mch
 Fixes to ivorn/fragment resolving

 Revision 1.4  2004/04/21 09:42:02  mch
 Added softwired shortcut for auto-integration tests

 Revision 1.3  2004/03/22 12:10:04  mch
 Fixed tests for new StoreClient  methods

 Revision 1.1  2004/03/16 01:34:14  mch
 Added resolver tester

 Revision 1.3  2004/03/08 13:45:57  mch
 Fixed imports

 Revision 1.2  2004/03/02 01:25:39  mch
 Minor fixes

 Revision 1.1  2004/03/01 22:35:09  mch
 Tests for StoreClient

 Revision 1.2  2003/12/03 17:41:10  mch
 Fix to get correct suite

 Revision 1.1  2003/12/03 17:39:25  mch
 New factory/interface based myspace delegates

 Revision 1.1  2003/12/02 18:03:53  mch
 Moved MySpaceDummyDelegate

 */

