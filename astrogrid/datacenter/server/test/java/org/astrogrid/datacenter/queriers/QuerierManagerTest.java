/*$Id: QuerierManagerTest.java,v 1.2 2003/12/01 16:44:11 nw Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.axisdataserver.types.QueryHelper;

/**
 * test behaviours the querier manager.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class QuerierManagerTest extends ServerTestCase {

    /**
     * Constructor for QuerierManagerTest.
     * @param arg0
     */
    public QuerierManagerTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(QuerierManagerTest.class);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        SimpleConfig.setProperty(QuerierManager.QUERIER_SPI_KEY,DummyQuerierSPI.class.getName());        
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();

    }
    Querier s1 = null;
    Querier s2 = null;
        
    public void testHandleUniqueness() throws Exception {

        s1 = QuerierManager.createQuerier(QueryHelper.buildMinimalQuery());
         assertNotNull(s1);
         s2 = QuerierManager.createQuerier(QueryHelper.buildMinimalQuery());
         assertNotNull(s2);
         assertNotSame(s1,s2);
         assertFalse(s1.getQueryId().equals(s2.getQueryId()));
         s1.close();
         s2.close();
     }
     
     public void testCollection() throws Exception {
         int startingSize = QuerierManager.getQueriers().size() ;
         s1 = QuerierManager.createQuerier(QueryHelper.buildMinimalQuery());
         String qid = s1.getQueryId();
         assertNotNull(qid);
         assertEquals(startingSize + 1 ,QuerierManager.getQueriers().size());
         //check we can retreive it.
         s2 = QuerierManager.getQuerier(qid);
         assertNotNull(s2);
         assertEquals(qid,s2.getQueryId());
         
         // check it gets removed from store on close - otherwise we'll get an ever-growing heap of old queriers.
         s1.close();         
         assertEquals(startingSize,QuerierManager.getQueriers().size());
         assertNull(QuerierManager.getQuerier(qid));
                  
     }


}


/* 
$Log: QuerierManagerTest.java,v $
Revision 1.2  2003/12/01 16:44:11  nw
dropped _QueryId, back to string

Revision 1.1  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested
 
*/