/*
 * $Id: IVOSRN.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
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


public class IVOSRN extends IVORN implements TargetIdentifier, SourceIdentifier
{
   public final static String SCHEME = "ivo";
   
   private String key = null;
   private String authority = null;
   private String fragment = null;
   
   /** Construct from given string */
   public IVOSRN(String ivorn) throws URISyntaxException
   {
      super(ivorn);
   }
   
   /** Construct from given authority, key and fragment */
   public IVOSRN(String anAuthority, String aKey, String aFragment)
   {
      super(anAuthority, aKey, aFragment);
   }
   
   /** Create a new ivorn from an existing one and a new fragment.
    * If the existing one has a fragment, it is replaced in the constructed IVORN
    */
   public IVOSRN(IVORN aPath, String aFragment)  {
      super(aPath, aFragment);
   }
   
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream resolveOutputStream(Principal user) throws IOException {
      return VoSpaceResolver.resolveOutputStream(this, user);
   }
   
   /** Used to set the mime type of the data about to be sent to the target. Does nothing. */
   public void setMimeType(String aMimeType, Principal user) {
      //@todo
   }

   public Reader resolveReader(Principal user) throws IOException {
      return new InputStreamReader(resolveInputStream(user));
   }
   
   public Writer resolveWriter(Principal user) throws IOException {
      return new OutputStreamWriter(resolveOutputStream(user));
   }

   /** All sources must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public InputStream resolveInputStream(Principal user) throws IOException {
      return VoSpaceResolver.resolveInputStream(this, user);
   }
   
   /** Used to get the mime type of the pointed-do data */
   public String getMimeType(Principal user) throws IOException {
      try {
         return StoreFileResolver.resolveStoreFile(toUri().toString(), user).getMimeType();
      }
      catch (URISyntaxException e) {
         throw new IOException(e+" resolving file at "+this);
      }
   }

   /** Gets the *location* of the ivorn */
   public String toLocation(Principal user) throws IOException {
      try {
         return StoreFileResolver.resolveStoreFile(toUri().toString(), user).getUri();
      }
      catch (URISyntaxException e) {
         throw new IOException(e+" resolving file at "+this);
      }
   }

}

/*
$Log: IVOSRN.java,v $
Revision 1.2  2004/12/07 01:33:36  jdt
Merge from PAL_Itn07

Revision 1.1.2.1  2004/12/06 02:39:58  mch
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


