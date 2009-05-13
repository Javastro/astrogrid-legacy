/*
 * $Id: HomespaceName.java,v 1.1 2009/05/13 13:20:41 gtr Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.slinger.homespace;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.account.LoginAccount;

/**
 * Homespace name, of the form homespace:<<acountname>>#path to file.
 * eg homespace:mch@roe.ac.uk/queriers/6dfQuery.adql.  A 'homespace' is
 * a way of identifying a file by the user/account; so that someone can say 'in
 * my home space at this path', and the physical/net location of that homespace
 * can be moved around.
 *
 * @author MCH, KMB, KTN, DM, ACD
 */

public class HomespaceName
{
   public final static String SCHEME = "homespace";
   public final static String FORM = "homespace:individual@community/path/path/file.ext";
   
   
   private String accountName = null;
   private String path = null;
   
   /** Construct from given string */
   public HomespaceName(String homespaceName) throws URISyntaxException
   {
      if (!homespaceName.startsWith(SCHEME+":")) {
         throw new URISyntaxException(homespaceName, "Scheme should be "+SCHEME+":");
      }

      //chop off scheme & initial slashes if any
      String s = homespaceName.substring(SCHEME.length()+1);
      if (s.startsWith("//")) {
         s = s.substring(2); //chop off double slashes, which can appear when using URLs
      }

      //look for path component given by slash
      int pathIdx = s.indexOf("/");
      if (pathIdx == -1) {
         //no path component - ensure there's no hash which might get used since ivorns use 'em
         if (s.indexOf("#")>-1) {
            throw new URISyntaxException(homespaceName, "has a hash at "+s.indexOf("#")+", should be of the form "+FORM);
         }
         setAccountName(s);
      }
      else {
         if ((s.indexOf("#")>-1) && (s.indexOf("#")<pathIdx)) {
            throw new URISyntaxException(homespaceName, "has a hash at "+s.indexOf("#")+", should be of the form "+FORM);
         }
         setAccountName(s.substring(0,pathIdx));
         setPath(s.substring(pathIdx));
      }
   }
   
   /** Construct from given account name and path */
   public HomespaceName(String anAccountName, String aPath)
   {
      setAccountName(anAccountName);
      setPath(aPath);
   }
   
   /** Construct from a Principle's homespace and a path on it*/
   public HomespaceName(Principal aUser, String aPath)  {

      setAccountName(aUser.getName());
      setPath(aPath);
   }

   /** Returns the homespace name from the given IVORN form used by most astrogrid applications 
    * KONA Jan 2008 Note:  Myspace destination ivorns currently have the form:
    * ivo://org.astrogrid.regtest/community/KonaAndrews#test_folder/new_file0.vot
    * NOTE:  Don't think this stuff is actively in use now.
    */
   public static HomespaceName fromIvorn(String ivorn) {
      int hashIdx = ivorn.indexOf("#");
      String key = ivorn.substring(0,hashIdx).substring(6);
      //int slashIdx = key.indexOf("/");  // MIGHT BE MORE THAN ONE NOW!
      int slashIdx = key.lastIndexOf("/");

      String ind = key.substring(slashIdx+1); //individual
      String com = key.substring(0,slashIdx); //community
      //return new HomespaceName(ind+"@"+com+"#", ivorn.substring(hashIdx+1));
      return new HomespaceName(ind+"@"+com, ivorn.substring(hashIdx+1));
   }

   /** Returns the reference in IVORN form as most astrogrid applications still use that */
   public String toIvorn() {
      if (getPath() == null) {
         return "ivo://"+getCommunity()+"/"+getIndividual();
      }
      return "ivo://"+getCommunity()+"/"+getIndividual()+"#"+getPath().substring(1); //remove initial slash from path
   }
   
   /** Sets account name property.  Account name should include an '@' sign at the moment, ie
    {individual}@{community}... */
   public void setAccountName(String name) {
      int atIdx = name.indexOf("@");
      if ((atIdx == -1) || (atIdx == 0) || (atIdx == name.length()-1)) {
         throw new IllegalArgumentException("Account name '"+name+"' should be of the form {individual}@{community}");
      }
      this.accountName = name;
   }

   /** Sets path.  Paths are absolute in homespace */
   public void setPath(String aPath) {
      if ((aPath == null) || (aPath.startsWith("/"))) {
         path = aPath;
      }
      else {
         path = "/"+aPath;
      }
   }
         
   
   /** Returns identifier scheme */
   public String getScheme() {      return SCHEME;  }

   /** Returns path to file on server
    */
   public String getPath() {      return path;   }
   
   /** Returns name of homespace - ie Principal name */
   public String getAccountName() {     return accountName; }

   /** Returns individual's name */
   public String getIndividual() {
      return accountName.substring(0,accountName.indexOf("@"));
   }
   
   /** Returns community's name */
   public String getCommunity() {
      return accountName.substring(accountName.indexOf("@")+1);
   }

   /** String representation */
   public String toString() {
      return toURI();
   }
   
   /** Returns the URI representation */
   public String toURI() {
      if (path == null) {
         return SCHEME+":"+accountName;
      } else {
         return SCHEME+":"+accountName+path;
      }
   }

   /** Returns true if the given string is likely to be a homespace - ie if it
    * starts with 'homespace:'
    */
   public static boolean isHomespaceName(String aString) {
      return aString.toLowerCase().startsWith(SCHEME+":");
   }

   /** Returns true if the given string is likely to be a myspace Ivorn - 
    * ie if it * starts with 'ivo'
    */
   public static boolean isMyspaceIvorn(String aString) {
      return aString.toLowerCase().startsWith("ivo:");
   }

}

/*
$Log: HomespaceName.java,v $
Revision 1.1  2009/05/13 13:20:41  gtr
*** empty log message ***

Revision 1.3  2008/02/07 17:27:45  clq2
PAL_KEA_2518

Revision 1.2.34.1  2008/02/07 16:36:16  kea
Further fixes for 1.0 support, and also MBT's changes merged into my branch.

Revision 1.2  2006/09/26 15:34:42  clq2
SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

Revision 1.1.2.1  2006/09/11 11:40:46  kea
Moving slinger functionality back into DSA (but preserving separate
org.astrogrid.slinger namespace for now, for easier replacement of
slinger functionality later).

Revision 1.2  2005/05/27 16:21:02  clq2
mchv_1

Revision 1.1.2.2  2005/05/17 09:11:55  mch
added fromIvorn

Revision 1.1.2.1  2005/05/13 17:02:04  mch
made homespace and agfm available both inside and outside URL plugin

Revision 1.1.2.3  2005/05/13 10:12:55  mch
'some fixes'

Revision 1.1.2.2  2005/05/03 14:10:54  mch
slinger

Revision 1.1.2.1  2005/04/21 17:09:03  mch
incorporated homespace etc into URLs

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


