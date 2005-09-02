/*$Id: GluImpl.java,v 1.2 2005/09/02 14:03:34 nw Exp $
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

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Glu;
import org.astrogrid.desktop.modules.cds.jglu.Jglu;
import org.astrogrid.desktop.modules.cds.jglu.JgluService;
import org.astrogrid.desktop.modules.cds.jglu.JgluServiceLocator;

/** Implementation of the GLU service
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Aug-2005
 *
 */
public class GluImpl implements Glu {

    /** Construct a new GluImpl
     * @throws javax.xml.rpc.ServiceException
     * 
     */
    public GluImpl() throws javax.xml.rpc.ServiceException {
        super();
        JgluService serv = new JgluServiceLocator(); // @todo later maybe supply url here..
        this.jglu = serv.getJglu();
    }
    protected final Jglu jglu;

    /**
     * @see org.astrogrid.acr.cds.Glu#getURLfromTag(java.lang.String)
     */
    public String getURLfromTag(String arg0) throws ServiceException {
        try {
            return jglu.getURLfromTag(arg0);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

}


/* 
$Log: GluImpl.java,v $
Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/