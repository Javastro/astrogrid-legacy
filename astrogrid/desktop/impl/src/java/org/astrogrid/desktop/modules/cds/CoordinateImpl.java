/*$Id: CoordinateImpl.java,v 1.5 2007/01/09 16:20:12 nw Exp $
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

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Coordinate;
import org.astrogrid.desktop.modules.cds.astrocoo.AstroCoo;
import org.astrogrid.desktop.modules.cds.astrocoo.AstroCooService;
import org.astrogrid.desktop.modules.cds.astrocoo.AstroCooServiceLocator;
import org.astrogrid.desktop.modules.system.Preference;

/** Implementation of the Coordinate service
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Aug-2005
 *
 */
public class CoordinateImpl implements Coordinate {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CoordinateImpl.class);

    /** Construct a new CoordinateImpl
     * @throws MalformedURLException 
     * 
     */
    public CoordinateImpl(Preference endpoint) throws javax.xml.rpc.ServiceException, MalformedURLException  {
        super();
        serv = new AstroCooServiceLocator();
        this.endpoint = endpoint;
    }
    private final Preference endpoint;
    public final static String COORDINATE_WS_GLU_TAG = "CDS/ws/AstroCoo.WS";
	private final AstroCooService serv;   
    /**
     * @see org.astrogrid.acr.cds.Coordinate#convert(double, double, double, int)
     */
    public String convert(double arg0, double arg1, double arg2, int arg3) throws ServiceException {
        try {
            return getCoo().convert(arg0,arg1,arg2,arg3);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public String convertL(double arg0, double arg1, int arg2) throws ServiceException {
        try {
            return getCoo().convert(arg0,arg1,arg2);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public String convertE(int arg0, int arg1, double arg2, double arg3, double arg4, int arg5, double arg6, double arg7)
            throws ServiceException {
        try {
            return getCoo().convert(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public String convertLE(int arg0, int arg1, double arg2, double arg3, int arg4, double arg5, double arg6)
            throws ServiceException {
        try {
            return getCoo().convert(arg0,arg1,arg2,arg3,arg4,arg5,arg6);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

	/**
	 * @return the coo
	 * @throws javax.xml.rpc.ServiceException 
	 * @throws MalformedURLException 
	 */
	protected AstroCoo getCoo() throws MalformedURLException, javax.xml.rpc.ServiceException {
		return  serv.getAstroCoo(new URL(endpoint.getValue()));


	}

}


/* 
$Log: CoordinateImpl.java,v $
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