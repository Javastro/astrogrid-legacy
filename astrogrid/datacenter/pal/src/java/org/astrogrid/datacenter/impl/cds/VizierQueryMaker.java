/*$Id: VizierQueryMaker.java,v 1.1 2004/10/05 19:19:18 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.impl.cds;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.impl.cds.vizier.DecimalDegreesTarget;
import org.astrogrid.datacenter.impl.cds.vizier.Unit;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;

/**
 * Produced Postgres-specific SQL
 */
public class VizierQueryMaker  {

   private static final Log log = LogFactory.getLog(VizierQueryMaker.class);

   /**
    * Makes an SQL string from the given Query */
   public VizierQuery getVizierQuery(Query query) throws QueryException {
      if (query instanceof ConeQuery) {
         return fromCone((ConeQuery) query);
      }
      else if (query instanceof AdqlQuery) {
         return fromAdql((AdqlQuery) query);
      }
      else {
         throw new QueryException("Don't know how to make an ADQL Query from a "+query.getClass());
      }
   }

   /**
    * Constructs a Vizier Query from the given cone query
    */
   public VizierQuery fromCone(ConeQuery cone) {
      VizierQuery viz = new VizierQuery(
         new DecimalDegreesTarget(cone.getRa(), cone.getDec()),
         cone.getRadius()
      );

      viz.setUnit(Unit.DEG);
      
      return viz;
      
   }
      
   /**
    * Constructs a Vizier Query from the given ADQL
    */
   public VizierQuery fromAdql(AdqlQuery query) {
      //should use appropriate xslt, but use deprecated stuff for now
      throw new UnsupportedOperationException("Need to write the ADQL -> Vizier Cone translator for ADQL 0.7.4+");

   }

}


/*
$Log: VizierQueryMaker.java,v $
Revision 1.1  2004/10/05 19:19:18  mch
Merged CDS implementation into PAL

Revision 1.5  2004/09/29 18:45:55  mch
Bringing Vizier into line with new(er) metadata stuff

Revision 1.4  2004/09/28 15:05:27  mch
Temporary compile-only fix for removing obsolete ADQL 0.5

Revision 1.3  2004/09/10 10:31:17  mch
Added cause to thrown error

Revision 1.2  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

Revision 1.1  2004/03/13 23:40:59  mch
Changes to adapt to It05 refactor

Revision 1.4  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.3  2004/03/12 05:03:23  mch
Removed unused code

Revision 1.2  2004/03/12 05:01:22  mch
Changed doc

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/

