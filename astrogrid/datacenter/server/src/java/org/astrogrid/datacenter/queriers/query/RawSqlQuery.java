/*
 * $Id: RawSqlQuery.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.query;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.DatacenterException;



/**
 * Represents a raw SQL query.
 *
 * @author M Hill
 */

public class RawSqlQuery implements Query {

   String sql = null;

   public final static String SQL_PASSTHROUGH_ENABLED = "datacenter.sql.passthrough.enabled";
   
   /** Constructs query. Throws exception if raw SQL is not allowed on this
    * server
    */
   public RawSqlQuery(String givenSql) {
      
      if (SimpleConfig.getSingleton().getString(SQL_PASSTHROUGH_ENABLED, "false") != "true") {
         throw new IllegalArgumentException("This datacenter does not allow raw SQL to be submitted");
      }
         
      this.sql = givenSql;
   }
   
   public String getSql() {
      return sql;
   }
   
}
/*
 $Log: RawSqlQuery.java,v $
 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



