/*$Id: StapImpl.java,v 1.20 2009/03/26 18:04:13 nw Exp $
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
import java.util.Date;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.desktop.modules.ivoa.DALImpl;
import org.astrogrid.desktop.modules.ivoa.DatasetSaver;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** Client for Simple Time Access Protocol.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Oct-2005
 * @modified Noel Winstanley changed from using Calendar to using Date.
 *@TEST
 */
public class StapImpl extends DALImpl implements Stap {

    private final SimpleDateFormat dateFormat;

	/** Construct a new SiapImpl
	 * @param v 
	 * @param cx 
     * 
     */
    public StapImpl(final Registry reg, final FileSystemManager v, final UIContext cx) {
        super(reg,v, cx);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // why has this format got '-' in it? 
        
    }
    
    private URL constructQueryPrim(final URI arg0, final Date start, final Date end) throws InvalidArgumentException, NotFoundException {
        return addOption(
        		addOption(resolveEndpoint(arg0),"START",dateFormat.format(start))
        		, "END", dateFormat.format(end));
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
    

    private URL constructQueryPrim(final URI service, final Date start, final Date end, final double ra,final double dec) throws InvalidArgumentException, NotFoundException {
        return addOption(constructQueryPrim(service,start,end),"POS",Double.toString(ra).concat(",").concat(Double.toString(dec)));
    }
    
    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQuery(final URI service, final Date start, final Date end)
            throws InvalidArgumentException, NotFoundException {
        return constructQueryPrim(service,start,end);
    }
    
    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryF(java.net.URI, double, double, double, java.lang.String)
     */
    public URL constructQueryF(final URI service, final Date start,final Date end, final String format)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQueryPrim(service,start,end),"FORMAT",format);
    }
    
    
    
    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQueryP(final URI service, final Date start, final Date end, final double ra, final double dec, final double size)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQueryPrim(service, start, end, ra, dec),"SIZE",Double.toString(size));
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryF(java.net.URI, double, double, double, java.lang.String)
     */
    public URL constructQueryPF(final URI service,final Date start, final Date end, final double ra, final double dec, final double size, final String format)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQueryP(service,start, end, ra, dec, size),"FORMAT",format);
    }
    
    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryS(java.net.URI, double, double, double, double)
     */
    public URL constructQueryS(final URI service, final Date start, final Date end, final double ra, final double dec, final double ra_size, final double dec_size)
            throws InvalidArgumentException, NotFoundException {
        if (ra_size == dec_size) {
            return constructQueryP(service, start, end, ra, dec,ra_size);
        } else {
            final String sizeStr = Double.toString(ra_size) + "," + Double.toString(dec_size);
            return addOption(constructQueryPrim(service,start, end, ra,dec),"SIZE",sizeStr);
        }
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuerySF(java.net.URI, double, double, double, double, java.lang.String)
     */
    public URL constructQuerySF(final URI service, final Date start, final Date end, final double ra, final double dec, final double ra_size, final double dec_size, final String format)
            throws InvalidArgumentException, NotFoundException {
            return addOption(constructQueryS(service, start, end, ra, dec, ra_size, dec_size),"FORMAT",format);        
    }    

 
	public String getRegistryAdqlQuery() {
        return "Select * from Registry r where ( " +
        " r.capability/@xsi:type like '%SimpleTimeAccess'  " +
        " or r.capability/@standardID = '" + StandardIds.STAP_1_0 + "' " +
        ") and @status = 'active'";
	}

	public String getRegistryXQuery() {
		return "//vor:Resource[("
	+"(capability/@xsi:type &= '*SimpleTimeAccess')"
	+ " or "
	+ " (capability/@standardID = '" + StandardIds.STAP_1_0 + "')"
	+ ") and ( not ( @status = 'inactive' or @status='deleted'))]";
	}

	public String getRegistryQuery() {
		return "type = Time";
	}

    @Override
    protected URL findAccessURL(final Service s) throws InvalidArgumentException {
        if (s instanceof StapService) {
            final StapCapability cap = ((StapService)s).findStapCapability();
            final Interface[] interfaces = cap.getInterfaces();
            Interface std = null;
            switch (interfaces.length) {
                case 0: throw new InvalidArgumentException(s.getId() + " does not provide an interface in it's stap capability");
                case 1:
                    std = interfaces[0];
                    break;
                default:    
                    for (int i = 0; i < interfaces.length; i++) {
                        final Interface cand = interfaces[i];
                        if ("std".equals(cand.getRole())) {
                            std = cand;
                        }
                        // none marked as std - just choose the first.
                        if (std == null) {
                            std = interfaces[0];
                        }
                    }
            }                            
            return std.getAccessUrls()[0].getValue();
        } else {
            throw new InvalidArgumentException(s.getId() + " does not provide a STAP capability");

        }   
    }

    @Override
    protected DatasetSaver newDatasetSaver() {
        final DatasetSaver saver = new DatasetSaver();
        saver.setLookForResultTable(false);
        return saver;
    }
}


/* 
$Log: StapImpl.java,v $
Revision 1.20  2009/03/26 18:04:13  nw
source code improvements - cleaned imports, @override, etc.

Revision 1.19  2009/03/04 18:43:10  nw
Complete - taskMove DAL over to VFS

Revision 1.18  2009/02/16 16:54:39  nw
Complete - taskUse SRQL in AR

Revision 1.17  2008/12/03 19:40:56  nw
Complete - taskDAL: add error detections and parsing improvements as used in astroscope retrievers.

Revision 1.16  2008/11/04 14:35:47  nw
javadoc polishing

Revision 1.15  2008/05/09 11:31:55  nw
Incomplete - task 392: joda time

Revision 1.14  2008/04/23 10:52:21  nw
marked as needing test.

Revision 1.13  2008/03/10 14:14:08  nw
fixed fallthough case statements.

Revision 1.12  2008/03/05 11:52:13  nw
Complete - task 316: use Guy's standardIDs.

Revision 1.11  2008/01/25 07:53:25  nw
Complete - task 134: Upgrade to reg v1.0

Revision 1.10  2007/10/23 09:26:00  nw
RESOLVED - bug 2189: How to query stap services
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2189

Revision 1.9  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

Revision 1.7  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.6  2006/10/11 10:39:01  nw
enhanced dal support.

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