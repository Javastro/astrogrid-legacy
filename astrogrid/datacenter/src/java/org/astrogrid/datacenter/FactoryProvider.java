/*
 * $Id: FactoryProvider.java,v 1.1 2003/08/20 14:50:25 nw Exp $
 * Created on 20-Aug-2003
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.datacenter;

import org.astrogrid.datacenter.job.JobFactory;
import org.astrogrid.datacenter.myspace.MySpaceFactory;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryFactory;
import org.astrogrid.datacenter.votable.VOTableFactory;

/** interface to a collection of factories 
 * describes a container that maintains and configures the various factories used within the data access component.
 * <p>this interface just provides access to the factories - no details of updating / creating factories is specified
 * @author noel
 * @see org.astrogrid.datacenter.config.FactoryProvider
 */
public interface FactoryProvider {
    /**
     * Access the job factory
     */
    public abstract JobFactory getJobFactory();
    /**
     * Access the my space factory 
     */
    public abstract MySpaceFactory getMySpaceFactory();
    /** access the votable factory */
	public abstract VOTableFactory getVOTableFactory();
    /** Access a query factory
     * @param catalogName name of the catalog to retrieve query factory for.
     * @return the query factory associated with this catalog, or the default query factory if a catalog specific one is not specified.
     * @throws QueryException if an appropriate query factory cannot be retrieved.
     * @see #isQueryFactory
     */
    public abstract QueryFactory getQueryFactory(String catalogName)
        throws QueryException;
    
    /** Access the default implementation of the query factory
     * 
     * @return the default query factory
     */
    public abstract QueryFactory getDefaultQueryFactory();
    /** verify whether a particular query factory is present
     * 
     * @param catalogName name of the catalog to check query factory for
     * @return true if the query factory for this catalog is present.
     */
    public abstract boolean isQueryFactoryLoaded(String catalogName);
}
/*
$Log: FactoryProvider.java,v $
Revision 1.1  2003/08/20 14:50:25  nw
added interface to a factory management system

*/