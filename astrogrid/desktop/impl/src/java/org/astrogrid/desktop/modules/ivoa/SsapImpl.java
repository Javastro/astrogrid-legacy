/*$Id: SsapImpl.java,v 1.19 2009/03/04 18:43:09 nw Exp $
 * Created on 27-Jan-2006
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

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.StarTable;

/** Simple Spectral Access client.
 * complies with v1.04 of the SSAP spec.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 */
public class SsapImpl extends DALImpl implements Ssap {

    /** Construct a new SsapImpl
     * @param reg
     * @param ms
     * @param v 
     * @param cx 
     */
    public SsapImpl(final Registry reg, final FileSystemManager v, final UIContext cx) {
        super(reg, v, cx);
    }

    /**
     * @see org.astrogrid.acr.ivoa.Ssap#getRegistryQuery()
     */
    public String getRegistryAdqlQuery() {
        return "Select * from Registry r where ( " +
        " r.capability/@xsi:type like '%SimpleSpectralAccess'  " 
        +" or r.capability/@standardID = '" + StandardIds.SSAP_1_0 + "' )";
//       " and ( not (@status = 'inactive' or @status='deleted') )";
    }
    public String getRegistryQuery() {
    	return "type = Spectral";
    }
    
    public String getRegistryXQuery() {
		return "//vor:Resource[("
		+ "(capability/@xsi:type &= '*SimpleSpectralAccess')"
		+ " or " 
		+ " (capability/@standardID = '" + StandardIds.SSAP_1_0 +"' )"
		+ " ) and ( not ( @status = 'inactive' or @status='deleted'))]";
    }

    protected URL findAccessURL(final Service s) throws InvalidArgumentException {
        if (!(s instanceof SsapService)) {
            throw new InvalidArgumentException(s.getId() + " does not provide a SSAP capability");
        }
        final SsapCapability cap = ((SsapService)s).findSsapCapability();
        final Interface[] interfaces = cap.getInterfaces();
        Interface std = null;
        switch (interfaces.length) {
            case 0: throw new InvalidArgumentException(s.getId() + " does not provide an interface in it's ssap capability");
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
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQuery(final URI service, final double ra, final double dec, final double size)
            throws InvalidArgumentException, NotFoundException {
        return addOption(addOption(
                addOption(
                        resolveEndpoint(service),"POS",Double.toString(ra) + "," + Double.toString(dec))
                    ,"SIZE",Double.toString(size)
                    ),"REQUEST","queryData"
        
        );
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryS(java.net.URI, double, double, double, double)
     */
    public URL constructQueryS(final URI service, final double ra, final double dec, final double ra_size, final double dec_size)
            throws InvalidArgumentException, NotFoundException {        
        if (ra_size == dec_size) {
            return constructQuery(service,ra,dec,ra_size);
        } else {
            final String sizeStr = Double.toString(ra_size) + "," + Double.toString(dec_size);
            return addOption(
            addOption(
                    addOption(
                            resolveEndpoint(service),"POS",Double.toString(ra) + "," + Double.toString(dec))
                        ,"SIZE",sizeStr
                        ),"REQUEST","queryData"
            );
        }
    }
  
    /** overridden to inject our own structure builder */
    @Override
    protected StructureBuilder newStructureBuilder() {
        return new SsapStructureBuilder();
    }
    
    /** An Extended structyre builder that selects one resource table out
     * of the response, and ignores the rest.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 26, 20082:54:38 PM
     */
    public static class SsapStructureBuilder extends StructureBuilder {
        boolean skipNextTable = false;
        boolean resultsTableParsed = false;
        
        @Override
        public void resource(final String name, final String id, final String type)
                throws SAXException {
            skipNextTable = ! "results".equalsIgnoreCase(type);
            
        }
        
        @Override
        public void startTable(final StarTable t) throws SAXException {
            if (skipNextTable || resultsTableParsed) {
                return;
            }
            super.startTable(t);
        }
        
        @Override
        public void rowData(final Object[] cells) throws SAXException {
            if (skipNextTable || resultsTableParsed) {
                return;
            }
            super.rowData(cells);
        }
        
        @Override
        public void endTable() throws SAXException {
            if (skipNextTable || resultsTableParsed) {
                return;
            }
            resultsTableParsed = true;
            super.endTable();
        }
    }
    

    
}


/* 
$Log: SsapImpl.java,v $
Revision 1.19  2009/03/04 18:43:09  nw
Complete - taskMove DAL over to VFS

Revision 1.18  2009/02/13 17:46:20  nw
Complete - taskUse SRQL in AR

Revision 1.17  2008/12/03 19:40:56  nw
Complete - taskDAL: add error detections and parsing improvements as used in astroscope retrievers.

Revision 1.16  2008/12/01 23:31:39  nw
Complete - taskDAL: add error detections and parsing improvements as used in astroscope retrievers.

Revision 1.15  2008/11/04 14:35:51  nw
javadoc polishing

Revision 1.14  2008/03/30 18:02:19  nw
FIXED - bug 2689: upgrade to handle latest ssa standard.
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2689

Revision 1.13  2008/03/10 14:14:01  nw
fixed fallthough case statements.

Revision 1.12  2008/03/05 11:52:13  nw
Complete - task 316: use Guy's standardIDs.

Revision 1.11  2008/01/25 07:53:25  nw
Complete - task 134: Upgrade to reg v1.0

Revision 1.10  2007/04/18 15:47:05  nw
tidied up voexplorer, removed front pane.

Revision 1.8  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.7  2006/08/31 21:34:46  nw
minor tweaks and doc fixes.

Revision 1.6  2006/08/15 10:13:50  nw
migrated from old to new registry models.

Revision 1.5  2006/06/27 19:13:07  nw
adjusted todo tags.

Revision 1.4  2006/06/15 09:49:08  nw
improvements coming from unit testing

Revision 1.3  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.2  2006/03/13 18:29:32  nw
fixed queries to not restrict to @status='active'

Revision 1.1  2006/02/02 14:49:49  nw
added starter implementation of ssap.
 
*/