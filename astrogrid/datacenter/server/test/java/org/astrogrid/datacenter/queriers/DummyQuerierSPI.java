/*
 * $Id: DummyQuerierSPI.java,v 1.2 2003/11/27 17:28:09 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.util.Date;

import org.astrogrid.datacenter.axisdataserver.types.QueryHelper;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.datacenter.queriers.spi.BaseQuerierSPI;
import org.astrogrid.datacenter.queriers.spi.IdTranslator;
import org.astrogrid.datacenter.queriers.spi.QuerierSPI;
import org.astrogrid.datacenter.queriers.spi.TranslatorMap;

/**
 * DummyQuerier.java
 *
 * @author M Hill
 */

public class DummyQuerierSPI extends BaseQuerierSPI implements QuerierSPI {




   public QueryResults doQuery( Object o,Class c) throws DatabaseAccessException
   {
      Date today = new Date();
      return new DummyQueryResults("Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
   }

/* (non-Javadoc)
 * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#getPluginInfo()
 */
public String getPluginInfo() {
    return "Dummy Querier";
}

public static Querier createDummyQuerier() throws Exception {
    return new Querier(new DummyQuerierSPI(),QueryHelper.buildMinimalQuery(),null,"handle");
}


    public TranslatorMap getTranslatorMap() {
            return new IdTranslator.DummyTranslatorMap();
    }

}

