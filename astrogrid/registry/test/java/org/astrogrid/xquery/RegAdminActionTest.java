package org.astrogrid.registry.xquery;;

import junit.framework.*;

public class RegAdminActionTest extends TestCase {

  public static void main(String args[]) {
   junit.textui.TestRunner.run(RegAdminActionTest.class);
}

  private RegAdminAction regAdminAction = null;

  String pathFileA = "D:\\astrogrid\\xml\\ipsi\\query.xml";
  String pathFileB = "D:\\astrogrid\\xml\\ipsi\\registry.xquery";
  String pathFileC = "D:\\astrogrid\\xml\\ipsi\\registry2.xml";

  public RegAdminActionTest(String name) {
	super(name);
  }

  protected void setUp() throws Exception {
	super.setUp();
	/**@todo verify the constructors*/
	regAdminAction = new RegAdminAction();
  }

  // include test here, basically in a method that pass two or three
  // variables depending of the problem.  Always the fine name is
  // passed, and then the node that want to be deleted, add or update.

  public void testDeleteNode() {
	 // node to be deleted
	 RegAdminAction("D:\\astrogrid\\xml\\ipsi\\query.xml", "masters");

  }

  public void testUpdateNode() {
  // node to be Updated
	  RegAdminAction("D:\\astrogrid\\xml\\ipsi\\query.xml", "masters", "masters1");
  }

  public void testAddNode() {
   // node to be add
	RegAdminAction("D:\\astrogrid\\xml\\ipsi\\query.xml", "masters");
  }


}
