/*$Id: VizieRImpl.java,v 1.6 2007/01/29 11:11:37 nw Exp $
 * Created on 16-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.cds;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.VizieR;
import org.astrogrid.desktop.modules.cds.vizier.VizieRService;
import org.astrogrid.desktop.modules.cds.vizier.VizieRServiceLocator;
import org.astrogrid.desktop.modules.system.Preference;
import org.w3c.dom.Document;

/** Implementation of the vizier service
 * 
 * @todo later, if demand, cache delegate between uses, and listen for preference
 * updates before invalidating it.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 16-Aug-2005
 *
 */
public class VizieRImpl implements VizieR {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(VizieRImpl.class);

    /** Construct a new VizieRImpl
     * @throws MalformedURLException 
     * 
     */
    public VizieRImpl(Preference endpoint) throws javax.xml.rpc.ServiceException, MalformedURLException {        
        super();
        serv = new VizieRServiceLocator();
        this.endpoint = endpoint;
    }
    public static final String VIZIER_WS_GLU_TAG= "CDS/ws/VizieR.WS";
	private final VizieRService serv;
	private final Preference endpoint;
    /**
     * @see org.astrogrid.acr.cds.VizieR#cataloguesMetaData(java.lang.String, double, java.lang.String, java.lang.String)
     */
    public Document cataloguesMetaData(String arg0, double arg1, String arg2, String arg3)
            throws ServiceException {
        try {
            String r= createVizDelegate().cataloguesMetaData(arg0,arg1,arg2,arg3);
            ByteArrayInputStream is = new ByteArrayInputStream(r.getBytes());
            return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Document cataloguesMetaDataWavelength(String arg0, double arg1, String arg2, String arg3, String arg4)
            throws ServiceException {
        try {
            String r= createVizDelegate().cataloguesMetaData(arg0,arg1,arg2,arg3,arg4);
            ByteArrayInputStream is = new ByteArrayInputStream(r.getBytes());
            return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.cds.VizieR#cataloguesData(java.lang.String, double, java.lang.String, java.lang.String)
     */
    public Document cataloguesData(String arg0, double arg1, String arg2, String arg3)
            throws ServiceException {
        try {
            String r= createVizDelegate().cataloguesData(arg0,arg1,arg2,arg3);
            ByteArrayInputStream is = new ByteArrayInputStream(r.getBytes());
            return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    
    public Document cataloguesDataWavelength(String arg0, double arg1, String arg2, String arg3, String arg4)
            throws ServiceException {
        try {
            String r= createVizDelegate().cataloguesData(arg0,arg1,arg2,arg3,arg4);
            ByteArrayInputStream is = new ByteArrayInputStream(r.getBytes());
            return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.cds.VizieR#metaAll()
     */
    public Document metaAll() throws ServiceException {
        try {
        String r = createVizDelegate().metaAll();
        ByteArrayInputStream is = new ByteArrayInputStream(r.getBytes());
        return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

	/**
	 * @return the viz
	 * @throws javax.xml.rpc.ServiceException 
	 * @throws MalformedURLException 
	 */
	protected org.astrogrid.desktop.modules.cds.vizier.VizieR createVizDelegate() throws MalformedURLException, javax.xml.rpc.ServiceException {
		return   serv.getVizieR(new URL(endpoint.getValue()));
	}

}


/* 
$Log: VizieRImpl.java,v $
Revision 1.6  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.5  2007/01/09 16:20:12  nw
updated to use preference.

Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.60.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.3.60.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/31 15:30:46  nw
added glu tags.

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/