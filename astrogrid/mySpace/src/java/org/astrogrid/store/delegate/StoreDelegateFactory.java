/*
 * $Id: StoreDelegateFactory.java,v 1.1 2004/02/24 15:59:56 mch Exp $
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
import org.astrogrid.store.AGSL;

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
   public static StoreClient createDelegate(Account operator, String endPoint) throws IOException
   {
      if (endPoint.toLowerCase().startsWith("http:")) {
         return new MySpaceIt04ServerDelegate(operator, endPoint);
      }
      else if (endPoint.toLowerCase().startsWith("vospace:")) {
         return new MySpaceIt04ServerDelegate(operator, new AGSL(new URL(endPoint), null).getDelegateEndpoint().toString());
      }
      throw new IllegalArgumentException("Unknown endpoint '"+endPoint+"', don't know which delegate to create for this");
   }

}

/*
$Log: StoreDelegateFactory.java,v $
Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.3  2004/02/17 03:37:27  mch
Various fixes for demo

Revision 1.2  2004/02/16 23:33:42  mch
Changed to use Account and AttomConfig

 */

