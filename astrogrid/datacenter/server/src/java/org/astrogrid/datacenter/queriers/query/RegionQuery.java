/*
 * $Id: RegionQuery.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.query;

import org.astrogrid.datacenter.sky.SkyRegion;


/**
 * Represents a Regional Query; the shape used to construct it represents the
 * region.
 *
 * @author M Hill
 */

public interface RegionQuery extends Query {

   /** a special case - 'rectangular' queries are likely to be much
    * faster than cones, polys etc, so first passes might extract a
    * quad query first.
    */
   public QuadQuery toQuadRegionQuery();
   
}
/*
 $Log: RegionQuery.java,v $
 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



