/*$Id: VizierQuerier.java,v 1.1 2003/11/18 11:23:49 nw Exp $
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

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.Query;
import org.astrogrid.datacenter.queriers.QueryResults;

import org.astrogrid.datacenter.cdsdelegate.vizier.*;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 *
 */
public class VizierQuerier extends DatabaseQuerier {

    public VizierQuerier() throws ServiceException {
        delegate = new VizierDelegate();
    }
    
    protected VizierDelegate delegate;


    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.DatabaseQuerier#queryDatabase(org.astrogrid.datacenter.queriers.Query)
     */
    public QueryResults queryDatabase(Query aQuery)
        throws DatabaseAccessException {
          // determine type of query
          // translate depending on type & do call
          Document result = null; 
          // this architecture is currently broken - it04 delegates will need a different back end to service the requests
          // hence this wrapped query format, (where the query may be in a range of formats) won't work anymore.
          // maybe need to talk this though with martin - maybe this would have worked..
          
          // build QueryResults from return value.
          return new DocumentQueryResults(result);
    }

}


/* 
$Log: VizierQuerier.java,v $
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/