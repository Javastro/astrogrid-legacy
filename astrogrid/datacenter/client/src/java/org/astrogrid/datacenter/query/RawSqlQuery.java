/*
 * $Id: RawSqlQuery.java,v 1.2 2004/03/13 23:38:27 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.DatacenterException;



/**
 * Represents a raw SQL query.
 *
 * @author M Hill
 */

public class RawSqlQuery implements Query {

   String sql = null;

   /** Constructs query.
    */
   public RawSqlQuery(String givenSql) {
      
      this.sql = givenSql;
   }
   
   public String getSql() {
      return sql;
   }
   
   /** Human representation for debugging, trace, etc */
   public String toString() {
      return "SqlQuery: '"+sql+"'";
   }
   
}
/*
 $Log: RawSqlQuery.java,v $
 Revision 1.2  2004/03/13 23:38:27  mch
 Test fixes and better front-end JSP access

 Revision 1.1  2004/03/12 20:00:11  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



