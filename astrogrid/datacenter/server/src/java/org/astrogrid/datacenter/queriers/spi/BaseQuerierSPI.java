/*$Id: BaseQuerierSPI.java,v 1.3 2003/12/01 16:11:30 nw Exp $
 * Created on 26-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.spi;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import org.astrogrid.util.Workspace;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2003
 *
 */
public abstract class BaseQuerierSPI implements QuerierSPI {
    protected Workspace workspace;
    protected final static  Log log = LogFactory.getLog("QuerierSPI");
    protected static final SimpleTranslatorMap map = new SimpleTranslatorMap();
    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#getTranslatorMap()
     */
    public TranslatorMap getTranslatorMap() {
        return map;
    }



    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#receiveWorkspace(org.astrogrid.util.Workspace)
     */
    public void setWorkspace(Workspace ws) {
        this.workspace = ws;
    }
    
    /** default null implementation - override to clean up your own resources */
    public void close() throws Exception {
    }

}


/* 
$Log: BaseQuerierSPI.java,v $
Revision 1.3  2003/12/01 16:11:30  nw
removed config interface.

Revision 1.2  2003/11/27 17:28:09  nw
finished plugin-refactoring

Revision 1.1  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.
 
*/