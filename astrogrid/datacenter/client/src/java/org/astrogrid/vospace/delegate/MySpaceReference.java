/*
 * $Id: MySpaceReference.java,v 1.1 2004/02/15 23:16:06 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.vospace.delegate;


/**
 * Relper methods for processing myspace references.
 *
 * A myspace reference is a madeup reference (by mch) to temporarily deal with
 * the fact that ivo locators (eg ivo://me@roe/asd/fasdf) are not resolvable
 * without a registry, and that otherwise we have to cope with two strings (a
 * reference to a myspace server and a reference to the file that that server
 * can resolve).
 *
 * Instead, we can use this format:
 *
 * myspace://delegateendpoint;individual@community/path/filename.ext
 *
 * which gives us everything we need to refer to a file in myspace, whether
 * a MySpaceManager or FTP etc, without recourse to a registry.
 *
 * Note that while this class is primarily for handling MySpace references
 * (as opposed to other VoSpace references, such as ftp) there are a few
 * methods for general locating help.
 *
 * @author mch
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.astrogrid.community.User;
import org.astrogrid.log.Log;

public class MySpaceReference
{
   /** Make a single myspace:// reference string out of a delegate endpoint
    * (ie a myspace manager service) and an ivo file reference
    */
   public static String makeMySpaceRef(String delegateEndpoint, String ivoRef)
   {
      //tidy up ivoRef
      ivoRef = ivoRef.replaceAll("\\\\", "/");
      if (ivoRef.toLowerCase().startsWith("ivo://")) {
         ivoRef = ivoRef.substring(6);
      }

      //tidy up service address - remove http (as it confuses uri) and endpoint
      if (delegateEndpoint.toLowerCase().startsWith("http://")) {
         delegateEndpoint = delegateEndpoint.substring(7);
      }

      return "myspace://"+delegateEndpoint+";"+ivoRef;
   }

   public static void assertValid(String myspaceRef)
   {
      assert myspaceRef != null : "MySpace Reference is null";
      assert myspaceRef.startsWith("myspace://") : "Does not start with 'myspace://'";
   }
   
   public static String getDelegateEndpoint(String myspaceRef)
   {
      if ((myspaceRef == null) || (myspaceRef.trim().length() == 0)) {
         return null;
      }
      assertValid(myspaceRef);
          
      return "http://"+myspaceRef.substring(10, myspaceRef.indexOf(";"));
   }
   
   /**
    * Takes a url or myspace reference, returns string representation of delegate
    * endpoint
    */
   public static String getDelegateRef(String ref) throws MalformedURLException
   {
      if (isMySpaceRef(ref))
      {
         return getDelegateEndpoint(ref);
      }
      URL url = new URL(ref);
      String delref = url.getProtocol()+"://"+url.getHost();
      if (url.getPort() > -1)
      {
         delref = delref+":"+url.getPort();
      }
      return delref;
   }
   
   /**
    * Returns whatever is required by the delegate to refer to the file.
    * This is currently /individual@community/path/path/filename.ext
    */
   public static String getDelegateFileRef(String ref) throws MalformedURLException {
      
      if (isMySpaceRef(ref))  {
         return getIvoRef(ref).substring(5);
      }
      else {
         URL url = new URL(ref);
         
         return url.getPath().substring(1);
      }
      
   }
   
   /** Returns the IVO reference to the file */
   public static String getIvoRef(String myspaceRef)
   {
      return "ivo:/"+myspaceRef.substring(myspaceRef.indexOf(";")+1);
   }
   
   /** Returns the path part of the reference - ie the list of directories
    * following the individual@community
    */
   public static String getPath(String myspaceRef)
   {
      String ivoRef = getIvoRef(myspaceRef);
      int slash = ivoRef.indexOf("/",7);
      return ivoRef.substring(slash);
   }
      
   /** Returns the file part of the reference - ie the text after the last slash
    */
   public static String getFilename(String myspaceRef)
   {
      String path = getPath(myspaceRef);
      int slash = path.lastIndexOf("/");
      return path.substring(slash+1);
   }
   
   /** Returns true if it's a valid myspace reference */
   public static boolean isValid(String myspaceRef)
   {
      try {
         assertValid(myspaceRef);
         return true;
      } catch (AssertionError ad) {} //ignore
      return false;
   }
   
   /** Returns true if it's an attempt to be a myspace reference */
   public static boolean isMySpaceRef(String myspaceRef)
   {
      return myspaceRef.toLowerCase().startsWith("myspace://");
   }
   
   /** Opens an inputstream to the file.  Just like url.openStream()....
    * Should work with both urls and myspace references
    */
   public static InputStream openStream(String myspaceRef) throws IOException {
      URL url = null;
      
      //if it's myspace get the url to it
      if (MySpaceReference.isMySpaceRef(myspaceRef)) {
         VoSpaceClient myspace = VoSpaceDelegateFactory.createDelegate(User.ANONYMOUS, MySpaceReference.getDelegateEndpoint(myspaceRef));
         String myspacePath = MySpaceReference.getDelegateFileRef(myspaceRef);
         
         url = myspace.getUrl(myspacePath);
         
         Log.logInfo("Loc '"+myspaceRef+"' -> path '"+myspacePath+"' -> URL '"+url+"'");
      }
      else {
         url = new URL(myspaceRef);
      }
      
      return url.openStream();
   }
}

/*
$Log: MySpaceReference.java,v $
Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

