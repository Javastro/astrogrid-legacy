/*
 * $Id: IVORN.java,v 1.2 2009/06/18 09:57:56 KevinBenson Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.slinger.ivo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.slinger.Slinger;

/**
 * International Virtual Observatory Resource Name.  An IVORN names a resource
 * and is resolved using Registries

 * @author MCH, KMB, KTN, DM, ACD
 */


public class IVORN
{
   public final static String SCHEME = "ivo";
   
   private String key = null;
   private String authority = null;
   private String fragment = null;
   
   /** Construct from given string */
   public IVORN(String ivorn) throws URISyntaxException
   {
      assert ivorn.startsWith(SCHEME+":") : "Scheme should be "+SCHEME+":";

      //separate authority, key & fragment
      URI uri = new URI(ivorn); //make use of URI parsing
      
      authority = uri.getAuthority();
      key = uri.getPath();
      setFragment(uri.getFragment());
   }
   
   /** Construct from given authority, key and fragment */
   public IVORN(String anAuthority, String aKey, String aFragment)
   {
      this.key = "/"+aKey;
      this.authority = anAuthority;
      setFragment(aFragment);
   }
   
   /** Create a new ivorn from an existing one and a new fragment.
    * If the existing one has a fragment, it is replaced in the constructed IVORN
    */
   public IVORN(IVORN aPath, String aFragment)  {

      this.authority = aPath.authority;
      this.key = aPath.key;
      setFragment(aFragment);
   }

   private void setFragment(String aFragment) {
      fragment = aFragment;
      //make sure empty fragments are also null for easier checking
      if ((fragment != null) && (fragment.trim().length()==0)) {
         fragment = null;
      }
   }
   
   /** Returns identifier scheme */
   public String getScheme() {      return SCHEME;  }

   /** Returns ivo id without the scheme.
    * eg 'ivo://roe.ac.uk/mch/myspace' returns 'roe.ac.uk/mch/myspace'
    */
   public String getPath() {
      if (key == null) {
         return authority;
      } else {
         return authority+key;
      }
   }

   public String getAuthority() { return authority; }
   
   public String getKey()       { return key; }
   
   /** Returns specific-to-service - ie fragment */
   public String getFragment()  { return fragment;   }

   /** String representation */
   public String toString() {
      return toURI();
   }

   /** Returns the IVORN in URI form */
   public String toURI() {
      if (fragment == null) {
         return SCHEME+"://"+getPath();
      } else {
         return SCHEME+"://"+getPath()+"#"+fragment;
      }
   }

   public boolean equals(Object anotherIvorn) {
      if (anotherIvorn instanceof IVORN) {
         return toURI().equals( ((IVORN) anotherIvorn).toURI());
      }
      return false;
   }
   
   /** Returns true if the given string is likely to be an ivorn - ie if it
    * starts with ivo://
    */
   public static boolean isIvorn(String aString) {
      return aString.toLowerCase().startsWith(SCHEME+"://");
   }
   
   /** For backwards compatibility, returns as the 'store' IVORN object from
    * the common project */
   public org.astrogrid.store.Ivorn toOldIvorn() {
      return new org.astrogrid.store.Ivorn(getAuthority(),
                                           getKey().substring(1), //remove initial slash
                                           getFragment());
   }
   
   /**
    * Given an IVO Resource Name, resolves a string using the local
    * config or Registry (ie this does not work with account ivorns - use Homespace), which will be some
    * kind of URI (and which may not be alocation - it may be another IVORN)
    */
   public String resolve() throws IOException {
      
      RegistryService registry = RegistryDelegateFactory.createQueryv1_0();

      //used for debug/Principal info - says somethign about where the ivorn has been looked for
      String lookedIn = "";
      
      //look up in config first
      String key = "ivorn."+getPath();
      String ms = ConfigFactory.getCommonConfig().getString(key, null);
      lookedIn += "Config (key="+key+") ";
      if (ms != null) {
         return ms;
      }
      
      //if not found, see if the registry can resolve it
      try {
         lookedIn += ", Registry "+registry;
         String resolvedEndPoint = registry.getEndPointByIdentifier(getPath());

         if (resolvedEndPoint == null) {
            //if not found, throw an exception
            throw new FileNotFoundException("Cannot resolve "+this+" from "+lookedIn);
         }

         if (getFragment() != null) {
            resolvedEndPoint = resolvedEndPoint+"#"+getFragment();
         }
         
         LogFactory.getLog(IVORN.class).trace("Registry-Resolved "+this+" to "+resolvedEndPoint+" (from "+registry+")");
         return resolvedEndPoint;
      }
      catch (RegistryException e) {
         throw Slinger.newIOException(e+" getting endpoint for '"+this+"' from "+registry,e);
      }
   }

}

/*
$Log: IVORN.java,v $
Revision 1.2  2009/06/18 09:57:56  KevinBenson
changed createQuery to createQueryv1_0 because createQuery is no longer used in registry client.

Revision 1.1.1.1  2009/05/13 13:20:41  gtr


Revision 1.2  2006/09/26 15:34:42  clq2
SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

Revision 1.1.2.1  2006/09/11 11:40:46  kea
Moving slinger functionality back into DSA (but preserving separate
org.astrogrid.slinger namespace for now, for easier replacement of
slinger functionality later).

Revision 1.2  2005/05/27 16:21:02  clq2
mchv_1

Revision 1.1.2.1  2005/05/13 17:02:04  mch
made homespace and agfm available both inside and outside URL plugin

Revision 1.1.2.1  2005/04/21 17:09:03  mch
incorporated homespace etc into URLs

Revision 1.4  2005/03/28 01:48:09  mch
Added socket source/target, and makeFile instead of outputChild

Revision 1.3  2005/03/21 16:10:43  mch
Fixes to compile (including removing refs to FileManager clients)

Revision 1.2  2005/03/15 12:07:28  mch
Added FileManager support

Revision 1.1  2005/02/14 20:47:38  mch
Split into API and webapp

Revision 1.5  2005/02/14 17:53:38  mch
Split between webnode (webapp) and library, prepare to split between API and special implementations

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

Revision 1.11  2004/10/06 17:37:47  mch
Added toURI

Revision 1.10  2004/07/07 10:55:24  mch
Replaced two-string constructor

Revision 1.9  2004/07/06 19:45:53  mch
Added isIvorn

Revision 1.8  2004/07/06 19:24:25  mch
Minor fix :-)

Revision 1.7  2004/07/06 19:20:28  mch
Doc updates and split between authority & key

Revision 1.6  2004/06/17 17:34:08  jdt
Miscellaneous coding standards issues.

Revision 1.5  2004/04/01 15:14:45  mch
Removed User/Agsl constructor as it makes a dependency on Agsl

Revision 1.4  2004/04/01 14:50:07  mch
added constructor from user & agsl

Revision 1.3  2004/03/25 12:21:30  mch
Tidied doc

Revision 1.2  2004/03/12 15:11:33  dave
Removed extra import in IvornTest.
Fixed redundant '#' in Ivorn with no fragment.
Fixed missing new-line at end of file.

Revision 1.1  2004/03/12 13:13:09  mch
Moved & Fixed null fragment error

Revision 1.3  2004/03/08 14:24:36  mch
Added toRegistryString()

Revision 1.2  2004/03/02 16:31:30  mch
Fixed case change

Revision 1.1  2004/03/01 16:38:58  mch
Merged in from datacenter 4.1 and odd cvs/case problems

Revision 1.1.2.1  2004/02/23 12:54:42  mch
It04 vospace identifiers

Revision 1.1  2004/02/16 23:31:47  mch
IVO Resource Name representation

 */


