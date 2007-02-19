/*
 * $Id: FileLocation.java,v 1.2 2007/02/19 16:20:24 gtr Exp $
 * 
 * Created on 11-Nov-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.apps.tables;

import java.io.OutputStream;
import java.net.URI;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Nov-2004
 * @version $Name:  $
 * @since iteration6
 */
public class FileLocation implements StarTableWriterLocation {

   /**
    * 
    */
   public FileLocation() {
      super();
      // TODO Auto-generated constructor stub
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.apps.tables.StarTableWriterLocation#getStream()
    */
   public OutputStream getStream() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "FileLocation.getStream() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.apps.tables.StarTableWriterLocation#getLocationReference()
    */
   public URI getLocationReference() {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("FileLocation.getLocationReference() not implemented");
   }

}
