/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.Component;
import java.awt.event.ActionEvent;

import org.astrogrid.desktop.modules.ui.UIComponent;
import org.easymock.MockControl;


import junit.framework.TestCase;

/** unit test for the functionality in the consumer base class
 * @author Noel Winstanley
 * @since Jun 21, 20063:26:07 PM
 */
public class AbstractPreferredTransferableConsumerUnitTest extends TestCase {

	
	public class TestConsumer extends AbstractSTA {

		boolean val;
		public TestConsumer(boolean val,String name, String description, String icon) {
			super(name, description, icon);
			this.val = val;
		}
		public void actionPerformed(ActionEvent arg0) {
		}

		protected boolean checkApplicability(PreferredTransferable atom) {
			return val;
		}
	};
	protected UIComponent comp;
	
	protected MockControl compControl;
	
	
	protected PreferredTransferable trans;
	protected MockControl transControl;
	
	public void testApplyToApplicable() {
		TestConsumer r = new TestConsumer(true,"name","description",null);
		assertFalse(r.isEnabled());
		assertNull(r.getAtom());
		assertNull(r.getParent());
		
		r.applyTo(trans,comp);
		
		assertTrue(r.isEnabled());
		assertNotNull(r.getAtom());
		assertEquals(trans,r.getAtom());
		assertNotNull(r.getParent());
		assertEquals(comp,r.getParent());
	}
	public void testApplyToInapplicable() {
		TestConsumer r = new TestConsumer(false,"name","description",null);
		assertFalse(r.isEnabled());
		assertNull(r.getAtom());
		
		r.applyTo(trans,comp);
		
		assertFalse(r.isEnabled());
		assertNull(r.getAtom());
		assertNull(r.getParent());
	}
	
	protected void setUp() throws Exception {
		transControl = MockControl.createControl(PreferredTransferable.class);
		trans = (PreferredTransferable)transControl.getMock();
		compControl = MockControl.createControl(UIComponent.class);
		comp = (UIComponent)compControl.getMock();
		}
}
