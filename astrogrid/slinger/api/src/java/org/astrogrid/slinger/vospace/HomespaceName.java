/*
 * $Id: HomespaceName.java,v 1.5 2005/03/31 09:04:12 mch Exp $
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
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.cfg.ConfigException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.slinger.SRI;
import org.astrogrid.slinger.SRL;
import org.astrogrid.slinger.StoreException;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.vospace.IVOSRN;
import org.astrogrid.slinger.agfm.FileManagerId;

/**
 * Homespace name, of the form homespace:<<acountname>>#path to file.
 * eg homespace:mch@roe.ac.uk#mch/queriers/6dfQuery.adql.  A 'homespace' is
 * a way of identifying a file by the user/account; so that someone can say 'in
 * my home space at this path', and the physical/net location of that homespace
 * can be moved around.
 *
 * @TODO NOTE VERY MUCH: Because the registry doesn't have a proper way of
 * distinguishing types of services, homespaces are ASSUMED to resolve to
 * FileManager services via a URI of ivo:// form.
 * @See FileManagerId.
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
         setAccountName(s);
      }
      else {
         setAccountName(s.substring(0,hashIdx));
         path = s.substring(hashIdx+1);
      }
   }
   
   /** Construct from given account name and path */
   public HomespaceName(String anAccountName, String aPath)
   {
      setAccountName(anAccountName);
      this.path = aPath;
   }
   
   /** Construct from a Principle and a path */
   public HomespaceName(Principal aUser, String aPath)  {

      setAccountName(aUser.getName());
      this.path = aPath;
   }

   /** Sets account name property.  Account name should include an '@' sign at the moment, ie
    {individual}@{community}... */
   public void setAccountName(String name) {
      if (name.indexOf("@") == -1) {
         throw new IllegalArgumentException("Account name should be of the form {individual}@{community}");
      }
      this.accountName = name;
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
      return ((SRL) resolveIvosrn().resolveTarget()).toURI();
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
      //what we should do return resolveIvosrn().resolveInputStream(user);
      return new FileManagerId(this).resolveInputStream(user);
   }
   
   /** Used to set the mime type of the data about to be sent to the target.  */
   public String getMimeType(Principal user) throws IOException {
      //what we should do return resolveIvosrn().getMimeType(user);
      return new FileManagerId(this).getMimeType(user);
   }
   
   /** Opens up an output stream to the homespace file, assuming that the file
    * is served by a FileManager.  */
   public OutputStream resolveOutputStream(Principal user) throws IOException {
      //what we should do return resolveIvosrn().resolveOutputStream(user);
      return new FileManagerId(this).resolveOutputStream(user);
   }
   
   /** Used to set the mime type of the data about to be sent to the target.  */
   public void setMimeType(String aMimeType, Principal user) throws IOException {
      //what we should do resolveIvosrn().setMimeType(aMimeType, user);
      new FileManagerId(this).setMimeType(aMimeType, user);
   }

   public Reader resolveReader(Principal user) throws IOException {
      return new InputStreamReader(resolveInputStream(user));
   }
   
   public Writer resolveWriter(Principal user) throws IOException {
      return new OutputStreamWriter(resolveOutputStream(user));
   }

   /**
    * Returns the registry-resolvable ivo resource name to this 'homespace'.  Looks first
    * in config file, and if not found there looks up the community server for the
    * accountname's community, which will return the IVORN to the storepoint.
    * Public so it can be used for displays
    */
   public IVOSRN resolveIvosrn() throws IOException {

      //used for debug/user info - says somethign about where the ivorn has been looked for
      String lookedIn = "";

      //create backwards compatible ivorn-syntax homespace, as community still expects the form "ivo://community/individual#path"

      //look up in config first
      String key = "homespace."+getName();
      String value = ConfigFactory.getCommonConfig().getString(key, null);
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

      IVORN ivoForm = toIvoForm();
      CommunityAccountSpaceResolver communityResolver = new CommunityAccountSpaceResolver();
//        Ivorn homespaceIvorn = community.resolve(homespace);
      lookedIn += "Community ("+communityResolver+") ";
      org.astrogrid.store.Ivorn oldHomespaceId = ivoForm.toOldIvorn();
      try {
         org.astrogrid.store.Ivorn homespaceService = communityResolver.resolve(oldHomespaceId);
         IVOSRN storepoint = new IVOSRN(new IVOSRN(homespaceService.toString()), getPath());
         return storepoint;
      }
      catch (URISyntaxException use) {
         throw new StoreException(use+" from IVORN returned by community delegate for "+oldHomespaceId);
      }
      catch (CommunityResolverException cre) {
         //don't ignore this - if it is trying to be resolved, use the config to do shortcut that
         throw new StoreException(cre+" resolving "+this+" ("+oldHomespaceId+") from community, looked in "+lookedIn,cre);
      }
      catch (CommunityException ce) {
         throw new StoreException(ce+" resolving "+this+" ("+oldHomespaceId+") from community, looked in "+lookedIn,ce);
      }
      catch (RegistryException re) {
         throw new StoreException(re+" resolving "+this+" ("+oldHomespaceId+") from communityb, looked in "+lookedIn,re);
      }
   }
   
   
   /** Returns the homespace id in the form of an IVO identifier. This is for backwards compatibility for
    * some AstroGrid components that still use IVO identifiers for things that aren't resolvable using
    * IVO components. */
   public IVOSRN toIvoForm() {

      int atIdx = getName().indexOf("@");
      if (atIdx == -1) {
         throw new IllegalArgumentException("homespace "+toString()+" should be of the form homespace:<individual>@<community>[#path]");
      }
      String individualId = getName().substring(0,atIdx);
      String communityId = getName().substring(atIdx+1);
      return new IVOSRN(communityId, individualId, getPath());
   }

   
   
   
   /**
    * quick tests/debugs
    */
   public static void main(String[] args) throws URISyntaxException, IOException
   {
      
      ConfigFactory.getCommonConfig().setProperty("org.astrogrid.registry.query.endpoint", "http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery");

      ConfigFactory.getCommonConfig().setProperty("homespace.agdemo1@org.astrogrid","ivo://astrogrid.org/myspace");
      
      HomespaceName name = new HomespaceName("homespace:agdemo1@org.astrogrid#/agdemo1/votable/MartinPlayingWithNamesYetAgain");
      System.out.println(name);

      IVOSRN ivosrn = name.resolveIvosrn();
      System.out.println("->"+ivosrn);
      
      name = new HomespaceName("homespace:guest04@uk.ac.le.star/votable/secresult1.vot");
      System.out.println(name);

      ivosrn = name.resolveIvosrn();
      System.out.println("->"+ivosrn);
      /*
//      SRL msrl = ivosrn.resolveSrl();
//      System.out.println("->"+ivosrnmsrl);
      
      OutputStream out = ((MSRL) msrl).resolveOutputStream(LoginAccount.ANONYMOUS);
      out.write("This could be rude and you'd never know cos it's about to be overwritten".getBytes());
      out.close();
      
      out = name.resolveOutputStream(LoginAccount.ANONYMOUS);
      out.write("Hello there everyone".getBytes());
      out.close();
      
      System.out.println("done");
       */
   }
}

/*
$Log: HomespaceName.java,v $
Revision 1.5  2005/03/31 09:04:12  mch
Added volist and some fixes to homespace resolvign

Revision 1.4  2005/03/28 01:48:09  mch
Added socket source/target, and makeFile instead of outputChild

Revision 1.3  2005/03/21 16:10:43  mch
Fixes to compile (including removing refs to FileManager clients)

Revision 1.2  2005/03/15 12:07:28  mch
Added FileManager support

Revision 1.1  2005/02/14 20:47:38  mch
Split into API and webapp

Revision 1.5  2005/01/27 18:53:16  mch
did toLocation

Revision 1.4  2005/01/26 17:41:48  mch
fix to compile until resolving is properly handled

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


