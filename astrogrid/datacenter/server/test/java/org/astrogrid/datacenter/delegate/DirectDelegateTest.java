/*$Id: DirectDelegateTest.java,v 1.1 2003/12/03 19:37:03 mch Exp $
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
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.queriers.DummyQuerier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 */
public class DirectDelegateTest  extends TestCase {

   public void testFactory() throws ServiceException, MalformedURLException, IOException
   {
      AdqlQuerier querier = DatacenterDelegateFactory.makeAdqlQuerier(Certification.ANONYMOUS, "local", DatacenterDelegateFactory.ASTROGRID_DIRECT);
      
      assertNotNull(querier);
   }

   /**
    * Creates a delegate, passes it a query and checks the return values
    */
   public void testQuery() throws ServiceException, MalformedURLException, IOException, ParserConfigurationException, SAXException, ADQLException
   {
      SimpleConfig.setProperty(QuerierManager.DATABASE_QUERIER_KEY, DummyQuerier.class.getName());
      
      AdqlQuerier delegate = DatacenterDelegateFactory.makeAdqlQuerier(Certification.ANONYMOUS, "local", DatacenterDelegateFactory.ASTROGRID_DIRECT);
      
      assertNotNull(delegate);
      
      //load test query file
      URL url = getClass().getResource("adqlQuery.xml");
      Element adqlQuery = XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      Select adql = ADQLUtils.unmarshalSelect(adqlQuery);
      
      //'submit' query to dummy service for count results
      //int count = delegate.countQuery(adql);

      //submit query for votable results
      DatacenterResults results = delegate.doQuery(AdqlQuerier.VOTABLE, adql);

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
Revision 1.1  2003/12/03 19:37:03  mch
Introduced DirectDelegate, fixed DummyQuerier

*/
