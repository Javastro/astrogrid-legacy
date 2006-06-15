/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers;
import junit.framework.TestCase;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.NullTarget;
import org.astrogrid.tableserver.test.SampleStarsPlugin;

/**
 * test behaviours the querier manager.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class QuerierManagerTest extends TestCase {
   
   Querier s1 = null;
   Querier s2 = null;
   
   /**
    * Constructor for QuerierManagerTest.
    * @param arg0
    */
   public QuerierManagerTest(String arg0) {
      super(arg0);
   }
   
   protected void setUp() throws Exception {
      super.setUp();
      SampleStarsPlugin.initConfig();
   }
   
   protected void tearDown() throws Exception {
      super.tearDown();
      
   }
   public void testHandleUniqueness() throws Exception {
      
      s1 = Querier.makeQuerier(LoginAccount.ANONYMOUS, 
          SimpleQueryMaker.makeTestQuery(
            new ReturnTable(new NullTarget(), ReturnTable.VOTABLE)),
          this);
      assertNotNull(s1);
      s2 = Querier.makeQuerier(LoginAccount.ANONYMOUS, 
          SimpleQueryMaker.makeTestQuery(
            new ReturnTable(new NullTarget(), ReturnTable.VOTABLE)),
          this);
      assertNotNull(s2);
      assertNotSame(s1,s2);
      assertFalse(s1.getId().equals(s2.getId()));
      s1.close();
      s2.close();
   }
   
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(QuerierManagerTest.class);
   }
}
