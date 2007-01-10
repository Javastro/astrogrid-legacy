/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;

import org.astrogrid.desktop.alternatives.HeadlessUIFactory;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIImpl;
import org.easymock.MockControl;

import junit.framework.TestCase;

/** unit tests for the ui action contribution.
 * @author Noel Winstanley
 * @since Jun 6, 20065:25:52 PM
 */
public class UIActionContributionUnitTest extends TestCase implements PropertyChangeListener {

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
		HeadlessUIFactory fac = new HeadlessUIFactory();
		//act.setUIImpl(fac.getUI());
	//	UIImpl ui = new UIImpl(
	//	MockControl uiControl = MockControl.createControl(UII
		
		act.actionPerformed(null);
		rControl.verify();

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIActionContribution.addParameter(Object)'
	 */
	public void testAddParameter() {
		assertNotNull(act.getParameters());
		assertEquals(0,act.getParameters().size());
		act.addParameter("foo");
		act.addParameter("bar");
		assertEquals(2,act.getParameters().size());
		assertEquals("foo",act.getParameters().get(0));
		assertEquals("bar",act.getParameters().get(1));
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
	public void testVisibleConditionPropertyChange() throws Exception {
		assertTrue(act.isVisible());
		Preference p = new Preference();
		p.setValue("false");
		act.setVisibleCondition(p);
		assertSame(p,act.getVisibleCondition());		
		assertFalse(act.isVisible());
		// check visibility updates on preferrnce change.
		act.addPropertyChangeListener(this);
		p.setValue("true");
		assertTrue("doesn't update on preference change",act.isVisible());
		Thread.yield();
		Thread.sleep(200);
		assertTrue(visible); // checks listener interfaces.
	
		// and back again.
		// on non boolean value, goes to invisible.
		p.setValue("blergh");
		assertFalse("should be invisible now",act.isVisible());
		Thread.yield();
		Thread.sleep(200);
		assertFalse(visible);
	}

	
	public void testVisibleConditionParentComponent() throws Exception {
		assertTrue(act.isVisible());
		Preference p = new Preference();
		p.setValue("false");
		act.setVisibleCondition(p);
		assertFalse(act.isVisible());
		
		Component c= new JButton();
		assertTrue(c.isVisible());
		act.setParentComponent(c);
		assertFalse("component visibliy not sycnhe on set",c.isVisible());
		
		p.setValue("true");
		assertTrue("doesn't update on preference change",act.isVisible());
		assertTrue(c.isVisible());
	
		// and back again.
		// on non boolean value, goes to invisible.
		p.setValue("blergh");
		assertFalse("should be invisible now",act.isVisible());
		assertFalse(c.isVisible());
	}
	
	boolean visible;
	public void propertyChange(PropertyChangeEvent evt) {
		assertSame(act,evt.getSource());
		assertEquals(UIActionContribution.VISIBLE_PROPERTY,evt.getPropertyName());
		visible = ((Boolean)evt.getNewValue()).booleanValue();
		
	}

}
