/*$Id: ConeQueryMaker.java,v 1.2 2004/03/12 20:04:57 mch Exp $
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

import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;

/**
 * Translates queries into Cone searches
 */
public class ConeQueryMaker  {
   
   public ConeQuery getConeQuery(Query query) throws QueryException {
      if (query instanceof ConeQuery) {
         return (ConeQuery) query;
      }
      else if (query instanceof AdqlQuery) {
         return getConeQuery((ConeQuery) query);
      }
      else {
         throw new QueryException("Don't know how to query using a "+query.getClass());
      }
   }
   
   /**
    * Constructs a Cone Query from the Region part of the given ADQL.
    */
   public ConeQuery coneFromAdql(AdqlQuery query) throws QueryException {
      throw new QueryException("Don't know how to query using a "+query.getClass());
   }
   

}


/*
$Log: ConeQueryMaker.java,v $
Revision 1.2  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/
