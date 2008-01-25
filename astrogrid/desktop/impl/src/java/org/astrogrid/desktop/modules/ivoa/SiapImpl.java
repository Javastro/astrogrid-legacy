/*$Id: SiapImpl.java,v 1.14 2008/01/25 07:53:25 nw Exp $
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

import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.StarTable;

/** Implementaiton of a component that does siap queries.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Oct-2005
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


    public String getRegistryQuery() {
    	return getRegistryAdqlQuery();
    }

	public String getRegistryAdqlQuery() {
        return "Select * from Registry r where " +
        " r.capability/@xsi:type like '%SimpleImageAccess'  " 
        +" or r.capability/@standardID = '" + SiapCapability.CAPABILITY_ID + "' ";
//@issue        " and ( not (@status = 'inactive' or @status='deleted') )";
	}

	public String getRegistryXQuery() {
		return "//vor:Resource[(" +
		"(capability/@xsi:type &= '*SimpleImageAccess')"
		+ " or "
		+ "(capability/@standardID = '" + SiapCapability.CAPABILITY_ID + "' )"
		+") and ( not ( @status = 'inactive' or @status='deleted'))]";
	
	}
	
	// override the table parser to use siap datamodel.
	protected StructureBuilder newStructureBuilder() {
		return new SiaStructureBuilder();
	}
	
	protected static class SiaStructureBuilder extends StructureBuilder {
		public void startTable(StarTable t) throws SAXException {
			super.startTable(t);
			// now iterate over the keys, mapping them from ucds if recognized.
			for (int i = 0; i < keys.length; i++) {
				String term = Sia10Map.mapUCD(keys[i]);
				if (term != null) {
					keys[i] = term;
				}
			}
		}
	}

	protected URL findAccessURL(Service s) throws InvalidArgumentException {
	    if (!(s instanceof SiapService)) {
	        throw new InvalidArgumentException(s.getId() + " does not provide a SIAP capability");
	    }
	    SiapCapability cap = ((SiapService)s).findSiapCapability();
	    Interface[] interfaces = cap.getInterfaces();
	    Interface std = null;
	    switch (interfaces.length) {
	        case 0: throw new InvalidArgumentException(s.getId() + " does not provide an interface in it's siap capability");
	        case 1:
	            std = interfaces[0];
	        default:    
	            for (int i = 0; i < interfaces.length; i++) {
	                Interface cand = interfaces[i];
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
	}
}



/* 
$Log: SiapImpl.java,v $
Revision 1.14  2008/01/25 07:53:25  nw
Complete - task 134: Upgrade to reg v1.0

Revision 1.13  2007/06/18 17:02:24  nw
javadoc fixes.

Revision 1.12  2007/04/18 15:47:05  nw
tidied up voexplorer, removed front pane.

Revision 1.10  2007/01/29 16:45:08  nw
cleaned up imports.

Revision 1.9  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.8  2006/10/11 10:39:01  nw
enhanced dal support.

Revision 1.7  2006/08/31 21:34:46  nw
minor tweaks and doc fixes.

Revision 1.6  2006/08/15 10:13:50  nw
migrated from old to new registry models.

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