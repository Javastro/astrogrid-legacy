/*
 * $Id: Slinger.java,v 1.3 2005/01/26 17:31:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.Principal;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.io.Piper;
import org.astrogrid.slinger.myspace.MSRL;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.EmailTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.slinger.targets.UrlTarget;
import org.astrogrid.slinger.vospace.IVORN;

/**
 * Something to do with sending things. Not really properly thought out yet
 *
 */

public class Slinger  {

   /**
    * Tests the destination exists and a file can be created on it.  This ensures
    * that the given server url is correct, that the server is running and that
    * the user has the right permissions on that particular one.
    * We do this
    * before, eg, running the query to try and ensure the query is not wasted.
    *
    * @throws IOException if the operation fails for any reason
    */
   public static void testConnection(TargetIdentifier target, Principal user) throws IOException {
      
      if (target instanceof EmailTarget) {
         ((EmailTarget) target).testServer();
      }

      // test to see that the agsl for the results is valid
      if ( (target instanceof UrlTarget) || (target instanceof IVORN) || (target instanceof MSRL) ) {

         Writer out = target.resolveWriter(user);

         try {
            out.write("This is a test file to make sure we can create a file at the target before we start, so our query results are not lost");
         }
         catch (IOException se) {
            //rethrow with more info
            throw new IOException("Test to write to '"+target+"' failed: "+se.getMessage());
         }
         //erm
         /*
         try {
            store.delete(target.resolveAgsl().getPath());
         }
         catch (StoreException se) {
            //log it but don't fail
            LogFactory.getLog(Slinger.class).error("Could not delete test file",se);
         }
          */
      }
   }

   /** Pipes source to target.  What this *should* do is where appropriate,
    * forward the source to the target (or v.v).  For example, myspace can
    * directly access URLs so that the sling doesn't have to happen through
    * this machine
    * */
   public static void sling(SourceIdentifier source, TargetIdentifier target, Principal user) throws IOException {
      Piper.bufferedPipe(source.resolveInputStream(user), target.resolveOutputStream(user));
   }
   
   /** For quick tests/debug */
   public static void main(String[] args) throws IOException, IOException, URISyntaxException
   {
      Slinger.testConnection(TargetMaker.makeTarget("myspace:http://katatjuta.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager#frog/votable/martinPlayingWith6dF"), LoginAccount.ANONYMOUS);
   }
}
/*
 $Log: Slinger.java,v $
 Revision 1.3  2005/01/26 17:31:56  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.4  2004/12/03 11:50:19  mch
 renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

 Revision 1.1.2.3  2004/11/29 19:30:13  mch
 Some fixes to myspace source resolving, and moved loginaccount to a new directory

 Revision 1.1.2.2  2004/11/26 18:20:47  mch
 Made more file choosery, attempted to do picking thingy

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 Revision 1.1.2.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.5  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.4  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.3.8.1  2004/11/02 21:51:54  mch
 Replaced AgslTarget with UrlTarget and MySpaceTarget

 Revision 1.3  2004/10/08 15:16:45  mch
 Removed unnecessary imports

 Revision 1.2  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.1  2004/09/07 01:39:27  mch
 Moved email keys from TargetIndicator to Slinger

 
 */



