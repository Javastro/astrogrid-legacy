/*
 * $Id: FML.java,v 1.2 2005/03/28 01:48:09 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */


package org.astrogrid.slinger.agfm;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import org.astrogrid.slinger.SRL;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.TargetIdentifier;

/**
 * (AstroGrid) FileManager Resource Locator.  A FileManager-specific way of identifying a file (or
 the location to create a file).  Note that this is a locator.
 * <p>
 * It is of the form:
 * <pre>
 *    managedfile:{serviceendpoint}[#{path}[!{storeId}]]
 * </pre>
 * <p>
 * ...and it is not yet finished.  It needs to use the low level FileManager delegates to
 * access the service; for now use FileManagerId.
*/


public class FML implements SRL, TargetIdentifier, SourceIdentifier
{
   private URL serviceEndpoint = null;
   private String filepath = null;
   
   /** When set it identifies which filestore the manager should look at */
   private String storeId = null;
   
   public static final String SCHEME = "fml";
   
   //for error messages
   public static final String FORM = SCHEME+":<serviceEndPoint>[#<Path>[!<Store>]]";
   
   /** Make a single reference out of a service endpoint */
   public FML(URL aServiceEndpoint)
   {
      this.serviceEndpoint = aServiceEndpoint;
   }

   /** Make a single reference out of a service endpoint
    * and a path to the file on that service  */
   public FML(URL aServiceEndpoint, String aServerPath)
   {
      this.serviceEndpoint = aServiceEndpoint;
      this.filepath = aServerPath;
   }
   
   /** Make a single reference out of a service endpoint
    * (eg a myspace manager service) and a path to the file.  In this case
    * we also define explicitly which filestore to use */
   public FML(URL aServiceEndpoint, String aServerPath, String aStore)
   {
      this.serviceEndpoint = aServiceEndpoint;
      this.filepath = aServerPath;
      this.storeId = aStore;
   }

   /** Make a reference from the given string representation
    */
   public FML(String fmrl) throws MalformedURLException {
      
//if assertions are off this won't get checked      assert fmrl.toLowerCase().startsWith(SCHEME+":") : fmrl+" is not a FileManager RL - should be of the form "+FORM;

      //?? the above doesn't seem to work on twmberlwm; do ordinary check
      if (!fmrl.toLowerCase().startsWith(SCHEME+":")) {
         throw new IllegalArgumentException(fmrl+" is not a FileManager RL - should be of the form "+FORM);
      }
      
      //look for path + server fragment
      int hashPos = fmrl.indexOf('#');
      if (hashPos > -1) {
         String fragment = fmrl.substring(hashPos+1);
         fmrl = fmrl.substring(0,hashPos);
         
         //look for server id
         int atPos = fragment.indexOf("!");
         if (atPos > -1) {
            String givenServer = fragment.substring(atPos+1);
            fragment = fragment.substring(0,atPos);
            
            this.storeId = givenServer;
         }
         
         this.filepath = fragment;
      }

      //look for whether start is an Ivorn or URL
      String rl = fmrl.substring(SCHEME.length()+1);
      //assume URL
      this.serviceEndpoint = new URL(rl);
   }

   /**
    * This string must be reversable through the above constructor, ie for valid msrl string s:
    *   new Msrl(s).toString().equals(s);
    * must be true.
    */
   public String toString() {
      return toURI();
   }

   /** Returns URI instance of locator */
   public String toURI()  {
      String msrl = SCHEME+":"+serviceEndpoint;
      if (filepath != null) { msrl = msrl +"#"+filepath; }
      if (storeId != null) { msrl = msrl +"!"+storeId; }
      return msrl;
   }
   
   /** Returns just the delegate end point */
   public URL getserviceEndpoint()   {   return serviceEndpoint;   }
   
   /** Returns the myspace filepath */
   public String getPath()             {  return filepath; }
   
   /** Returns the server ID */
   public String getServer()           { return storeId; }
      
   /** Returns the name part of the reference - ie the last token broken by
    * slashes.  If the last character is a slash, this indicates a directory
    * and the previous token is returned
    */
   public String getFilename()
   {
      String path = getPath();

     if (path != null) { //might refer to a server, ie no path
         if (path.endsWith("/")) {
            path = path.substring(0,path.length()-1); //chop off last slash
         }
         int slash = path.lastIndexOf("/");
         return path.substring(slash+1);
      }
      else {
         return null;
      }
   }

   /** Returns true if the given string is an attempt to be a vospace reference */
   public static boolean isFmrl(String fmrl)
   {
      return fmrl.toLowerCase().startsWith(SCHEME+":");
   }

   public OutputStream resolveOutputStream(Principal user) throws IOException {
      throw new UnsupportedOperationException();
   }

   
   /** Used to set the mime type of the data about to be sent to the target. */
   public void setMimeType(String aMimeType, Principal user) {
//@todo      this.mimeType = aMimeType;
   }

   public InputStream resolveInputStream(Principal user) throws IOException {
      throw new UnsupportedOperationException();
   }
   
   public Reader resolveReader(Principal user) throws IOException {
      return new InputStreamReader(resolveInputStream(user));
   }
   
   public Writer resolveWriter(Principal user) throws IOException {
      return new OutputStreamWriter(resolveOutputStream(user));
   }
   
   /** Used to set the mime type of the data about to be sent to the source. Does nothing. */
   public String getMimeType(Principal user) throws IOException {
      //@todo
      return null;
   }
   

}

/*

$Log: FML.java,v $
Revision 1.2  2005/03/28 01:48:09  mch
Added socket source/target, and makeFile instead of outputChild

Revision 1.1  2005/03/15 12:07:28  mch
Added FileManager support


 */



