/*$Id: VizierQuerier.java,v 1.5 2003/12/01 16:50:11 nw Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cds.querier;

import javax.xml.rpc.ServiceException;

import org.astrogrid.datacenter.cdsdelegate.vizier.VizierDelegate;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.spi.BaseQuerierSPI;
import org.astrogrid.datacenter.queriers.spi.QuerierSPI;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 *@todo implement this.
 */
public class VizierQuerier extends BaseQuerierSPI implements QuerierSPI{

    /** @todo check configuration for endpoint setting before settling with default */
    public VizierQuerier() throws ServiceException {       
        delegate = new VizierDelegate();
    }
    static {
        map.add("http://tempuri.org/adql",new AdqlVizierTranslator());
    }    
    protected VizierDelegate delegate;

    public String getPluginInfo() {
        return "Proxy Dataserver that links CDS Vizier into Astrogrid";
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#doQuery(java.lang.Object, java.lang.Class)
     */
    public QueryResults doQuery(Object arg0, Class arg1) throws Exception {
        if (!(arg0 instanceof VizierCone)) {
            throw new IllegalArgumentException("I expect a Cone parameter");
        }
        VizierCone cone = (VizierCone) arg0;              
        Document result = cone.doDelegateQuery(delegate);
        return new DocumentQueryResults(result);
    }

}


/* 
$Log: VizierQuerier.java,v $
Revision 1.5  2003/12/01 16:50:11  nw
first working tested version

Revision 1.4  2003/11/28 19:12:16  nw
getting there..

Revision 1.3  2003/11/25 11:14:51  nw
upgraded to new service interface

Revision 1.2  2003/11/20 15:47:18  nw
improved testing

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/