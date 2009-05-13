/*
 * $Id: Slinger.java,v 1.1 2009/05/13 13:20:40 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.io.*;

import java.net.URISyntaxException;
import java.net.URL;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.piper.PipeListener;
import org.astrogrid.io.piper.StreamPiper;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sources.StringSource;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;
import org.astrogrid.slinger.targets.StreamTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.WriterTarget;


/**
 * Connects sources and targets and pipes between them. Like StreamPiper but deals
 * with SourceIdentifiers and TargetIdentifiers.
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
   public static void testConnection(TargetIdentifier target) throws IOException {

      // test to see that the agsl for the results is valid
      if ( (target instanceof UrlSourceTarget) ) {

         try {
            Writer out = target.openWriter();
//          out.write("This is a connection test");
            out.close();
         }
         catch (IOException ioe) {
            //rethrow with more info
            throw renewIOException(", writing to '"+target+"'", ioe);
         }
      }
   }

   /** Convenience routine for creating IOExceptions from existing IOExceptions and
    * a message and throwing them.  This compensates for the lack of IOException(msg, cause)
    * constructor, which can make rethrowing IOExceptions with extra information
    * rather tedious. The stack trace here will be the same as the original exception */
   public static IOException renewIOException(String appendMsg, IOException ioe) throws IOException {
      IOException newIoe = new IOException(ioe.getMessage()+appendMsg);
      newIoe.setStackTrace(ioe.getStackTrace());
      return newIoe;
   }

   /** Convenience routine for creating IOExceptions from other exceptions as 'causes'.
    * This compensates for the lack of IOException(msg, cause)
    * constructor, which can make rethrowing IOExceptions with extra information
    * rather tedious. The stack trace here will be the same as the original exception */
   public static IOException newIOException(String appendMsg, Exception cause)  {
      IOException newIoe = new IOException(appendMsg);
      newIoe.initCause(cause);
      return newIoe;
   }
   
   /** Pipes source to target <i>through this machine</i>.  More sophisticated
    * remote slings (ie, where this machine tells another server to get the
    * data direct) will require a different interface :-)
    * */
   public static void sling(SourceIdentifier source, TargetIdentifier target) throws IOException {
      
      target.setMimeType(source.getMimeType());
      
      InputStream in = new BufferedInputStream(source.openInputStream());
      OutputStream out = new BufferedOutputStream(target.openOutputStream());
      
      StreamPiper piper = new StreamPiper();
      piper.pipe(in, out, null);

      in.close();
      out.close();
   }

   /** Spawns a pipe to do the equivelent of 'sling' to read all the data from
    * the source and send it to the target.  Returns the StreamPiper that is
    * doing the work, so that you can cancel it if need be.
    * */
   public static StreamPiper spawnSling(SourceIdentifier source, TargetIdentifier target, PipeListener listener) throws IOException {
      
      target.setMimeType(source.getMimeType());
      
      InputStream in = new BufferedInputStream(source.openInputStream());
      OutputStream out = new BufferedOutputStream(target.openOutputStream());
      
      return StreamPiper.spawnPipe(in, out, listener, StreamPiper.DEFAULT_BLOCK_SIZE);
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
   
   /** Command line use & debug */
   public static void main(String[] args) throws IOException, URISyntaxException
   {
//      ConfigFactory.getCommonConfig().setProperty("org.astrogrid.registry.query.endpoint", "http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery");
      
      if (args.length<=1) {
//         printHelp();
         URL id = new URL("homespace:DSATEST1@uk.ac.le.star#MartinsTestTree.txt");
         System.out.println("Testing out...");
         Slinger.sling(new StringSource("Some text"), new UrlSourceTarget(id));
         System.out.println("...Reading back...");
         StringWriter sw = new StringWriter();
         Slinger.sling(new UrlSourceTarget(id), new WriterTarget(sw));
         System.out.println("...Done: "+sw.toString());
      }
      else if (args[0].trim().toLowerCase().equals("get")) {
         SourceIdentifier source = new UrlSourceTarget(new URL(args[1]));
         TargetIdentifier target = new StreamTarget(System.out);
         sling(source, target);
      }
      else {
         if (args.length<=2) {
            printHelp();
         }
         else if (args[0].trim().toLowerCase().equals("copy")) {
            SourceIdentifier source = new UrlSourceTarget(new URL(args[1]));
            TargetIdentifier target = new UrlSourceTarget(new URL(args[2]));
            sling(source, target);
         }
         else if (args[0].trim().toLowerCase().equals("send")) {
            SourceIdentifier source = new StringSource(args[1]);
            TargetIdentifier target = new UrlSourceTarget(new URL(args[2]));
            sling(source, target);
         }
         else {
            printHelp();
         }
      }
       /**/
   }
}

/*
 $Log: Slinger.java,v $
 Revision 1.1  2009/05/13 13:20:40  gtr
 *** empty log message ***

 Revision 1.2  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.1.2.1  2006/09/11 11:40:46  kea
 Moving slinger functionality back into DSA (but preserving separate
 org.astrogrid.slinger namespace for now, for easier replacement of
 slinger functionality later).

 Revision 1.5  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.4.10.2  2005/05/13 10:12:55  mch
 'some fixes'

 Revision 1.4.10.1  2005/04/21 17:09:03  mch
 incorporated homespace etc into URLs

 Revision 1.4  2005/03/28 01:48:09  mch
 Added socket source/target, and makeFile instead of outputChild

 Revision 1.3  2005/03/24 17:53:44  mch
 Added checks for preventing local disk access

 Revision 1.2  2005/03/23 15:29:42  mch
 added command-line Slinger, rationalised copy, send, get etc

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp



 

 */







