/*$Id: QuerierSpiContractTest.java,v 1.2 2003/11/28 16:10:30 nw Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.spi;

import junit.framework.TestCase;

import org.astrogrid.datacenter.axisdataserver.types.QueryHelper;
import org.astrogrid.datacenter.axisdataserver.types._QueryId;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.util.Workspace;
import org.w3c.dom.Element;

/** unit test to check contract between Querier and QuerierSPI is obeyed.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Nov-2003
 *
 */
public class QuerierSpiContractTest extends TestCase {

    /**
     * Constructor for TranslatorSpiContractTest.
     * @param arg0
     */
    public QuerierSpiContractTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(QuerierSpiContractTest.class);
    }
    
    protected void setUp() throws Exception {
        workspace = new Workspace(QID.getId());
        query = QueryHelper.buildMinimalQuery();
    }
    
    public void testContract() throws Exception {
        // create a mock SPI, pass to querier, check querier initializes it correctly.
        MockQuerierSPI spi = new MockQuerierSPI();
        Querier querier = new Querier(spi,query,workspace,QID);
        querier.doQuery();
        querier.close();
        // now check mock was initialized correctly
        spi.selfCheck();
    }
    
    public static final _QueryId QID = new _QueryId();
    static {
        QID.setId("handle");
    }
    protected _query query;
    protected Workspace workspace;
    
    
    class MockQuerierSPI implements QuerierSPI {

        // check that everything is initialized as required.
        public void selfCheck() {
            assertEquals(1,seenConf);
            assertEquals(1,seenWorkspace);
            assertEquals(1,seenQuery);
            assertEquals(1,seenClose);
        }
        protected SimpleTranslatorMap trans = new SimpleTranslatorMap();
        { 
            trans.add("http://tempuri.org/adql",new IdTranslator());
        }
        public TranslatorMap getTranslatorMap() {
          return trans;  
        }

        public String getPluginInfo() {
            return "MOCK";
        }

        public void receiveConfig(Config conf) {
          assertNotNull(conf);
          seenConf++;
        }
        
        private int seenConf;
        private int seenWorkspace;
        private int seenQuery;
        private int seenClose;
        public void receiveWorkspace(Workspace ws) {
            assertSame(workspace,ws);
            seenWorkspace++;
            
        }

        public QueryResults doQuery(Object o, Class type) throws Exception {
            assertNotNull(o);
            assertEquals(Element.class,type);
            assertTrue(type.isInstance(o));
            assertEquals(1,seenConf);
            assertEquals(1,seenWorkspace);
            assertEquals(0,seenQuery);
            assertEquals(0,seenClose);
            seenQuery++;
            return null;
        }

        public void close() throws Exception {
            assertEquals(0,seenClose);
            seenClose++;
            
        }
    }

}


/* 
$Log: QuerierSpiContractTest.java,v $
Revision 1.2  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.1  2003/11/27 17:28:09  nw
finished plugin-refactoring
 
*/