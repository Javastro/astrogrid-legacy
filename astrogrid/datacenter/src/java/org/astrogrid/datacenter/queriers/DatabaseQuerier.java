/*
 * $Id: DatabaseQuerier.java,v 1.1 2003/08/26 16:40:54 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.query.Query;

/**
 * The interface that all database queriers must implement
 *
 * @author M Hill
 */

public interface DatabaseQuerier
{
   /**
    * Applies the given adql to the database, returning an abstract handle
    * to whatever the results are
    */
   public QueryResults queryDatabase(Query query);

}

