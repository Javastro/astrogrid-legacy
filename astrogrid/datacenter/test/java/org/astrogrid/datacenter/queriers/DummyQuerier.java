/*
 * $Id: DummyQuerier.java,v 1.2 2003/08/27 16:33:52 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.util.Date;
import org.astrogrid.datacenter.query.Query;

/**
 * DummyQuerier.java
 *
 * @author M Hill
 */

public class DummyQuerier implements DatabaseQuerier
{
   public QueryResults queryDatabase(Query query) throws DatabaseAccessException
   {
      Date today = new Date();
      return new DummyQueryResults("Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
   }

}

