/*$Id: SQLUtilsTest.java,v 1.1 2004/01/14 13:23:55 nw Exp $
 * Created on 13-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.sql;

import java.io.IOException;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Jan-2004
 *
 */
public class SQLUtilsTest extends TestCase {
   /**
    * Constructor for SQLUtilsTest.
    * @param arg0
    */
   public SQLUtilsTest(String arg0) {
      super(arg0);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(SQLUtilsTest.class);
   }
   public final String SQL = "select * from neep where x < 1";
   public void testToQueryBody() throws IOException {
      Element el = SQLUtils.toQueryBody(SQL);
      assertNotNull(el);
      assertEquals(SQLUtils.SQL_XMLNS,el.getNamespaceURI()); // check it has the namespace attr
      Node n = el.getFirstChild();
      assertNotNull(n);
      assertEquals(SQL,n.getNodeValue());
   }
   
   
}


/* 
$Log: SQLUtilsTest.java,v $
Revision 1.1  2004/01/14 13:23:55  nw
added unit test for SQLUtils
 
*/