/*$Id: Wavelength.java,v 1.2 2003/12/01 16:50:30 nw Exp $
 * Created on 11-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cdsdelegate.vizier;

/** Static enumeration class of supported wavelengths
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Nov-2003
 *
 */
public class Wavelength {

    public final static Wavelength RADIO = new Wavelength("Radio");
    public final static Wavelength IR = new Wavelength("IR");
    public final static Wavelength OPTICAL = new Wavelength("optical");
    public final static Wavelength UV = new Wavelength("UV");
    public final static Wavelength EUV = new Wavelength("EUV");
    public final static Wavelength X_RAY = new Wavelength("X-ray");
    public final static Wavelength GAMMA_RAY = new Wavelength("Gamma-ray");    

    /**
     * 
     */
    private Wavelength(String name) {
        this.name = name;
    }
    protected final String name;
    
    

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
        return name.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

    /** convert a string into a wavelength constant.
     * @param value
     * @return
     */
    public static Wavelength parse(String v) {
        String value= v.trim();
        if (RADIO.name.equalsIgnoreCase(value)) {
            return RADIO;
        } 
        else if (IR.name.equalsIgnoreCase(value)) {
            return IR;
        }
        else if (OPTICAL.name.equalsIgnoreCase(value)) {
            return OPTICAL;
        }        
        else if (UV.name.equalsIgnoreCase(value)) {
            return UV;
        }
        else if (EUV.name.equalsIgnoreCase(value)) {
            return EUV;
        }
        else if (X_RAY.name.equalsIgnoreCase(value)) {
            return X_RAY;
        }
        else if (GAMMA_RAY.name.equalsIgnoreCase(value)) {
            return GAMMA_RAY;
        } 
        else {
            throw new IllegalArgumentException("Unrecognized wavelength value " + value);
        }                                
    }

}


/* 
$Log: Wavelength.java,v $
Revision 1.2  2003/12/01 16:50:30  nw
first working tested version

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/