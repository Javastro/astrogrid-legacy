/*$Id: EgsoQuerierTest.java,v 1.4 2004/08/27 14:32:35 KevinBenson Exp $
 * Created on 01-Dec-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.sec.querier;

import java.io.InputStream;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.returns.TargetIndicator;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.RawSqlQuery;
import org.astrogrid.community.Account;
import java.io.StringWriter;
import org.astrogrid.datacenter.adql.generated.Select;
import java.io.InputStreamReader;


//import org.astrogrid.datacenter.queriers.spi.Translator;
//import org.astrogrid.datacenter.queriers.spi.TranslatorMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.astrogrid.util.DomHelper;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Dec-2003
 *
 */
public class EgsoQuerierTest extends ServerTestCase {

    /**
     * Constructor for VizierQuerierTest.
     * @param arg0
     */
    public EgsoQuerierTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(EgsoQuerierTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

       //DummySqlPlugin.populateDb();
       EgsoSqlPlugin.initConfig();
        // should set configuration and workspace here -- however, not needed by this plugin implementaiton.
    }
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
   public static Test suite() {
         // Reflection is used here to add all the testXXX() methods to the suite.
         return new TestSuite(EgsoQuerierTest.class);
      }

   public void testAdql1() throws Exception {
      askAdqlFromFile("sql-querier-test-5.xml");
   }
   /*
   public void testAdl2() throws Exception {
      askAdqlFromFile("sql-querier-test-3.xml");
   }

   public void testAdl3() throws Exception {
      askAdqlFromFile("sql-querier-test-3.xml");
   }
   */
   /** Read ADQL input document, run query on dummy SQL plugin, and return VOTable document
    *
    * @param queryFile resource file of query
    */
   protected void askAdqlFromFile(String queryFile) throws Exception {
      assertNotNull(queryFile);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      assertNotNull("Could not open query file :" + queryFile,is);
      Select select = Select.unmarshalSelect(new InputStreamReader(is));
      assertNotNull(select);

      StringWriter sw = new StringWriter();
      Querier q = Querier.makeQuerier(Account.ANONYMOUS, new AdqlQuery(select), new TargetIndicator(sw), QueryResults.FORMAT_VOTABLE);

      manager.askQuerier(q);
      
      Document results = DomHelper.newDocument(sw.toString());
      assertIsVotable(results);
   }
   
   QuerierManager manager = new QuerierManager("SqlQuerierTest");

}


/*
$Log: EgsoQuerierTest.java,v $
Revision 1.4  2004/08/27 14:32:35  KevinBenson
class was placed at another package level, which caused a compile bug

Revision 1.3  2004/08/19 17:50:22  mch
Fix for TargetIndicator move

Revision 1.2  2004/07/26 13:50:25  KevinBenson
Small test case that goes out and queries sec based on an adql query

Revision 1.1  2004/07/07 09:17:40  KevinBenson
New SEC/EGSO proxy to query there web service on the Solar Event Catalog

Revision 1.2  2004/03/16 01:32:35  mch
Fixed for cahnges to code to work with new plugins

Revision 1.1  2003/12/01 16:51:04  nw
added tests for cds spi
 
*/
