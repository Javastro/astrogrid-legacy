/*
 * $Id: FactoryProvider.java,v 1.5 2003/08/28 15:30:32 nw Exp $
 * Created on 20-Aug-2003
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.astrogrid.datacenter;

import org.astrogrid.datacenter.job.JobFactory;
//import org.astrogrid.datacenter.myspace.MySpaceFactory; no longer used
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryFactory;
//import org.astrogrid.datacenter.votable.VOTableFactory;

/** interface to a collection of factories
 * describes a container that maintains and configures the various factories used within the data access component.
 * <p>this interface just provides access to the factories - no details of updating / creating factories is specified
 * @author noel
 * @see org.astrogrid.datacenter.config.FactoryManager
 */
public interface FactoryProvider {
    /**
     * Access the job factory
     */
    public abstract JobFactory getJobFactory();
    /**
     * Access the my space factory
     *
    public abstract MySpaceFactory getMySpaceFactory(); no longer used
    */
    /** access the votable factory */
//   public abstract VOTableFactory getVOTableFactory();
    /** Access a query factory
     * @param catalogName name of the catalog to retrieve query factory for.
     * @return the query factory associated with this catalog, or the default query factory if a catalog specific one is not specified.
     * @throws QueryException if an appropriate query factory cannot be retrieved.
     * @see #isQueryFactoryAvailable
     */
    public abstract QueryFactory getQueryFactory(String catalogName)
        throws QueryException;

    /** Access the default implementation of the query factory
     *
     * @return the default query factory
     */
    public abstract QueryFactory getDefaultQueryFactory();
    /** verify whether a query factory specific to a particular catalogue is present
     * note that if it is not available, the default query factory will be used.
     * @param catalogName name of the catalog to check query factory for
     * @return true if the query factory for this catalog is present.
     */
    public abstract boolean isQueryFactoryAvailable(String catalogName);
}
/*
$Log: FactoryProvider.java,v $
Revision 1.5  2003/08/28 15:30:32  nw
minor fixes to javadoc

Revision 1.4  2003/08/25 21:46:38  mch
Removed VOTable-middleman classes (to replace with more general ResultSet)

Revision 1.3  2003/08/25 20:59:57  mch
Removed dummy MySpace-related classes

Revision 1.2  2003/08/21 12:27:03  nw
renamed a method to make it more meaningful

Revision 1.1  2003/08/20 14:50:25  nw
added interface to a factory management system

*/
