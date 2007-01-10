/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import org.apache.hivemind.internal.Module;
import org.astrogrid.desktop.hivemind.ServiceBeanUnitTest;
import org.astrogrid.desktop.modules.system.Preference;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley
 * @since Jan 5, 200712:15:47 AM
 */
public class PreferenceUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		pref = new Preference();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		pref = null;
	}
	
	Preference pref;

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#hashCode()}.
	 */
	public void testHashCode() {
		assertTrue(pref.hashCode() > 0);
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#isAdvanced()}.
	 */
	public void testIsAdvanced() {
		assertFalse(pref.isAdvanced());
		pref.setAdvanced(true);
		assertTrue(pref.isAdvanced());
		pref.setAdvanced(false);
		assertFalse(pref.isAdvanced());		
	}

	/**
		 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#getDescription()}.
		 */
		public void testGetDescription() {
			assertNull(pref.getDescription());
			pref.setDescription("foo");
			assertEquals("foo",pref.getDescription());
		}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#getDefaultValue()}.
	 */
	public void testGetDefaultValue() {
		assertNull(pref.getDefaultValue());
		pref.setDefaultValue("foo");
		assertEquals("foo",pref.getDefaultValue());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#getName()}.
	 */
	public void testGetName() {
		assertNull(pref.getName());
		pref.setName("fred");
		assertEquals("fred",pref.getName());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#isRequiresRestart()}.
	 */
	public void testIsRequiresRestart() {
		assertFalse(pref.isRequiresRestart());
		pref.setRequiresRestart(true);
		assertTrue(pref.isRequiresRestart());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#getUiName()}.
	 */
	public void testGetUiName() {
		assertNull(pref.getUiName());
		pref.setUiName("fred");
		assertEquals("fred",pref.getUiName());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#getValue()}.
	 */
	public void testGetValue() {
		assertNull(pref.getValue());
		pref.setValue("fred");
		assertEquals("fred",pref.getValue());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#toString()}.
	 */
	public void testToString() {	
		// check it works, even when uninitialized.
		assertNull(pref.toString());
		pref.setDefaultValue("fred");
		assertEquals("fred",pref.toString());
		pref.setValue("barney");
		assertEquals("barney",pref.toString());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#equals(java.lang.Object)}.
	 */
	public void testEqualsObject() {
		assertTrue(pref.equals(pref));
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#addPropertyChangeListener(java.beans.PropertyChangeListener)}.
	 */
	public void testPropertyChangeListener() {
		PropertyChangeListener l = new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
			}
		};
		pref.addPropertyChangeListener(l);
		pref.removePropertyChangeListener(l);
		// should be fine if I remove it twice.
		pref.removePropertyChangeListener(l);
	}



	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.Preference#getUnits()}.
	 */
	public void testGetUnits() {
		assertNull(pref.getUnits());
		pref.setUnits("uni");
		assertEquals("uni",pref.getUnits());
	}
	
	public void testAlternatives() throws Exception {
		String[] alts = pref.getAlternatives();
		assertNotNull(alts);
		assertEquals(0,alts.length);
		pref.addAlternative("foo");
		pref.addAlternative("bar");
		alts = pref.getAlternatives();
		assertTrue(Arrays.equals(new String[] {"foo","bar"}, alts));
	}
	
	public void testOptions() throws Exception {
		String[] opts = pref.getOptions();
		assertNotNull(opts);
		assertEquals(0,opts.length);
		pref.addOption("true");
		pref.addOption("false");
		opts = pref.getOptions();
		assertTrue(Arrays.equals(new String[] {"true","false"}, opts));
	}
	
	public void testModuleName() throws Exception {
		assertNull(pref.getModuleName());
		ServiceBeanUnitTest.MockModule m = new ServiceBeanUnitTest.MockModule();
		pref.setModule(m);
		String s = pref.getModuleName();
		assertNotNull(s);
	}

}
