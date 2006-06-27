/*$Id: SiapImpl.java,v 1.5 2006/06/27 19:13:07 nw Exp $
 * Created on 17-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;

/** Implementaiton of a component that does siap queries.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Oct-2005
 *
 */
public class SiapImpl extends DALImpl implements Siap {

    /** Construct a new SiapImpl
     * 
     */
    public SiapImpl(Registry reg, MyspaceInternal ms) {
        super(reg,ms);
        
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQuery(URI service, double ra, double dec, double size)
            throws InvalidArgumentException, NotFoundException {
        return addOption(
        		addOption(
        				resolveEndpoint(service),"POS",Double.toString(ra) + "," + Double.toString(dec))
        			,"SIZE",Double.toString(size));
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryF(java.net.URI, double, double, double, java.lang.String)
     */
    public URL constructQueryF(URI service, double ra, double dec, double size, String format)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQuery(service,ra,dec,size),"FORMAT",format);
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryS(java.net.URI, double, double, double, double)
     */
    public URL constructQueryS(URI service, double ra, double dec, double ra_size, double dec_size)
            throws InvalidArgumentException, NotFoundException {        
        if (ra_size == dec_size) {
            return constructQuery(service,ra,dec,ra_size);
        } else {
            String sizeStr = Double.toString(ra_size) + "," + Double.toString(dec_size);
            return addOption(
            		addOption(
            				resolveEndpoint(service),"POS",Double.toString(ra) + "," + Double.toString(dec))
            			,"SIZE",sizeStr);
        }
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuerySF(java.net.URI, double, double, double, double, java.lang.String)
     */
    public URL constructQuerySF(URI service, double ra, double dec, double ra_size, double dec_size, String format)
            throws InvalidArgumentException, NotFoundException {
            return addOption(constructQueryS(service,ra,dec,ra_size,dec_size),"FORMAT",format);        
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#getRegistryQueryToListSiap()
     */
    public String getRegistryQuery() {
        return "Select * from Registry where " +
        " @xsi:type like '%SimpleImageAccess'  " ;
//@fixme        " and ( not (@status = 'inactive' or @status='deleted') )";
    }

}


/* 
$Log: SiapImpl.java,v $
Revision 1.5  2006/06/27 19:13:07  nw
adjusted todo tags.

Revision 1.4  2006/06/15 09:49:07  nw
improvements coming from unit testing

Revision 1.3  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.2  2006/03/13 18:29:32  nw
fixed queries to not restrict to @status='active'

Revision 1.1  2005/10/17 16:02:45  nw
added siap and cone interfaces
 
*/