/*$Id: QuerierSpiContractTest.java,v 1.7 2004/01/15 17:39:51 nw Exp $
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

import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.axisdataserver.types.QueryHelper;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
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
        query = QueryHelper.buildMinimalQuery();
    }
    
    public void testContract() throws Exception {
        // create a mock SPI, pass to querier, check querier initializes it correctly.
        MockQuerierSPI spi = new MockQuerierSPI();
        Querier querier = new PluginQuerier(spi,QuerierManager.generateQueryId(),query);
        querier.doQuery();
        querier.close();
        // now check mock was initialized correctly
        spi.selfCheck();
    }
    
 
    protected Query query;
    
    
    class MockQuerierSPI implements QuerierSPI {

        // check that everything is initialized as required.
        public void selfCheck() {
            assertEquals(1,seenWorkspace);
            assertEquals(1,seenQuery);
            assertEquals(1,seenClose);
        }
        protected SimpleTranslatorMap trans = new SimpleTranslatorMap();
        {
            trans.add(ADQLUtils.ADQL_XMLNS,new IdTranslator());
        }
        public TranslatorMap getTranslatorMap() {
          return trans;
        }

        public String getPluginInfo() {
            return "MOCK";
        }


        
        private int seenWorkspace;
        private int seenQuery;
        private int seenClose;
        public void setWorkspace(Workspace ws) {
            assertNotNull(ws);
            seenWorkspace++;
            
        }

        public QueryResults doQuery(Object o, Class type) throws Exception {
            assertNotNull(o);
            assertEquals(Element.class,type);
            assertTrue(type.isInstance(o));
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
Revision 1.7  2004/01/15 17:39:51  nw
fixed test.

Revision 1.6  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.5.10.2  2004/01/08 09:43:41  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.5.10.1  2004/01/07 11:51:07  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.5  2003/12/01 20:58:42  mch
Abstracting coarse-grained plugin

Revision 1.4  2003/12/01 16:44:11  nw
dropped QueryId, back to string

Revision 1.3  2003/12/01 16:12:01  nw
removed config

Revision 1.2  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.1  2003/11/27 17:28:09  nw
finished plugin-refactoring
 
*/
