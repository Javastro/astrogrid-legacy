/*$Id: StdSqlMaker.java,v 1.2 2004/03/12 20:04:57 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.astrogrid.datacenter.queriers.sql.deprecated.SqlQuerierSPI;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;

/**
 * A 'standard' translator that creates 'standard' SQL
 */
public class StdSqlMaker  extends SqlMaker {

   private static final Log log = LogFactory.getLog(StdSqlMaker.class);

   /**
    * Constructs an SQL statement for the given cone query. Looks for the HTM
    * column first - if it finds it, uses that to do the cone search
    */
   public String fromCone(ConeQuery query) {
      if (SimpleConfig.getSingleton().getString(CONE_SEARCH_HTM_KEY, null) != null) {
         return getHtmSql(query);
      } else {
         return getRaDecSql(query);
      }
   }
   
   /** Returns the SQL suitable for doing a cone query on RA & DEC values */
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
         "(("+decCol+"<"+(dec+radius)+" AND "+decCol+">"+(dec-radius)+") AND"+
         " ("+ raCol+"<"+(ra +radius)+" AND "+ raCol+">"+(ra -radius)+"))"+
         " AND "+
         //circle
         "((2 * ASIN( SQRT("+
         "SIN(("+dec+"-"+decCol+")/2) * SIN(("+dec+"-"+decCol+")/2) +"+    //some sqls won't handle powers so multiply by self
         "COS("+dec+") * COS("+decCol+") * "+
         "SIN(("+ra+"-"+raCol+")/2) * SIN(("+ra+"-"+raCol+")/2)  "+ //some sqls won't handle powers so multiply by self
      "))) < "+radius+")";
   }
      
   /** Returns the SQL suitable for doing a cone query on HTM-indexed catalogue */
   public String getHtmSql(ConeQuery query) {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Constructs an SQL statement for the given ADQL
    */
   public String fromAdql(AdqlQuery query) throws QueryException {
      //should use appropriate xslt, but use deprecated stuff for now

      // find the translator
        Element queryBody = query.toDom().getDocumentElement();
        String namespaceURI = queryBody.getNamespaceURI();
        if (namespaceURI == null) {
            // maybe not using namespace aware parser - see if we can find an xmlns attribute instead
            namespaceURI = queryBody.getAttribute("xmlns");
        }
        if (namespaceURI == null) {
            DomHelper.PrettyElementToStream(queryBody,System.out);
            throw new IllegalArgumentException("Query body has no namespace - cannot determine language");
        }
        SqlQuerierSPI spi = new SqlQuerierSPI();
        Translator trans = spi.getTranslatorMap().lookup(namespaceURI);
        if (trans == null) {
            throw new RuntimeException("No ADQL translator available for namespace: '" + namespaceURI+"'");
        }
        // do the translation
        Object intermediateRep = null;
        Class expectedType = null;
        try { // don't trust it.
            intermediateRep = trans.translate(queryBody);
            expectedType = trans.getResultType();
            if (! expectedType.isInstance(intermediateRep)) { // checks result is non-null and the right type.
                throw new DatabaseAccessException("Translation result " + intermediateRep.getClass().getName() + " not of expected type " + expectedType.getName());
            }
        } catch (Throwable t) {
            throw new RuntimeException("Translation phase failed:" + t.getMessage());
        }
        return (String) intermediateRep;

   }
   
}


/*
$Log: StdSqlMaker.java,v $
Revision 1.2  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/
