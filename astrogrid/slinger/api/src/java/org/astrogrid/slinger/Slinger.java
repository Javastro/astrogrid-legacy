/*
 * $Id: Slinger.java,v 1.3 2005/03/24 17:53:44 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.Principal;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.Piper;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sources.SourceMaker;
import org.astrogrid.slinger.sources.StringSource;
import org.astrogrid.slinger.targets.EmailTarget;
import org.astrogrid.slinger.targets.StreamTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.slinger.targets.UrlTarget;
import org.astrogrid.slinger.vospace.IVORN;


/**
 * Something to do with sending things. Not really properly thought out yet
 *
 */

public class Slinger  {

   /** Configuration setting to allow access to local files */
   public static final String PERMIT_LOCAL_ACCESS_KEY = "AllowLocalFileAccess";
   
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
      if ( (target instanceof UrlTarget) || (target instanceof IVORN) || (target instanceof SRL) ) {

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
      
      target.setMimeType(source.getMimeType(user), user);
      
      InputStream in = source.resolveInputStream(user);
      OutputStream out = target.resolveOutputStream(user);
      
      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
   }

   /** Convenience routine returns true if this application is configured to serve local
    * files */
   public static boolean allowLocalAccess() {
      return ConfigFactory.getCommonConfig().getBoolean(PERMIT_LOCAL_ACCESS_KEY, false);
   }
   
   
   public static void printHelp() {
      System.out.println("Using Slinger: ");
      System.out.println(" GET <id> ");
      System.out.println(" COPY <source ID>  <target ID>");
      System.out.println(" SEND <text> <target ID>");
   }
   
   /** For quick tests/debug */
   public static void main(String[] args) throws IOException, IOException, URISyntaxException
   {
//      Slinger.testConnection(TargetMaker.makeTarget("myspace:http://katatjuta.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager#frog/votable/martinPlayingWith6dF"), LoginAccount.ANONYMOUS);
      if (args.length<=1) {
         printHelp();
      }
      else if (args[0].trim().toLowerCase().equals("get")) {
         SourceIdentifier source = SourceMaker.makeSource(args[1]);
         TargetIdentifier target = new StreamTarget(System.out);
         sling(source, target, LoginAccount.ANONYMOUS);
      }
      else {
         if (args.length<=2) {
            printHelp();
         }
         else if (args[0].trim().toLowerCase().equals("copy")) {
            SourceIdentifier source = SourceMaker.makeSource(args[1]);
            TargetIdentifier target = TargetMaker.makeTarget(args[2]);
            sling(source, target, LoginAccount.ANONYMOUS);
         }
         else if (args[0].trim().toLowerCase().equals("send")) {
            SourceIdentifier source = new StringSource(args[1]);
            TargetIdentifier target = TargetMaker.makeTarget(args[2]);
            sling(source, target, LoginAccount.ANONYMOUS);
         }
         else {
            printHelp();
         }
      }
   }
}

/*
 $Log: Slinger.java,v $
 Revision 1.3  2005/03/24 17:53:44  mch
 Added checks for preventing local disk access

 Revision 1.2  2005/03/23 15:29:42  mch
 added command-line Slinger, rationalised copy, send, get etc

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp



 

 */







