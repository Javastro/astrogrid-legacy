/*
 * $Id: HomespaceName.java,v 1.3 2005/01/26 17:31:57 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.slinger.vospace;

import java.io.*;

import java.net.URISyntaxException;
import java.security.Principal;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.slinger.SRI;
import org.astrogrid.slinger.SRL;
import org.astrogrid.slinger.StoreException;
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

public class HomespaceName implements SRI, TargetIdentifier, SourceIdentifier
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
      return toURI();
   }

   /** Returns the URI representation */
   public String toURI() {
      if (path == null) {
         return SCHEME+":"+accountName;
      } else {
         return SCHEME+":"+accountName+"#"+path;
      }
   }

   /** Resolves the location */
   public String toLocation(Principal user) throws IOException {
      return resolveIvosrn().resolveSrl().toURI();
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
      return resolveIvosrn().resolveInputStream(user);
   }
   
   /** Used to set the mime type of the data about to be sent to the target.  */
   public String getMimeType(Principal user) throws IOException {
      return resolveIvosrn().getMimeType(user);
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
      return resolveIvosrn().resolveOutputStream(user);
   }
   
   /** Used to set the mime type of the data about to be sent to the target.  */
   public void setMimeType(String aMimeType, Principal user) throws IOException {
      resolveIvosrn().setMimeType(aMimeType, user);
   }

   /**
    * Returns the ivosrn to this 'homespace'
    */
   public IVOSRN resolveIvosrn() throws IOException {

      //used for debug/user info - says somethign about where the ivorn has been looked for
      String lookedIn = "";

      //create backwards compatible ivorn-syntax homespace, as community still expects the form "ivo://community/individual#path"
      int atIdx = getName().indexOf("@");
      if (atIdx == -1) {
         throw new IllegalArgumentException("homespace "+this+" should be of the form homespace:<individual>@<community>[#path]");
      }
      String individualId = getName().substring(0,atIdx);
      String communityId = getName().substring(atIdx+1);
      org.astrogrid.store.Ivorn oldHomespaceId = new org.astrogrid.store.Ivorn(communityId, individualId, getPath());

      //look up in config first
      String key = "homespace."+getName();
      String value = SimpleConfig.getSingleton().getString(key, null);
      lookedIn += "Config (key=homespace."+getName()+") ";
      if (value != null) {
         if (IVOSRN.isIvorn(value)) {
            try {
               return new IVOSRN( new IVOSRN(value), getPath());
            }
            catch (URISyntaxException use) {
               throw new ConfigException(use+" config value for homespace key "+key+" is not a valid IVORN: "+value);
            }
         }
         else {
            throw new ConfigException("Config value for homespace key '"+key+"' is not an IVORN: "+value);
         }
      }

      //look up old id in config
      value = SimpleConfig.getSingleton().getString(oldHomespaceId.getPath(), null);
      lookedIn += "Config (key="+oldHomespaceId.getPath()+") ";
      if (value != null) {
         if (value.startsWith("ivo:")) {
            try {
               return new IVOSRN( new IVOSRN(value), getPath());
            }
            catch (URISyntaxException use) {
               throw new ConfigException(use+" config value for homespace key "+key+" is not a valid IVORN: "+value);
            }
         }
         else {
            throw new ConfigException("Config value for homespace key '"+key+"' is not a valid IVORN: "+value);
         }
      }
      
      
      //lazy load delegate - also more robust in case it doesn't instantiate
      CommunityAccountSpaceResolver communityResolver = new CommunityAccountSpaceResolver();

      try {
//         Ivorn homespaceIvorn = community.resolve(homespace);
         lookedIn += "Community ("+communityResolver+") ";
         IVOSRN storepoint = new IVOSRN(new IVOSRN(communityResolver.resolve(oldHomespaceId).toString()), getPath());
         return storepoint;
      }
      catch (URISyntaxException use) {
         throw new StoreException(use+" from IVORN returned by community delegate for "+oldHomespaceId);
      }
      catch (CommunityResolverException cre) {
         //don't ignore this - if it is trying to be resolved, use the config to do shortcut that
         throw new StoreException(cre+" resolving "+this+" ("+oldHomespaceId+") from community "+communityResolver+", looked in "+lookedIn,cre);
      }
      catch (CommunityException ce) {
         throw new StoreException(ce+" resolving "+this+" ("+oldHomespaceId+") from community "+communityResolver+", looked in "+lookedIn,ce);
      }
      catch (RegistryException re) {
         throw new StoreException(re+" resolving "+this+" ("+oldHomespaceId+") from community "+communityResolver+", looked in "+lookedIn,re);
      }
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

      IVOSRN ivosrn = name.resolveIvosrn();
      System.out.println("->"+ivosrn);
      
      SRL msrl = ivosrn.resolveSrl();
      System.out.println("->"+msrl);
      
      OutputStream out = ((MSRL) msrl).resolveOutputStream(LoginAccount.ANONYMOUS);
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
Revision 1.3  2005/01/26 17:31:57  mch
Split slinger out to scapi, swib, etc.

Revision 1.1.2.5  2005/01/26 14:35:36  mch
Separating slinger and scapi

Revision 1.1.2.4  2004/12/08 18:37:11  mch
Introduced SPI and SPL

Revision 1.1.2.3  2004/12/06 21:03:16  mch
Fixes to resolving homespace

Revision 1.1.2.2  2004/12/06 02:39:58  mch
a few bug fixes

Revision 1.1.2.1  2004/12/03 11:50:19  mch
renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

Revision 1.1.2.1  2004/11/22 00:46:28  mch
New Slinger Package


 */


