/*$Id: QuerierManagerTest.java,v 1.2 2005/02/28 18:47:05 mch Exp $
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
import org.astrogrid.account.LoginAccount;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.test.SampleStarsPlugin;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.NullTarget;

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
      SampleStarsPlugin.initConfig();
   }
   
   protected void tearDown() throws Exception {
      super.tearDown();
      
   }
   public void testHandleUniqueness() throws Exception {
      
      s1 = Querier.makeQuerier(LoginAccount.ANONYMOUS, SimpleQueryMaker.makeConeQuery(30,30,6, new ReturnTable(new NullTarget(), ReturnTable.VOTABLE)), this);
      assertNotNull(s1);
      s2 = Querier.makeQuerier(LoginAccount.ANONYMOUS, SimpleQueryMaker.makeConeQuery(30,30,6, new ReturnTable(new NullTarget(), ReturnTable.VOTABLE)), this);
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
 Revision 1.2  2005/02/28 18:47:05  mch
 More compile fixes

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:25  mch
 Initial checkin

 Revision 1.5.2.2  2004/11/29 21:52:18  mch
 Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

 Revision 1.5.2.1  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.5  2004/11/11 20:42:50  mch
 Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

 Revision 1.4  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.3  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.2  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:11:33  mch
 Moved server test directory to pal

 Revision 1.12  2004/09/08 16:12:32  mch
 Fix tests to use ADQL 0.7.4

 Revision 1.11  2004/09/01 13:19:54  mch
 Added sample stars metadata

 Revision 1.10  2004/09/01 12:10:58  mch
 added results.toHtml

 Revision 1.9  2004/03/14 04:13:16  mch
 Wrapped output target in TargetIndicator

 Revision 1.8  2004/03/13 23:38:56  mch
 Test fixes and better front-end JSP access

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
