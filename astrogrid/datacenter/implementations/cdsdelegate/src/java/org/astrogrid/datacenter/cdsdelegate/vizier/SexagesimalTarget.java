/*$Id: SexagesimalTarget.java,v 1.1 2003/11/18 11:23:49 nw Exp $
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

/** Representation of a target as a Sexagesimal  -  (hr min sec +deg arcmin arcsec)
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 *
 */
public class SexagesimalTarget extends Target {

    /**
     * 
     */
    public SexagesimalTarget(double hour,double min,double sec, double deg, double arcmin, double arcsec) {
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.deg = deg;
        this.arcmin = arcmin;
        this.arcsec =arcsec;
    }
    protected final double hour;
    protected final double min;
    protected final double sec;
    protected final double deg;
    protected final double arcmin;
    protected final double arcsec;
    

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        SexagesimalTarget other = (SexagesimalTarget) obj;
        return this.hour == other.hour 
            && this.min == other.min 
            && this.sec == other.sec 
            && this.deg == other.deg
            && this.arcmin == other.arcmin
            && this.arcsec == other.arcsec;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        // TODO Auto-generated method stub
        return (new Double(hour + min + sec + deg + arcmin + arcsec)).hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return hour + " " + min + " " + sec + " " + deg + " " + arcmin + " " + arcsec;
    }

    /**
     * @return
     */
    public double getArcmin() {
        return arcmin;
    }

    /**
     * @return
     */
    public double getArcsec() {
        return arcsec;
    }

    /**
     * @return
     */
    public double getDeg() {
        return deg;
    }

    /**
     * @return
     */
    public double getHour() {
        return hour;
    }

    /**
     * @return
     */
    public double getMin() {
        return min;
    }

    /**
     * @return
     */
    public double getSec() {
        return sec;
    }

}


/* 
$Log: SexagesimalTarget.java,v $
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/