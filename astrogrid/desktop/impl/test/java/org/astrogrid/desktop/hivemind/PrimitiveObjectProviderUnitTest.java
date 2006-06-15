/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.LocationImpl;
import org.apache.hivemind.impl.ModuleImpl;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.service.ObjectProvider;
import org.apache.hivemind.util.FileResource;
import org.easymock.MockControl;

import junit.framework.TestCase;

/** test for object provider. 
 * mocks up the hivemind stuff, to prevent dragging in a load of baggage.
 * @author Noel Winstanley
 * @since Jun 6, 20064:22:55 PM
 */
public class PrimitiveObjectProviderUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		p = new PrimitiveObjectProvider();
		moduleControl = MockControl.createControl(Module.class);
		m = (Module)moduleControl.getMock();
		Resource res = new FileResource("/foo/bar/choo");
		loc = new LocationImpl(res);
	}
	
	protected ObjectProvider p;
	protected MockControl moduleControl;
	protected Module m;
	protected Location loc;

	public void testProvideObjectInteger() {
		m.expandSymbols("23",loc);
		moduleControl.setReturnValue("23");
		moduleControl.replay();
		Object o = p.provideObject(m,Integer.class,"23",loc);
		assertNotNull(o);
		assertEquals(new Integer(23),o);
	}
	
	public void testProvideObjectLong() {
		m.expandSymbols("23",loc);
		moduleControl.setReturnValue("23");
		moduleControl.replay();		
		Object o = p.provideObject(m,Long.class,"23",loc);
		assertNotNull(o);
		assertEquals(new Long(23),o);
	}
	
	public void testProvideObjectOther() {
		try {
			m.expandSymbols("object",loc);
			moduleControl.setReturnValue("object");
			moduleControl.replay();			
			p.provideObject(m,Object.class,"object",loc);
			fail("Expected to fail");
		} catch (ApplicationRuntimeException e) {
			// expected;
		}
	}

}
