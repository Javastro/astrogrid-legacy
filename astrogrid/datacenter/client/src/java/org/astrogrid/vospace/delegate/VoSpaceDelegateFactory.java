/*
 * $Id: VoSpaceDelegateFactory.java,v 1.1 2004/02/15 23:16:06 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.vospace.delegate;

import java.io.IOException;
import org.astrogrid.community.User;

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
   public static VoSpaceClient createDelegate(User operator, String endPoint) throws IOException
   {
      if (endPoint.toLowerCase().startsWith("http:")) {
         return new MySpaceIt04ServerDelegate(operator, endPoint);
      }
      else if (endPoint.toLowerCase().startsWith("myspace:")) {
         return new MySpaceIt04ServerDelegate(operator, MySpaceReference.getDelegateEndpoint(endPoint));
      }
      throw new IllegalArgumentException("Unknown endpoint '"+endPoint+"', don't know which delegate to create for this");
   }

}

/*

 */

