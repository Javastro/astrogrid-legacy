/*
 * $Id: SkyRegion.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;

import java.awt.Dimension;


/**
 * Represents an area on the sky
 *
 * @author M Hill
 */

public interface SkyRegion {

   /** Returns the overall bounds - ie min RA/DEC, max RA/DEC or opposite corner HTMs.
    * This can be useful for first-stage queries */
   public SkyQuad getBounds();
   
}
/*
 $Log: SkyRegion.java,v $
 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



