/*
 * $Id: HomespaceName.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.slinger.vospace;

import java.io.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.slinger.StoreFile;
import org.astrogrid.slinger.StoreFileResolver;
import org.astrogrid.slinger.myspace.MSRL;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.vospace.IVOSRN;

/**
 * Homespace name, of the form homespace:<<acountname>>#path to file.
 * eg homespace:mch@roe.ac.uk#mch/queriers/6dfQuery.adql
 *
 * @author MCH, KMB, KTN, DM, ACD
 */

public class HomespaceName implements TargetIdentifier, SourceIdentifier
{
   public final static String SCHEME = "homespace";
   
   private String accountName = null;
   private String path = null;
   
   /** Construct from given string */
   public HomespaceName(String homespaceName) throws URISyntaxException
   {
      assert homespaceName.startsWith(SCHEME+":") : "Scheme should be "+SCHEME+":";

      String s = homespaceName.substring(SCHEME.length()+1);
      int hashIdx = s.indexOf("#");
      if (hashIdx == -1) {
         accountName = s;
      }
      else {
         accountName = s.substring(0,hashIdx);
         path = s.substring(hashIdx+1);
      }
   }
   
   /** Construct from given account name and path */
   public HomespaceName(String anAccountName, String aPath)
   {
      this.accountName = anAccountName;
      this.path = aPath;
   }
   
   /** Construct from a Principle and a path */
   public HomespaceName(Principal aUser, String aPath)  {

      this.accountName = aUser.getName();
      this.path = aPath;
   }
   
   /** Returns identifier scheme */
   public String getScheme() {      return SCHEME;  }

   /** Returns path to file on server
    */
   public String getPath() {      return path;   }
   
   /** Returns name of homespace - ie Principal name */
   public String getName() {     return accountName; }

   /** String representation */
   public String toString() {
      if (path == null) {
         return SCHEME+":"+accountName;
      } else {
         return SCHEME+":"+accountName+"#"+path;
      }
   }

   /** Returns the URI representation */
   public URI toUri() {
      try {
         return new URI(toString());
      }
      catch (URISyntaxException e) {
         //this should never happen as it shouldn't be possible to create an agsl that isn't
         throw new RuntimeException("Application error: "+e+" for HomespaceName "+toString());
      }
   }

   /** Resolves the location */
   public String toLocation(Principal user) throws IOException {
      return StoreFileResolver.resolveStoreFile(this, user).getUri();
   }
   
   /** Returns true if the given string is likely to be a homespace - ie if it
    * starts with 'homespace:'
    */
   public static boolean isHomespaceName(String aString) {
      return aString.toLowerCase().startsWith(SCHEME+":");
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public InputStream resolveInputStream(Principal user) throws IOException {
      return VoSpaceResolver.resolveInputStream(VoSpaceResolver.resolveIvosrn(this), user);
   }
   
   /** Used to set the mime type of the data about to be sent to the target.  */
   public String getMimeType(Principal user) throws IOException {
      return StoreFileResolver.resolveStoreFile(this, user).getMimeType();
   }
   
   public Reader resolveReader(Principal user) throws IOException {
      return new InputStreamReader(resolveInputStream(user));
   }
   
   public Writer resolveWriter(Principal user) throws IOException {
      return new OutputStreamWriter(resolveOutputStream(user));
   }

   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream resolveOutputStream(Principal user) throws IOException {
      return VoSpaceResolver.resolveOutputStream(this, user);
   }
   
   /** Used to set the mime type of the data about to be sent to the target.  */
   public void setMimeType(String aMimeType, Principal user) throws IOException {
      StoreFile file = StoreFileResolver.resolveStoreFile(this, user);
      file.setMimeType(aMimeType, user);
   }
   
   /**
    * quick tests/debugs
    */
   public static void main(String[] args) throws URISyntaxException, IOException
   {
      
      SimpleConfig.setProperty("org.astrogrid.registry.query.endpoint", "http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery");

      SimpleConfig.setProperty("homespace.agdemo1@org.astrogrid","ivo://astrogrid.org/myspace");
      
      HomespaceName name = new HomespaceName("homespace:agdemo1@org.astrogrid#/agdemo1/votable/MartinPlayingWithNamesYetAgain");
      System.out.println(name);

      IVOSRN ivosrn = VoSpaceResolver.resolveIvosrn(name);
      System.out.println("->"+ivosrn);
      
      MSRL msrl = VoSpaceResolver.resolveMsrl(ivosrn);
      System.out.println("->"+msrl);
      
      OutputStream out = msrl.resolveOutputStream(LoginAccount.ANONYMOUS);
      out.write("This could be rude and you'd never know cos it's about to be overwritten".getBytes());
      out.close();
      
      out = name.resolveOutputStream(LoginAccount.ANONYMOUS);
      out.write("Hello there everyone".getBytes());
      out.close();
      
      System.out.println("done");
   }
}

/*
$Log: HomespaceName.java,v $
Revision 1.2  2004/12/07 01:33:36  jdt
Merge from PAL_Itn07

Revision 1.1.2.3  2004/12/06 21:03:16  mch
Fixes to resolving homespace

Revision 1.1.2.2  2004/12/06 02:39:58  mch
a few bug fixes

Revision 1.1.2.1  2004/12/03 11:50:19  mch
renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

Revision 1.1.2.1  2004/11/22 00:46:28  mch
New Slinger Package


 */


