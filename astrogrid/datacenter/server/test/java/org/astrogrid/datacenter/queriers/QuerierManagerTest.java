/*$Id: QuerierManagerTest.java,v 1.7 2004/03/12 20:11:09 mch Exp $
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
import java.io.Writer;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.queriers.test.DummySqlPlugin;

/**
 * test behaviours the querier manager.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class QuerierManagerTest extends ServerTestCase {
   
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
      SimpleConfig.setProperty(QuerierPluginFactory.DATABASE_QUERIER_KEY, DummySqlPlugin.class.getName());
   }
   
   protected void tearDown() throws Exception {
      super.tearDown();
      
   }
   public void testHandleUniqueness() throws Exception {
      
      s1 = Querier.makeQuerier(Account.ANONYMOUS, new ConeQuery(30,30,6), (Writer) null, QueryResults.FORMAT_VOTABLE);
      assertNotNull(s1);
      s2 = Querier.makeQuerier(Account.ANONYMOUS, new ConeQuery(30,30,6), (Writer) null, QueryResults.FORMAT_VOTABLE);
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


/*
 $Log: QuerierManagerTest.java,v $
 Revision 1.7  2004/03/12 20:11:09  mch
 It05 Refactor (Client)

 Revision 1.6  2004/03/12 04:54:06  mch
 It05 MCH Refactor

 Revision 1.5  2004/02/24 16:03:48  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.4  2004/02/16 23:07:05  mch
 Moved DummyQueriers to std server and switched to AttomConfig

 Revision 1.3  2003/12/01 20:58:42  mch
 Abstracting coarse-grained plugin

 Revision 1.2  2003/12/01 16:44:11  nw
 dropped _QueryId, back to string

 Revision 1.1  2003/11/28 16:10:30  nw
 finished plugin-rewrite.
 added tests to cover plugin system.
 cleaned up querier & queriermanager. tested
 
 */
