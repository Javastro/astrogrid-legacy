/*$Id: SimplestQuerierSPI.java,v 1.2 2004/01/15 12:26:14 nw Exp $
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

/** A SPI abstract base class for plugin authors who'd rather do it all their own way. 
 * <p>
 * The language translator mechanism is effectively disabled, so that the query document Element is passed
 * unchanged into  a {@link #doQuery(Element)} method.
 * Authors need to implement this method, and also {@link #getPluginInfo}. Any cleanup code can be 
 * added by overriding the default implementation of {@link #close} 
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Dec-2003
 *
 */
public abstract class SimplestQuerierSPI extends BaseQuerierSPI implements QuerierSPI {
   
    public abstract String getPluginInfo() ;

   /** Implement this method to perform the querying 
    * 
    * @param e the input query document
    * @return a query results object
    * @throws Exception
    */
    public abstract QueryResults doQuery(Element e) throws Exception ;

    public final QueryResults doQuery(Object o, Class type) throws Exception {
        return doQuery((Element)o);
    }

   /** @return a map that always returns a {@link IdTranslator} */
    public final TranslatorMap getTranslatorMap() {
        return new IdTranslator.DummyTranslatorMap();
    }

}


/* 
$Log: SimplestQuerierSPI.java,v $
Revision 1.2  2004/01/15 12:26:14  nw
improved documentation

Revision 1.1  2003/12/01 15:31:10  nw
simplest possible querier
 
*/