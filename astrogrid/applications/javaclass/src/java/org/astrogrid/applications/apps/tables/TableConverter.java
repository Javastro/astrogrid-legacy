/*
 * $Id: TableConverter.java,v 1.2 2007/02/19 16:20:24 gtr Exp $
 * 
 * Created on 10-Nov-2004 by Paul Harrison (pah@jb.man.ac.uk)
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
import java.io.OutputStream;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.StarTableOutput;
import uk.ac.starlink.table.TableFormatException;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 10-Nov-2004
 * @version $Name:  $
 * @since iteration6
 */
public class TableConverter {

   private final StarTableFactory factory;
   private final StarTable inTable;

   /**
    * @param stream
    * @param inputFomat
    * @throws IOException
    * @throws TableFormatException
    * 
    */
   public TableConverter(InputStream stream, String inputFormat) throws TableFormatException, IOException {
      factory = new StarTableFactory();
      inTable = factory.makeStarTable(new StreamDataSource(stream), inputFormat);
      
      
   }
   
   public void  write(String outputFormat) throws TableFormatException, IOException
   {
      //TODO - it would be good if the writeStartable routine
      	new StarTableOutput().writeStarTable(inTable, outputFormat, outputFormat);
   }
   

}
