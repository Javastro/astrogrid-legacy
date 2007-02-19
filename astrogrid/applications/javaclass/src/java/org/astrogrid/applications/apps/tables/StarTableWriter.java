/*
 * $Id: StarTableWriter.java,v 1.2 2007/02/19 16:20:24 gtr Exp $
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

import java.io.IOException;

import uk.ac.starlink.table.StarTable;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Nov-2004
 * @version $Name:  $
 * @since iteration6
 */
public interface StarTableWriter {
   /**
    * Writes a <tt>StarTable</tt> object to a given location.
    * If possible, a location of "-" should be taken as a request to
    * write to standard output.
    *
    * @param  startab  the table to write
    * @param  location  the destination of the written object 
    *         (probably, but not necessarily, a filename)
    */
   void writeStarTable( StarTable startab, StarTableWriterLocation location )
           throws IOException;

   /**
    * Indicates whether the destination is of a familiar form for this
    * kind of writer.  This may be used to guess what kind of format
    * a table should be written in.  Implementations should return
    * <tt>true</tt> for values of <tt>location</tt> which look like
    * the normal form for their output format, for instance one with
    * the usual file extension.
    *
    * @param  location  the location name (probably filename)
    * @return <tt>true</tt> iff it looks like a file this writer would
    *         normally write
    */
   boolean looksLikeFile( StarTableWriterLocation location );

   /**
    * Gives the name of the format which is written by this writer.
    * Matching against this string may be used by callers to identify
    * or select this writer from a list.
    *
    * @param   a short string identifying the output format of this writer
    */
   String getFormatName();

}
