/*
 * $Id: StreamDataSource.java,v 1.2 2007/02/19 16:20:24 gtr Exp $
 * 
 * Created on 09-Nov-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.apps.tables;

import java.io.IOException;
import java.io.InputStream;

import uk.ac.starlink.util.DataSource;

/**
 * A simple Datasource object that can be created from a standard stream. Exists because we have streams already created in CEA framework and all the standard STIL datasources take files/urls etc....
 * @author Paul Harrison (pah@jb.man.ac.uk) 09-Nov-2004
 * @version $Name:  $
 * @since iteration6
 */
public class StreamDataSource extends DataSource {

  protected InputStream originalStream;
   /**
    * 
    */
   public StreamDataSource(InputStream stream) {
    originalStream = stream;
   }

   /* (non-Javadoc)
    * @see uk.ac.starlink.util.DataSource#getRawInputStream()
    */
   protected InputStream getRawInputStream() throws IOException {
     return originalStream;
   }

}
