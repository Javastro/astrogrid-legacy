/*$Id: NullVOTableFactory.java,v 1.1 2003/08/21 12:27:24 nw Exp $
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

import org.astrogrid.datacenter.config.ConfigurableImpl;
import org.astrogrid.datacenter.myspace.Allocation;
import org.astrogrid.datacenter.myspace.MySpaceFactory;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.votable.VOTable;
import org.astrogrid.datacenter.votable.VOTableException;
import org.astrogrid.datacenter.votable.VOTableFactory;

/** null implementation of a votable factory.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class NullVOTableFactory
    extends ConfigurableImpl
    implements VOTableFactory {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.votable.VOTableFactory#stream(org.astrogrid.datacenter.query.Query, org.astrogrid.datacenter.myspace.Allocation, org.astrogrid.datacenter.myspace.MySpaceFactory)
     */
    public void stream(Query query, Allocation allocation, MySpaceFactory fac)
        throws VOTableException {

    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.votable.VOTableFactory#createVOTable(org.astrogrid.datacenter.query.Query)
     */
    public VOTable createVOTable(Query query) throws VOTableException {
        return null;
    }

}


/* 
$Log: NullVOTableFactory.java,v $
Revision 1.1  2003/08/21 12:27:24  nw
added set of null-factory implementations.
 
*/