/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.ui.UIComponent;
import static org.easymock.EasyMock.*;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20071:50:14 PM
 */
public class UIComponentBodyguardUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		ucb = new UIComponentBodyguard();
		ui = createMock(UIComponent.class);
	}
UIComponentBodyguard ucb;
UIComponent ui;
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		ucb = null;
		ui = null;
	}

	public void testUninitialized() throws Exception {
		try {
			ucb.get();
			fail("expexted to chuck");
		} catch (IllegalStateException e) {
			// ok.
		}
	}
	public void testSetNull() throws Exception {
		try {
			ucb.set(null);
			fail("expected to chuck");
		} catch (IllegalArgumentException e) {
			// ok
		}
	}
	public void testSet() throws Exception {
		ucb.set(ui);
		assertSame(ui,ucb.get());
		assertSame(ui,ucb.get());
	}
	
	public void testDoubleSet() throws Exception {
		ucb.set(ui);
		try {
			ucb.set(ui);
			fail("expected to chuck");
		} catch (IllegalStateException e) {
			// ok
		}
	}

}
