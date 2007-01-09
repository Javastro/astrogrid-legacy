/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import org.astrogrid.desktop.alternatives.HeadlessUIFactory;
import org.astrogrid.desktop.modules.system.UIImpl;
import org.easymock.MockControl;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20065:25:52 PM
 */
public class UIActionContributionUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		act = new UIActionContribution();
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		act = null;
	}
	protected UIActionContribution act;

	/* too hard to test - means I need to get an instance of UIImpl - which drages everything else in.
	 * @todo provide interface around UIImpl - then can mock successfully.
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.actionPerformed(ActionEvent)'
	 */
	public void dontTestActionPerformed() {
		MockControl rControl = MockControl.createControl(Runnable.class);
		Runnable r = (Runnable)rControl.getMock();
		r.run();
		rControl.replay();
		act.setObject(r);
		act.setMethodName("run");
		//HeadlessUIFactory fac = new HeadlessUIFactory();
		//act.setUIImpl();
	//	UIImpl ui = new UIImpl(
	//	MockControl uiControl = MockControl.createControl(UII
		
		act.actionPerformed(null);
		rControl.verify();

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.addParameter(Object)'
	 */
	public void testAddParameter() {

	}
	public void testSetAfter() {
		assertNull(act.getAfter());
		act.setAfter("test");
		assertEquals("test",act.getAfter());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UITabContribution.setBefore(String)'
	 */
	public void testSetBefore() {
		assertNull(act.getBefore());
		act.setBefore("test");
		assertEquals("test",act.getBefore());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.setConfirmMessage(String)'
	 */
	public void testSetConfirmMessage() {
		assertNull(act.getConfirmMessage());
		act.setConfirmMessage("test");
		assertEquals("test",act.getConfirmMessage());

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.setIconName(String)'
	 */
	public void testSetIconName() {
		assertNull(act.getIcon());
		act.setIconName("collapse.gif");
		assertNotNull(act.getIcon());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.setMethodName(String)'
	 */
	public void testSetMethodName() {
		assertNull(act.getMethodName());
		act.setMethodName("test");
		assertEquals("test",act.getMethodName());

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.setName(String)'
	 */
	public void testSetName() {
		assertNull(act.getName());
		act.setName("test");
		assertEquals("test",act.getName());

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.setObject(Object)'
	 */
	public void testSetObject() {
		assertNull(act.getObject());
		Object o = new Object();
		act.setObject(o);
		assertEquals(o,act.getObject());

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.setParentName(String)'
	 */
	public void testSetParentName() {
		assertNull(act.getParentName());
		act.setParentName("test");
		assertEquals("test",act.getParentName());

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.setText(String)'
	 */
	public void testSetText() {
		assertNull(act.getText());
		act.setText("test");
		assertEquals("test",act.getText());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.setToolTipText(String)'
	 */
	public void testSetToolTipTextString() {
		assertNull(act.getToolTipText());
		act.setToolTipText("test");
		assertEquals("test",act.getToolTipText());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.setUIImpl(UIImpl)'
	 */
	public void testSetUIImpl() {

	}

}
