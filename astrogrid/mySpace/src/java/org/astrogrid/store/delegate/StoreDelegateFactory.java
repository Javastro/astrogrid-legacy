/*
 * $Id: StoreDelegateFactory.java,v 1.3 2004/03/01 16:38:58 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate;


import java.io.IOException;
import java.net.URL;
import org.astrogrid.community.Account;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;

/**
 * Creates the appropriate delegates to access the various vospace servers
 *
 * @author M Hill
 */

public class StoreDelegateFactory
{
   
   /**
    * Creates the correct delegate for the given url.  The url is either the DUMMY
    * constant from MySpaceDummyDelegate, or an http:// url to access a 'real'
    * myspace.  We could also add a FTP and GridFTP delegates for services
    * without accounts on MySpace servers.
    */
   public static StoreClient createDelegate(Account operator, Agsl location) throws IOException
   {
      if (location.getMsrl() != null) {
         Msrl msrl = location.getMsrl();

         return new MySpaceIt04ServerDelegate(operator, msrl.getDelegateEndpoint().toString());
      }
      URL url = location.resolveURL();
      if (url.getProtocol().equals("ftp")) {
         return new FtpStore(url);
      }
      if (url.getProtocol().equals("file")) {
         return new LocalFileStore();
      }
      
      throw new IllegalArgumentException("Don't know how to create delegate for AGSL '"+location+"'");
   }

}

/*
$Log: StoreDelegateFactory.java,v $
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

