/*
 * $Id: VoSpaceDelegateFactory.java,v 1.2 2004/02/16 23:33:42 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.vospace.delegate;
import java.io.IOException;
import java.net.URL;
import org.astrogrid.community.Account;
import org.astrogrid.vospace.VoRL;

/**
 * Creates the appropriate delegates to access the various vospace servers
 *
 * @author M Hill
 */

public class VoSpaceDelegateFactory
{
   
   /**
    * Creates the correct delegate for the given url.  The url is either the DUMMY
    * constant from MySpaceDummyDelegate, or an http:// url to access a 'real'
    * myspace.  We could also add a FTP and GridFTP delegates for services
    * without accounts on MySpace servers.
    */
   public static VoSpaceClient createDelegate(Account operator, String endPoint) throws IOException
   {
      if (endPoint.toLowerCase().startsWith("http:")) {
         return new MySpaceIt04ServerDelegate(operator, endPoint);
      }
      else if (endPoint.toLowerCase().startsWith("vospace:")) {
         return new MySpaceIt04ServerDelegate(operator, new VoRL(new URL(endPoint), null).getDelegateEndpoint().toString());
      }
      throw new IllegalArgumentException("Unknown endpoint '"+endPoint+"', don't know which delegate to create for this");
   }

}

/*
$Log: VoSpaceDelegateFactory.java,v $
Revision 1.2  2004/02/16 23:33:42  mch
Changed to use Account and AttomConfig

 */

