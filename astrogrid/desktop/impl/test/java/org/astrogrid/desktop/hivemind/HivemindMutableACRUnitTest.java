/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observer;

import junit.framework.TestCase;

import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.desktop.framework.Module;

/** unit test of this class.
 * @author Noel Winstanley
 * @since Jan 5, 20071:48:13 AM
 */
public class HivemindMutableACRUnitTest extends TestCase {

	/**
	 * 
	 */
	private static final String UNKNOWN_COMPONENT = "unknown";

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		services = new HashMap();
		serviceBean = new ServiceBean();
		serviceBean.setId("module.component");
		serviceBean.setInterface(Runnable.class);
		
		service = new Runnable() {

			public void run() {
			}
		};
		
		module = new ServiceBeanUnitTest.MockModule() {
			@Override
            public String getModuleId() {
				return "module";
			}
			@Override
            public Object getService(Class arg0) {
				return service;
			}
			
			@Override
            public Object getService(String arg0, Class arg1) {
				return service;
			}
		};
		
		serviceBean.setModule(module);
		services.put(serviceBean.getId(),serviceBean);
		
		descriptors = new HashMap();
		mDesc = new ModuleDescriptor();
		mDesc.setName("module");
		descriptors.put(mDesc.getName(),mDesc);
		
		cDesc = new ComponentDescriptor();
		cDesc.setName("component");
		cDesc.setInterfaceClass(Runnable.class);
		mDesc.addComponent(cDesc);
		
		removed = new ComponentDescriptor();
		removed.setName(UNKNOWN_COMPONENT);
		removed.setInterfaceClass(Observer.class);
		mDesc.addComponent(removed);
		
		acr= new HivemindMutableACR(services, descriptors);
	}
	protected ServiceBeanUnitTest.MockModule module;
	protected ServiceBean serviceBean;
	protected Runnable service;
	protected HashMap services;
	protected HashMap descriptors;
	protected ModuleDescriptor mDesc;
	protected ComponentDescriptor cDesc;
	protected ComponentDescriptor removed;
	protected HivemindMutableACR acr;

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		removed = null;
		serviceBean = null;
		services = null;
		service = null;
		descriptors = null;
		module = null;
		acr = null;
		mDesc = null;
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.hivemind.HivemindMutableACR#getDescriptors()}.
	 */
	public void testGetDescriptors() {
		assertEquals(descriptors,acr.getDescriptors());

		assertNull(mDesc.getComponent(UNKNOWN_COMPONENT));
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.hivemind.HivemindMutableACR#getModule(java.lang.String)}.
	 */
	public void testGetModule() {
		Module m = acr.getModule(module.getModuleId());
		assertNotNull(m);
		assertSame(mDesc,m.getDescriptor());
	}
	
	public void testGetUnknownModule() {
		assertNull(acr.getModule("unknownmodule"));
	}
	/**
	 * Test method for {@link org.astrogrid.desktop.hivemind.HivemindMutableACR#moduleIterator()}.
	 */
	public void testModuleIterator() {
		Iterator i = acr.moduleIterator();
		assertNotNull(i);
		assertTrue(i.hasNext());
		Object o = i.next();
		assertTrue(o instanceof Module);
		assertFalse(i.hasNext());
	}
	


	/**
	 * Test method for {@link org.astrogrid.desktop.hivemind.HivemindMutableACR#getService(java.lang.Class)}.
	 */
	public void testGetServiceClass() throws Exception {
		Object candidate = acr.getService(Runnable.class);
		assertNotNull(candidate);
		assertSame(service,candidate);
	}
	
	public void testGetInvalidServiceClass() throws Exception {
		try {
			acr.getService(Object.class);
			fail("expected to fail");
		} catch (org.astrogrid.acr.NotFoundException e) {
			// ok
		}
	}
	
	public void testGetUnknownServiceClass() throws Exception {
		try {
			acr.getService(Observer.class);
			fail("expected to fail");
		} catch (org.astrogrid.acr.NotFoundException e) {
			// ok
		}
	}

	public void testGetServiceString() throws Exception {
		Object candidate = acr.getService("module.component");
		assertNotNull(candidate);
		assertSame(service,candidate);
	}
	
	public void testGetUnknownServiceString() throws Exception {
		try {
			acr.getService("unknown.object");
			fail("expected to fail");
		} catch (org.astrogrid.acr.NotFoundException e) {
			// ok
		}
	}
 // test the proxy object we get back..
	
	public void testGetModuleProxy() throws Exception{
		Module m = acr.getModule(module.getModuleId());
		assertNotNull(m);
		// have already tested getting the descriptor - just want to test services now.
		
		Object candidate = m.getComponent(Runnable.class);
		assertNotNull(candidate); 
		assertSame(service,candidate);
		
		candidate = m.getComponent("component");
		assertNotNull(candidate);
		assertSame(service,candidate);

		try {
		candidate = m.getComponent(UNKNOWN_COMPONENT);
		fail("expected to fail");
		} catch (org.astrogrid.acr.NotFoundException e) {
			// ok
		}		
		
		
	}
}
