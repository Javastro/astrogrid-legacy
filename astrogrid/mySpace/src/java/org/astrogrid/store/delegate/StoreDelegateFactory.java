/*
 * $Id: StoreDelegateFactory.java,v 1.11 2004/04/06 09:00:46 KevinBenson Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate;


import java.io.IOException;
import java.net.URL;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.myspaceItn05.MySpaceIt05Delegate;

/**
 * Creates the appropriate delegates to access the various vospace servers
 *
 * @author M Hill
 */

public class StoreDelegateFactory
{
   
   /**
    * Creates the correct delegate for the given Agsl and operator, for general
    * file handling - moving, copying, uploading, etc.
    */
   public static StoreClient createDelegate(User operator, Agsl location) throws IOException
   {

      if (location.getScheme().startsWith(Agsl.SCHEME+":"+Msrl.SCHEME) && location.getEndpoint().endsWith("/Manager")) {
         //System.out.println("the endpoint location = " + location.getEndpoint().toString());
         return new MySpaceIt05Delegate(operator, location.getEndpoint().toString());
      }
      if (location.getScheme().startsWith(Agsl.SCHEME+":"+Msrl.SCHEME)) {
         return new org.astrogrid.store.delegate.myspace.MySpaceIt04Delegate(operator, location.getEndpoint().toString());
      }
      if (location.getScheme().startsWith(Agsl.SCHEME+":ftp")) {
         return new org.astrogrid.store.delegate.ftp.FtpStore(operator, location);
      }
      if (location.getScheme().startsWith(Agsl.SCHEME+":file")) {
         return new org.astrogrid.store.delegate.local.LocalFileStore(location);
      }
      
      throw new IllegalArgumentException("Don't know how to create delegate for AGSL '"+location+"'");
   }

   /**
    * Creates the correct delegate for administering the store client at the
    * given Agsl.  Ignores the path of the Agsl.
    */
   public static StoreAdminClient createAdminDelegate(User operator, Agsl location) throws IOException {
      if (location.getScheme().startsWith(Agsl.SCHEME+":"+Msrl.SCHEME) && location.getEndpoint().endsWith("/Manager")) {
         //System.out.println("the endpoint location = " + location.getEndpoint().toString());
         return new MySpaceIt05Delegate(operator, location.getEndpoint().toString());
      }      
      if (location.getScheme().startsWith(Agsl.SCHEME+":file")) {
         return new org.astrogrid.store.delegate.local.LocalFileStore(location);
      }      
      throw new IllegalArgumentException("Don't know how to create delegate for AGSL '"+location+"'");
   }
}

/*
$Log: StoreDelegateFactory.java,v $
Revision 1.11  2004/04/06 09:00:46  KevinBenson
changed it around so that it calls community first then registry to figvure out endpoints

Revision 1.10  2004/04/05 14:15:27  KevinBenson
change to strip out the myspace.  And call the myspaceitn05

Revision 1.9  2004/03/22 10:25:42  mch
Added VoSpaceClient, StoreDelegate, some minor changes to StoreClient interface

Revision 1.8  2004/03/19 12:39:37  mch
Added StoreAdminClient implementation to LocalFileStore

Revision 1.7  2004/03/05 19:24:43  mch
Store delegates were moved

Revision 1.6  2004/03/04 12:51:31  mch
Moved delegate implementations into subpackages

Revision 1.5  2004/03/02 00:15:39  mch
Renamed MyspaceIt04Delegate from misleading ServerDelegate

Revision 1.4  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

Revision 1.3  2004/03/01 16:38:58  mch
Merged in from datacenter 4.1 and odd cvs/case problems

Revision 1.2  2004/03/01 15:15:04  mch
Updates to Store delegates after myspace meeting

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.3  2004/02/17 03:37:27  mch
Various fixes for demo

Revision 1.2  2004/02/16 23:33:42  mch
Changed to use Account and AttomConfig

 */

