/*
 * $Id: DummyQuerier.java,v 1.11 2003/09/22 16:52:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.util.Date;

import org.w3c.dom.Element;

/**
 * DummyQuerier.java
 *
 * @author M Hill
 */

public class DummyQuerier extends DatabaseQuerier
{
   public DummyQuerier(Element givenDOM) throws IOException
   {
      super(givenDOM);
   }

   public QueryResults queryDatabase(Query query ) throws DatabaseAccessException
   {
      Date today = new Date();
      return new DummyQueryResults("Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
   }

   /** Publicicise set error status for test purposes */
   public void setErrorStatus(Throwable th)
   {
      super.setErrorStatus(th);
   }




}

