/*$Id: VizierCone.java,v 1.2 2003/12/01 16:50:11 nw Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cds.querier;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.xml.parsers.ParserConfigurationException;

import org.astrogrid.datacenter.cdsdelegate.vizier.Target;
import org.astrogrid.datacenter.cdsdelegate.vizier.Unit;
import org.astrogrid.datacenter.cdsdelegate.vizier.VizierDelegate;
import org.astrogrid.datacenter.cdsdelegate.vizier.Wavelength;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Dataclass that represents a vizier cone search.
 * <p>
 * Contains fields for each of the required or optional search terms that can be passed to vizier.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class VizierCone {

    /**
     * 
     */
    public VizierCone() {

    }
    protected Target target;
    protected double radius;
    protected Unit unit;
    protected Wavelength wavelength;
    protected String additionalTerms;
    protected boolean metaData = false;
    
    /** double-dispatch thingie - chooses which query method of the vizier delegate to call, based on which search terms are currently set.
     * @param delegate - delegate to call method on
     * @return votable document returned by the delegate
     */ 
    public Document doDelegateQuery(VizierDelegate delegate) throws RemoteException, ParserConfigurationException, SAXException, IOException {
        //special case for metadata=true and nothing else
        if (metaData && target == null) {
            return delegate.metaAll();
        }
        // always require a Target and radius
        if (target == null || radius == 0.0 || unit == null) {
            throw new IllegalArgumentException("Must specify target, radius an unit");
        }
        if (metaData) {
            if (wavelength == null) {
                if (additionalTerms == null) {
                    return delegate.cataloguesMetaData(target,radius,unit);
                } else {
                    return delegate.cataloguesMetaData(target,radius,unit,additionalTerms);
                }                
            } else {
                if (additionalTerms == null) {
                    return delegate.cataloguesMetaData(target,radius,unit,wavelength);
                } else {
                    return delegate.cataloguesMetaData(target,radius,unit,wavelength,additionalTerms);
                }   
            }
        } else { // non meta-data query
            if (wavelength == null) {
                if (additionalTerms == null) {
                    return delegate.cataloguesData(target,radius,unit);
                } else {
                    return delegate.cataloguesData(target,radius,unit,additionalTerms);
                }                
            } else {
                if (additionalTerms == null) {
                    return delegate.cataloguesData(target,radius,unit,wavelength);
                } else {
                    return delegate.cataloguesData(target,radius,unit,wavelength,additionalTerms);
                }   
            }            
        }
    }

    /**
     * @param string
     */
    public void setAdditionalTerms(String string) {
        additionalTerms = string;
    }

    /**
     * @param d
     */
    public void setRadius(double d) {
        radius = d;
    }

    /**
     * @param target
     */
    public void setTarget(Target target) {
        this.target = target;
    }

    /**
     * @param unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * @param wavelength
     */
    public void setWavelength(Wavelength wavelength) {
        this.wavelength = wavelength;
    }

    /**
     * @param b
     */
    public void setMetaData(boolean b) {
        metaData = b;
    }

    /**
     * @return
     */
    public String getAdditionalTerms() {
        return additionalTerms;
    }

}


/* 
$Log: VizierCone.java,v $
Revision 1.2  2003/12/01 16:50:11  nw
first working tested version

Revision 1.1  2003/11/28 19:12:16  nw
getting there..
 
*/