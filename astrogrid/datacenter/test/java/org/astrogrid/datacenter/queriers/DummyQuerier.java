/*
 * $Id: DummyQuerier.java,v 1.5 2003/09/05 13:23:49 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.util.Date;
import org.w3c.dom.Node;

/**
 * DummyQuerier.java
 *
 * @author M Hill
 */

public class DummyQuerier extends DatabaseQuerier
{
   public QueryResults queryDatabase(Node n) throws DatabaseAccessException
   {
      Date today = new Date();
      return new DummyQueryResults("Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
   }

/* (non-Javadoc)
 * @see org.astrogrid.datacenter.queriers.DatabaseQuerier#close()
 */
public void close() throws DatabaseAccessException {
   //does nothing
    
}

}

