/*$Id: SimplestQuerierSPI.java,v 1.1 2003/12/01 15:31:10 nw Exp $
 * Created on 01-Dec-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.spi;

import org.astrogrid.datacenter.queriers.QueryResults;
import org.w3c.dom.Element;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Dec-2003
 *
 */
public abstract class SimplestQuerierSPI extends BaseQuerierSPI implements QuerierSPI {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#getPluginInfo()
     */
    public abstract String getPluginInfo() ;

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#doQuery(java.lang.Object, java.lang.Class)
     */
    public abstract QueryResults doQuery(Element e) throws Exception ;

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#doQuery(java.lang.Object, java.lang.Class)
     */
    public final QueryResults doQuery(Object o, Class type) throws Exception {
        return doQuery((Element)o);
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#getTranslatorMap()
     */
    public final TranslatorMap getTranslatorMap() {
        return new IdTranslator.DummyTranslatorMap();
    }

}


/* 
$Log: SimplestQuerierSPI.java,v $
Revision 1.1  2003/12/01 15:31:10  nw
simplest possible querier
 
*/