/*$Id: DecimalDegreesTarget.java,v 1.1 2003/11/18 11:23:49 nw Exp $
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

/** Target represented in decimal degrees
 * @todo - find correct name for these parameters
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 *
 */
public class DecimalDegreesTarget extends Target {

    /**
     * 
     */
    public DecimalDegreesTarget(double x,double y) {
        this.x = x;
        this.y = y;
    }
    protected final double x;
    protected final double y;
    

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return x + " " + y;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        // should test with an error factor.
        DecimalDegreesTarget other = (DecimalDegreesTarget)obj;
        return this.x == other.x && this.y == other.y;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (new Double(x + y)).hashCode();
    }

    /**
     * @return
     */
    public double getX() {
        return x;
    }

    /**
     * @return
     */
    public double getY() {
        return y;
    }

}


/* 
$Log: DecimalDegreesTarget.java,v $
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/