/*
 * $Id: RdbmsQueryValidator.java,v 1.1 2005/03/10 16:42:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.query.DefaultQueryTraverser;
import org.astrogrid.query.condition.ColumnReference;


/**
 * Checks the query against the rdbms resource
 */

public class RdbmsQueryValidator extends DefaultQueryTraverser {
   
   TableMetaDocInterpreter interpreter = null;

   public RdbmsQueryValidator(TableMetaDocInterpreter reader)  {
      this.interpreter = reader;
   }
   
   public void visitScope(String[] scope) {
      for (int i = 0; i < scope.length; i++) {
         if (interpreter.getTable(null, scope[i]) ==null) {
            throw new IllegalArgumentException("Table '"+scope[i]+"' is not available in this RDBMS Resource");
         }
      }
   }
   
   public void visitColumnReference(ColumnReference expression) {
      try {
         if (interpreter.getColumn(null,expression.getTableName(), expression.getColName()) == null) {
            throw new IllegalArgumentException(expression+" is not in RDBMS Resource and so cannot be queried");
         }
      } catch (MetadataException me) {
         throw new RuntimeException(me);
      }
      
   }
   
}

/*
 $Log: RdbmsQueryValidator.java,v $
 Revision 1.1  2005/03/10 16:42:55  mch
 Split fits, sql and xdb

 Revision 1.2  2005/03/10 13:49:52  mch
 Updating metadata

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.3  2005/01/24 12:14:28  mch
 Fixes to VizieR proxy and resource stuff

 Revision 1.1.2.2  2004/12/10 12:37:13  mch
 Cone searches to look in metadata, lots of metadata interpreterrs

 Revision 1.1.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.2  2004/12/07 21:21:09  mch
 Fixes after a days integration testing

 Revision 1.1.2.1  2004/12/05 19:38:37  mch
 changed skynode to 'raw' soap (from axis) and bug fixes


 */




