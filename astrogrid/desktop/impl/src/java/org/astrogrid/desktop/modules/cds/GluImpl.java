/*$Id: GluImpl.java,v 1.5 2007/01/09 16:20:12 nw Exp $
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

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Glu;
import org.astrogrid.desktop.modules.cds.jglu.Jglu;
import org.astrogrid.desktop.modules.cds.jglu.JgluService;
import org.astrogrid.desktop.modules.cds.jglu.JgluServiceLocator;
import org.astrogrid.desktop.modules.system.Preference;

/** Implementation of the GLU service
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Aug-2005
 *@fixme work out why this never works - mabe replace with dynamic implementation?
 */
public class GluImpl implements Glu {

    private final JgluService serv;
    private final Preference endpoint;

	/** Construct a new GluImpl
     * @throws javax.xml.rpc.ServiceException
     * @throws MalformedURLException 
     * 
     */
    public GluImpl(Preference endpoint) throws javax.xml.rpc.ServiceException, MalformedURLException {
        super();
        serv = new JgluServiceLocator();    
        this.endpoint = endpoint;

    }


    /**
     * @see org.astrogrid.acr.cds.Glu#getURLfromTag(java.lang.String)
     */
    public String getURLfromTag(String arg0) throws ServiceException {
        try {
            return getJglu().getURLfromTag(arg0);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

	/**
	 * @return the jglu
	 * @throws javax.xml.rpc.ServiceException 
	 * @throws MalformedURLException 
	 */
	protected Jglu getJglu() throws MalformedURLException, javax.xml.rpc.ServiceException {
		return serv.getJglu(new URL(endpoint.getValue()));
	}

}


/* 
$Log: GluImpl.java,v $
Revision 1.5  2007/01/09 16:20:12  nw
updated to use preference.

Revision 1.4  2006/08/31 21:34:46  nw
minor tweaks and doc fixes.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.60.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.2.60.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/