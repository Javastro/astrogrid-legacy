/*$Id: Cardinality.java,v 1.1 2009/02/26 12:25:47 pah Exp $
 * Created on 16-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;

/** models the cardinality - i.e. number of times it may occur - of a parameter.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Aug-2004
 *
 */
public class Cardinality {

 /**
  *  Construct a new Cardinality
  * @param min minimum times the parameter must occur
  * @param max maximum tmes the parameter must occur.
  * @see #UNBOUNDED
  */
    public Cardinality(int min,int max) {
        this.min = min;
        this.max =max;
    }
    
    private final int min;
    private final int max;
    /** constant value to indicate unbounded max occurs */
    public final static int UNBOUNDED = 0;
    
    /** inspect the minimum number of occurences required of a parameter */
    public int getMinOccurs() {
        return min;
    }
    
    /** inspect the maximum number of occurrences required of a parameter */
    public int getMaxOccurs() {
        return max;
    }
    // constants for some common varieties
    
    /** cardinality of a required parameter */
    public final static Cardinality MANDATORY = new Cardinality(1,1);
    /** cardinality of an optional paramter */
    public final static Cardinality OPTIONAL = new Cardinality(0,1);
    /** cardinality of a repeated parameter that must occur at least once */
    public final static Cardinality MANDATORY_REPEATED = new Cardinality(1,UNBOUNDED);
    /** cardinality of a repeated parameter thay may occur 0, 1 or many times */
    public final static Cardinality OPTIONAL_REPEATED = new Cardinality(0,UNBOUNDED);
    

    /**
     * Returns <code>true</code> if this <code>Cardinality</code> is the same as the o argument.
     *
     * @return <code>true</code> if this <code>Cardinality</code> is the same as the o argument.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        Cardinality castedObj = (Cardinality) o;
        return ((this.min == castedObj.min) && (this.max == castedObj.max));
    }
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + min;
        hashCode = 31 * hashCode + max;
        return hashCode;
    }
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Cardinality:");
        buffer.append(" min: ");
        buffer.append(min);
        buffer.append(" max: ");
        buffer.append(max);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: Cardinality.java,v $
Revision 1.1  2009/02/26 12:25:47  pah
separate more out into cea-common for both client and server

Revision 1.2  2008/09/13 09:51:05  pah
code cleanup

Revision 1.1  2004/08/16 11:03:07  nw
added classes to model cardinality of prefs.
 
*/