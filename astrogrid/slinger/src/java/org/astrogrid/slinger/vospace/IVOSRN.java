/*
 * $Id: IVOSRN.java,v 1.4 2005/01/26 17:41:48 mch Exp $
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
import org.astrogrid.slinger.SRI;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.store.delegate.VoSpaceResolver;

/**
 * International Virtual Observatory Storepoint Resource Name.  An IVORN used to
 * name storepoints - eg files.
 *
 * @author MCH, KMB, KTN, DM, ACD
 */


public class IVOSRN extends IVORN implements SRI, TargetIdentifier, SourceIdentifier
{
   
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
//@todo      return TargetMaker.makeTarget(super.resolve());
      return null;
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
      return null; //return VoSpaceResolver.resolveInputStream(this, user);
   }
   
   /** Used to get the mime type of the pointed-do data */
   public String getMimeType(Principal user) throws IOException {
      //@todo
      return null;
   }

   /** resolves to a locator  */
   public String resolveSrl() throws IOException {
      return null; //@todo
   }

}

/*
$Log: IVOSRN.java,v $
Revision 1.4  2005/01/26 17:41:48  mch
fix to compile until resolving is properly handled

Revision 1.3  2005/01/26 17:31:57  mch
Split slinger out to scapi, swib, etc.

Revision 1.1.2.3  2005/01/26 14:35:37  mch
Separating slinger and scapi

Revision 1.1.2.2  2004/12/08 18:37:11  mch
Introduced SPI and SPL

Revision 1.1.2.1  2004/12/06 02:39:58  mch
a few bug fixes

Revision 1.1.2.1  2004/12/03 11:50:19  mch
renamed Msrl etc to separate from storeclient ones.  Prepared for SRB


 */


