/*
 * $Id: DummyQuerier.java,v 1.6 2003/09/07 18:58:58 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.util.Date;
import org.w3c.dom.Element;

/**
 * DummyQuerier.java
 *
 * @author M Hill
 */

public class DummyQuerier extends DatabaseQuerier
{
   public QueryResults queryDatabase(Element soapBody) throws DatabaseAccessException
   {
      Date today = new Date();
      return new DummyQueryResults("Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
   }


}

