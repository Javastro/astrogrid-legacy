/*
 * $Id: DummyQuerier.java,v 1.1 2003/08/26 16:41:46 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.util.Date;

/**
 * DummyQuerier.java
 *
 * @author M Hill
 */

public class DummyQuerier implements DatabaseQuerier
{
   public QueryResults queryDatabase(org.astrogrid.datacenter.query.Query query)
   {
      Date today = new Date();
      return new DummyQueryResults("Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
   }

}

