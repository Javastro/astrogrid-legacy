/*$Id: NullQueryFactory.java,v 1.2 2003/08/28 15:52:00 mch Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.impl.empty;

import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryFactory;
import org.w3c.dom.Element;

/** null implementation of a query factory
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class NullQueryFactory
    implements QueryFactory {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.query.QueryFactory#createQuery(org.w3c.dom.Element)
     */
    public Query createQuery(Element queryElement) throws QueryException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.query.QueryFactory#execute(org.astrogrid.datacenter.query.Query)
     */
    public void execute(Query query) throws QueryException {

    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.query.QueryFactory#end()
     */
    public void end() {

    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.query.QueryFactory#getImplementation()
     */
    public Object getImplementation() {
        return null;
    }

}


/*
$Log: NullQueryFactory.java,v $
Revision 1.2  2003/08/28 15:52:00  mch
New Configuration package

Revision 1.1  2003/08/21 12:27:24  nw
added set of null-factory implementations.

*/
