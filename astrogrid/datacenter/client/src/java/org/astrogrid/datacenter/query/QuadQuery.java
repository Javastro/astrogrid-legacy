/*
 * $Id: QuadQuery.java,v 1.1 2004/03/12 20:00:11 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import org.astrogrid.datacenter.sky.RaDecPoint;
import org.astrogrid.datacenter.sky.SkyPoint;
import org.astrogrid.datacenter.sky.SkyQuad;

/**
 * Represents a 'Rectangular' Search.
 *
 * @author M Hill
 */

public class QuadQuery implements RegionQuery {

   private SkyQuad region;
   
   /** Constructs query from given ra & decs of corners.
    */
   public QuadQuery(double minRa, double minDec, double maxRa, double maxDec) {
      region = new SkyQuad(new RaDecPoint(minRa, minDec), new RaDecPoint(maxRa, maxDec));
   }

   /** Returns itself */
   public QuadQuery toQuadRegionQuery() { return this; }
   
}
/*
 $Log: QuadQuery.java,v $
 Revision 1.1  2004/03/12 20:00:11  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



