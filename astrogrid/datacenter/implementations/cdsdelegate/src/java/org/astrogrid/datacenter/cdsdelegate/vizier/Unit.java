/*$Id: Unit.java,v 1.1 2003/11/18 11:23:49 nw Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cdsdelegate.vizier;

/** enumeration class representing different units of measurement.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 *
 */
public class Unit {
    public static final Unit ARCMIN = new Unit("arcmin");
    public static final Unit ARCSEC = new Unit("arcsec");
    public static final Unit DEG = new Unit("deg");
    /**
     * 
     */
    private Unit(String s) {
        this.unitName = s;
    }
    protected final String unitName;

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return this == obj;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return unitName.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return unitName.toString();
    }

}


/* 
$Log: Unit.java,v $
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/