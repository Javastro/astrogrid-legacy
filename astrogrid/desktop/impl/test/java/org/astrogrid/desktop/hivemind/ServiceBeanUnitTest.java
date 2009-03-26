/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.Location;
import org.apache.hivemind.Messages;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.internal.ServiceModelFactory;
import org.apache.hivemind.internal.ServicePoint;
import org.apache.hivemind.schema.Translator;

/** Simple unit tests for the service bean
 * @author Noel Winstanley
 * @since Jun 6, 20064:09:16 PM
 */
public class ServiceBeanUnitTest extends TestCase {

	/**
	 * @author Noel Winstanley
	 * @since Jan 5, 20071:54:20 AM
	 */
	public static class MockModule implements Module {
		public boolean containsService(Class arg0) {
			return false;
		}

		public String expandSymbols(String arg0, Location arg1) {
			return null;
		}

		public ClassResolver getClassResolver() {
			return null;
		}

		public List getConfiguration(String arg0) {
			return null;
		}

		public Map getConfigurationAsMap(String arg0) {
			return null;
		}

		public ErrorHandler getErrorHandler() {
			return null;
		}

		public Locale getLocale() {
			return null;
		}

		public Messages getMessages() {
			return null;
		}

		public String getModuleId() {
			return "mockModule";
		}

		public Object getService(Class arg0) {
			return null;
		}

		public Object getService(String arg0, Class arg1) {
			return null;
		}

		public ServiceModelFactory getServiceModelFactory(String arg0) {
			return null;
		}

		public ServicePoint getServicePoint(String arg0) {
			return null;
		}

		public Translator getTranslator(String arg0) {
			return null;
		}

		public boolean isConfigurationMappable(String arg0) {
			return false;
		}

		public Class resolveType(String arg0) {
			return null;
		}

		public Location getLocation() {
			return null;
		}

		public String valueForSymbol(String arg0) {
			return null;
		}
	}

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		this.sb = new ServiceBean();
	}
	
	protected ServiceBean sb;

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		sb = null;
	}
	
	public void testId() {
		assertNull(sb.getId());
		sb.setId("test");
		assertEquals("test",sb.getId());
	}
	
	public void testInvalidId() {
		try {
			sb.setId(null);
			fail("expected to fail");
		} catch (IllegalArgumentException e) {
			// expected
		}
		
	}
	
	public void testInterface() {
		assertNull(sb.getInterface());
		sb.setInterface(Runnable.class);
		assertEquals(Runnable.class,sb.getInterface());
	}
	
	public void testNonnterfaceClass() {
		try {
			sb.setInterface(Object.class);
			fail("Expected to fail");
		} catch (IllegalArgumentException e) {
		}
		
	}
	

	
	
	public void testModule() {
		assertNull(sb.getModule());
		Module m = new MockModule();
		sb.setModule(m);
		assertEquals(m,sb.getModule());
	}
	
	public void testInvalidModule() {
		try {
			sb.setModule(null);
			fail("Expected to reject this");
		} catch (IllegalArgumentException e) {
			// ok
		}
	}
	
	public void testToString() {
		assertNotNull(sb.toString());
	}
	
	
}
