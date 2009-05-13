/*
 * $Id: UrlToTriTableWriter.java,v 1.1 2009/05/13 13:20:23 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.impl.ukssdc;

import java.io.IOException;
import org.astrogrid.tableserver.out.FilteredTableWriter;
import org.astrogrid.tableserver.out.TableWriter;

/**
 * Includes a column in the table that refers to the Defines the methods a table writer must implement...
 *
 * @author M Hill
 */

public class UrlToTriTableWriter extends FilteredTableWriter {

   
   public UrlToTriTableWriter(TableWriter writer) {
      super(writer);
   }
      
   /** Writes the given array of values out */
   public void writeRow(Object[] colValues) throws IOException {
      
      //work out url
      String url = "(url to file will go here)";

      //add extra column
      Object[] newValues = new Object[colValues.length+1];
      for (int i = 0; i < colValues.length; i++) {
         newValues[i] = colValues[i];
      }
      newValues[colValues.length] = url;
      
      writer.writeRow(newValues);
   }

}


