/*
 * $Id: SkyPoint.java,v 1.3 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.sky;

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
   
}

