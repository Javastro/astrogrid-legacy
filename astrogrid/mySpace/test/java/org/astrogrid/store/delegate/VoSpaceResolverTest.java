/*
 * $Id: VoSpaceResolverTest.java,v 1.3 2004/03/22 12:10:04 mch Exp $
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

   public static final String ACCOUNT_FILE_IVORN = "ivo://roe.ac.uk/mch#mch@roe.ac.uk/famous/data/file.txt";

   public static final String ACCOUNT_FILE_AGSL = "astrogrid:store:myspace://grendel12.roe.ac.uk/something#mch@roe.ac.uk/famous/data/file.txt";
   
   public static final String LOCAL_MS_I = "ivo://localmyspace/file.txt";

                                             
   /** Tests that the shortcut works - ie that the local configuration can be used
    * to override registry/community resolving
    */
   public void testShortcut() throws IOException, MalformedURLException, URISyntaxException
   {
      // make sure it works
      SimpleConfig.getSingleton().setProperty(ACCOUNT_FILE_IVORN, ACCOUNT_FILE_AGSL);
      
      VoSpaceResolver resolver = new VoSpaceResolver();
      Agsl resolvedAgsl = resolver.resolveAgsl(new Ivorn(ACCOUNT_FILE_IVORN));
      
      assertEquals(new Agsl(ACCOUNT_FILE_AGSL), resolvedAgsl);
      
      // make sure it doesn't
      SimpleConfig.getSingleton().setProperty(ACCOUNT_FILE_IVORN, "Something wrong");
      
      resolvedAgsl = resolver.resolveAgsl(new Ivorn(ACCOUNT_FILE_IVORN));
      
      assertNotSame(new Agsl(ACCOUNT_FILE_AGSL), resolvedAgsl);
   }

   /**
    * Tests to see if we can resolve an input stream correctly */
   public void testFileResolve() throws IOException, URISyntaxException
   {
      String testContents = "A bit of text to make sure it gets created on the "+new Date();
      String filename = "resolverTestfile.txt";
      
      LocalFileStore store = new LocalFileStore();
      
      store.putString(testContents, filename, false);

      //add AGSL file location to configuration
      SimpleConfig.getSingleton().setProperty(LOCAL_MS_I, "astrogrid:store:file://"+filename);
      
      VoSpaceResolver resolver = new VoSpaceResolver();
      Reader reader = new InputStreamReader(resolver.resolveInputStream(Account.ANONYMOUS.toUser(), new Ivorn(LOCAL_MS_I)));
      StringWriter writer = new StringWriter();
      Piper.pipe(reader, writer);

      assertSame(testContents, writer.toString());
      
   }
   
   public static void main(String[] args) throws IOException {
      junit.textui.TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(VoSpaceResolverTest.class);
   }

}

/*
 $Log: VoSpaceResolverTest.java,v $
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

