/*
 * $Id: IVORN.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
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
import org.astrogrid.slinger.StoreFileResolver;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.TargetIdentifier;

/**
 * International Virtual Observatory Storepoint Resource Name.  An IVORN used to
 * name storepoints - eg files.
 *
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
      fragment = uri.getFragment();
   }
   
   /** Construct from given authority, key and fragment */
   public IVORN(String anAuthority, String aKey, String aFragment)
   {
      this.key = "/"+aKey;
      this.authority = anAuthority;
      this.fragment = aFragment;
   }
   
   /** Create a new ivorn from an existing one and a new fragment.
    * If the existing one has a fragment, it is replaced in the constructed IVORN
    */
   public IVORN(IVORN aPath, String aFragment)  {

      this.authority = aPath.authority;
      this.key = aPath.key;
      fragment = aFragment;
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
   public String getFragment() {
      return fragment;
   }

   /** String representation */
   public String toString() {
      if (fragment == null) {
         return SCHEME+"://"+getPath();
      } else {
         return SCHEME+"://"+getPath()+"#"+fragment;
      }
   }

   /** Returns the IVORN in URI form */
   public URI toUri() {
      try {
         return new URI(toString());
      }
      catch (URISyntaxException e) {
         //this should never happen as it shouldn't be possible to create an agsl that isn't
         throw new RuntimeException("Application error: "+e+" for IVORN "+toString());
      }
   }

   /** Representation to be used when submitting the IVORN to a registry
    * to be resolved *
   public String toRegistryString() {
      return SCHEME+"://"+getPath();
   }
   
   /** Returns true if the given string is likely to be an ivorn - ie if it
    * starts with ivo://
    */
   public static boolean isIvorn(String aString) {
      return aString.toLowerCase().startsWith(SCHEME+"://");
   }
   

}

/*
$Log: IVORN.java,v $
Revision 1.2  2004/12/07 01:33:36  jdt
Merge from PAL_Itn07

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


