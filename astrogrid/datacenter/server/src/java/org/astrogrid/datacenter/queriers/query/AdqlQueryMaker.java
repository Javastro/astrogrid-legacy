/*$Id: AdqlQueryMaker.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.query;

import org.astrogrid.datacenter.adql.generated.*;

import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.queriers.query.AdqlQuery;
import org.astrogrid.datacenter.queriers.query.ConeQuery;
import org.astrogrid.datacenter.queriers.query.Query;
import org.astrogrid.datacenter.queriers.query.QueryException;

/**
 * Translates queries into Cone searches
 */
public class AdqlQueryMaker  {
   
   public AdqlQuery getAdqlQuery(Query query) throws QueryException {
      if (query instanceof AdqlQuery) {
         return (AdqlQuery) query;
      }
      else if (query instanceof ConeQuery) {
         return fromCone((ConeQuery) query);
      }
      else {
         throw new QueryException("Don't know how to make an ADQL Query from a "+query.getClass());
      }
   }
   
   /**
    * Constructs an Adql query from a cone
    */
   public AdqlQuery fromCone(ConeQuery query) throws QueryException {
         Select s = ADQLUtils.buildMinimalQuery();
         TableExpression tc = new TableExpression();
         s.setTableClause(tc);
         
         Where w = new Where();
         tc.setWhereClause(w);
         
         Circle c = new Circle();
         c.setDec(ADQLUtils.mkApproxNum(query.getDec()));
         c.setRa(ADQLUtils.mkApproxNum(query.getRa()));
         c.setRadius(ADQLUtils.mkApproxNum(query.getRadius()));
         w.setCircle(c);

         //now set FROM from configuration
      /*
         From f = new From();
         tc.setFromClause(f);
         ArrayOfTable tables = new ArrayOfTable();
         Table t = new Table();
         t.setName(SimpleConfig.getSqlQueryMaker.CONE_asdf..asdf"TARGET"); //should get from config
         t.setAliasName("t");
         tables.addTable(t);
         f.setTableReference(tables);
       */
      return new AdqlQuery(s);
   }
   

}


/*
$Log: AdqlQueryMaker.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/
