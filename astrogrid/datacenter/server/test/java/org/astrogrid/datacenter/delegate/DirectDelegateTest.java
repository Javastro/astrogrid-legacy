/*$Id: DirectDelegateTest.java,v 1.4 2004/02/24 16:03:48 mch Exp $
 * Created on 19-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.delegate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.sitedebug.DummyQuerier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 */
public class DirectDelegateTest  extends TestCase {

   public void testFactory() throws ServiceException, MalformedURLException, IOException
   {
      FullSearcher querier = DatacenterDelegateFactory.makeFullSearcher(Account.ANONYMOUS, "local", DatacenterDelegateFactory.ASTROGRID_DIRECT);
      
      assertNotNull(querier);
   }

   /**
    * Creates a delegate, passes it a query and checks the return values
    */
   public void testQuery() throws ServiceException, MalformedURLException, IOException, ParserConfigurationException, SAXException, ADQLException
   {
      SimpleConfig.setProperty(QuerierManager.DATABASE_QUERIER_KEY, DummyQuerier.class.getName());
      
      FullSearcher delegate = DatacenterDelegateFactory.makeFullSearcher(Account.ANONYMOUS, "local", DatacenterDelegateFactory.ASTROGRID_DIRECT);
      
      assertNotNull(delegate);
      
      //load test query file
      URL url = getClass().getResource("adqlQuery.xml");
      Element adqlQuery = XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      Select adql = ADQLUtils.unmarshalSelect(adqlQuery);
      
      //'submit' query to dummy service for count results
      //int count = delegate.countQuery(adql);

      //submit query for votable results
      DatacenterResults results = delegate.doQuery(FullSearcher.VOTABLE,ADQLUtils.toQueryBody(adql));

   }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DirectDelegateTest.class);
    }
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(DirectDelegateTest.class);
    }

}


/*
$Log: DirectDelegateTest.java,v $
Revision 1.4  2004/02/24 16:03:48  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.3  2004/02/16 23:07:04  mch
Moved DummyQueriers to std server and switched to AttomConfig

Revision 1.2  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.1.6.2  2004/01/08 09:43:41  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.1.6.1  2004/01/07 13:02:09  nw
removed Community object, now using User object from common

Revision 1.1  2003/12/03 19:37:03  mch
Introduced DirectDelegate, fixed DummyQuerier

*/
