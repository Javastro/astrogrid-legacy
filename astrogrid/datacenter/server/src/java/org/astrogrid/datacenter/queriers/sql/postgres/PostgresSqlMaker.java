/*$Id: PostgresSqlMaker.java,v 1.1 2004/04/01 17:17:57 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql.postgres;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;

/**
 * Produced Postgres-specific SQL.  This means:
 * <p>
 * * All <> are replaced by &&
 * <p>
 * * No aggregate functions in WHERE
 * <p>
 * * Trig functions assume radians as arguments
 */
public class PostgresSqlMaker extends StdSqlMaker {

   private static final Log log = LogFactory.getLog(PostgresSqlMaker.class);

   public String getRaDecSql(ConeQuery query) {

      String table = SimpleConfig.getSingleton().getString(CONE_SEARCH_TABLE_KEY);
      String alias = table.substring(0,1);
      
      //get which columns given RA & DEC for cone searches
      String raCol  = alias+"."+SimpleConfig.getSingleton().getString(CONE_SEARCH_RA_COL_KEY);
      String decCol = alias+"."+SimpleConfig.getSingleton().getString(CONE_SEARCH_DEC_COL_KEY);
      
      double ra  = query.getRa();
      double dec = query.getDec();
      double radius = query.getRadius();
      
      return "SELECT * FROM "+table+" as "+alias+
         " WHERE "+
         //square - for quicker searches
         "("+decCol+"<"+(dec+radius)+" AND "+decCol+">"+(dec-radius)+" AND"+
         " "+ raCol+"<"+(ra +radius)+" AND "+ raCol+">"+(ra -radius)+")"+
         " AND "+
         //circle
         "( DEGREES(2 * ASIN( SQRT("+
              "POW(SIN(RADIANS(("+dec+"-"+decCol+")/2)), 2) "+
              "+ COS(RADIANS("+dec+")) * COS(RADIANS("+decCol+")) "+
                "* POW(SIN(RADIANS(("+ra+"-"+raCol+")/2),2))"+
         "))) < "+radius+")";
   }
   
   /**
    * Constructs an SQL statement for the given ADQL.
    */
   public String fromAdql(AdqlQuery query) {
      
      String stdSql = super.fromAdql(query);
      
      String postgresSql = stdSql.replaceAll("<>","&&");
      
      return postgresSql;
   }

   
}


/*
$Log: PostgresSqlMaker.java,v $
Revision 1.1  2004/04/01 17:17:57  mch
Added postgres-specific SQL translator

Revision 1.5  2004/03/24 15:57:31  kea
Updated Javadocs etc.

Revision 1.4  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.3  2004/03/12 05:03:23  mch
Removed unused code

Revision 1.2  2004/03/12 05:01:22  mch
Changed doc

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor
 
*/

