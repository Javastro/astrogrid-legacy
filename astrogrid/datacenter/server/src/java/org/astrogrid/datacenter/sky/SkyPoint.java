/*
 * $Id: SkyPoint.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;

import java.awt.Dimension;


/**
 * Represents a point on the sky.  These SkyPoint representations are immutable;
 * this is important so that we can return references to internal
 * representations, without having to create new objects every time, and without
 * worrying about changes being made to them externally.
 *
 * @author M Hill
 */

public interface SkyPoint {

   /** Returns the point as an RaDec */
   public RaDecPoint toRaDec();
   
   /** Returns the point as a HTM  */
   public HtmPoint toHtm();
   
}
/*
 $Log: SkyPoint.java,v $
 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



