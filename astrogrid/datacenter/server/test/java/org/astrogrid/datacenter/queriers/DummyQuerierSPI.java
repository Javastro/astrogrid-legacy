/*
 * $Id: DummyQuerierSPI.java,v 1.3 2003/11/28 16:10:30 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.util.Date;

import org.astrogrid.datacenter.axisdataserver.types.QueryHelper;
import org.astrogrid.datacenter.axisdataserver.types._QueryId;
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
    _QueryId qid = new _QueryId();
    qid.setId("Handle");
    return new Querier(new DummyQuerierSPI(),QueryHelper.buildMinimalQuery(),null,qid);
}


    public TranslatorMap getTranslatorMap() {
            return new IdTranslator.DummyTranslatorMap();
    }

}

