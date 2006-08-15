/*$Id: StapImpl.java,v 1.5 2006/08/15 10:16:24 nw Exp $
 * Created on 17-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.ivoa.DALImpl;

/** Implementaiton of a component that does siap queries.
 * @todo refine stap interface - simplify - there's loads of similar methods. overloaded methods aren't available via xmlrpc.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Oct-2005
 *
 */
public class StapImpl extends DALImpl implements Stap {

    private final SimpleDateFormat dateFormat;

	/** Construct a new SiapImpl
     * 
     */
    public StapImpl(Registry reg, MyspaceInternal ms) {
        super(reg,ms);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        
    }
    
    private URL constructQueryPrim(URI arg0, Calendar start, Calendar end) throws InvalidArgumentException, NotFoundException {
        return addOption(
        		addOption(resolveEndpoint(arg0),"START",dateFormat.format(start.getTime()))
        		, "END", dateFormat.format(end.getTime()));
        /*
        StringBuffer urlSB = new StringBuffer(url);
        // add a query part, if not already there
        char lastch = url.charAt(url.length() - 1);
        if (lastch != '?' && lastch != '&')
            urlSB.append((url.indexOf('?') > 0) ? '&' : '?');
        urlSB.append("START=").append(dateFormat.format(start.getTime()))
        .append("&END=").append(dateFormat.format(end.getTime()));
        try {
            return new URL(urlSB.toString());
        } catch (MalformedURLException e) {
            throw new InvalidArgumentException("Failed to construct query URL",e);            
        } */       
    }
    

    private URL constructQueryPrim(URI service, Calendar start, Calendar end, double ra,double dec) throws InvalidArgumentException, NotFoundException {
        return addOption(constructQueryPrim(service,start,end),"POS",Double.toString(ra).concat(",").concat(Double.toString(dec)));
    }
    
    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQuery(URI service, Calendar start, Calendar end)
            throws InvalidArgumentException, NotFoundException {
        return constructQueryPrim(service,start,end);
    }
    
    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryF(java.net.URI, double, double, double, java.lang.String)
     */
    public URL constructQueryF(URI service, Calendar start, Calendar end, String format)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQueryPrim(service,start,end),"FORMAT",format);
    }
    
    
    
    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQuery(URI service, Calendar start, Calendar end, double ra, double dec, double size)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQueryPrim(service, start, end, ra, dec),"SIZE",Double.toString(size));
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryF(java.net.URI, double, double, double, java.lang.String)
     */
    public URL constructQueryF(URI service, Calendar start, Calendar end, double ra, double dec, double size, String format)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQuery(service,start, end, ra, dec, size),"FORMAT",format);
    }
    
    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryS(java.net.URI, double, double, double, double)
     */
    public URL constructQueryS(URI service, Calendar start, Calendar end, double ra, double dec, double ra_size, double dec_size)
            throws InvalidArgumentException, NotFoundException {
        if (ra_size == dec_size) {
            return constructQuery(service, start, end, ra, dec,ra_size);
        } else {
            String sizeStr = Double.toString(ra_size) + "," + Double.toString(dec_size);
            return addOption(constructQueryPrim(service,start, end, ra,dec),"SIZE",sizeStr);
        }
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuerySF(java.net.URI, double, double, double, double, java.lang.String)
     */
    public URL constructQuerySF(URI service, Calendar start, Calendar end, double ra, double dec, double ra_size, double dec_size, String format)
            throws InvalidArgumentException, NotFoundException {
            return addOption(constructQueryS(service, start, end, ra, dec, ra_size, dec_size),"FORMAT",format);        
    }    

 
	public String getRegistryAdqlQuery() {
        return "Select * from Registry where " +
        " @xsi:type like '%SimpleTimeAccess'  " +
        " and @status = 'active'";
	}

	public String getRegistryXQuery() {
		return "//vor:Resource[@xsi:type &= '*SimpleTimeAccess' and ( not ( @status = 'inactive' or @status='deleted'))]";

	}

}


/* 
$Log: StapImpl.java,v $
Revision 1.5  2006/08/15 10:16:24  nw
migrated from old to new registry models.

Revision 1.4  2006/06/15 09:45:39  nw
improvements coming from unit testing

Revision 1.3  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.2  2006/03/16 14:03:57  KevinBenson
I had a bunch of printlns in this file for some reason

Revision 1.1  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

Revision 1.1  2005/10/17 16:02:45  nw
added siap and cone interfaces
 
*/