/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import junit.framework.TestCase;

import org.astrogrid.desktop.hivemind.ServiceBeanUnitTest;
import org.astrogrid.desktop.modules.system.pref.Preference;

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
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#hashCode()}.
	 */
	public void testHashCode() {
		assertTrue(pref.hashCode() > 0);
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#isAdvanced()}.
	 */
	public void testIsAdvanced() {
		assertFalse(pref.isAdvanced());
		pref.setAdvanced(true);
		assertTrue(pref.isAdvanced());
		pref.setAdvanced(false);
		assertFalse(pref.isAdvanced());		
	}

	/**
		 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#getDescription()}.
		 */
		public void testGetDescription() {
			assertNull(pref.getDescription());
			pref.setDescription("foo");
			assertEquals("foo",pref.getDescription());
		}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#getDefaultValue()}.
	 */
	public void testGetDefaultValue() {
		assertNull(pref.getDefaultValue());
		pref.setDefaultValue("foo");
		assertEquals("foo",pref.getDefaultValue());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#getName()}.
	 */
	public void testGetName() {
		assertNull(pref.getName());
		pref.setName("fred");
		assertEquals("fred",pref.getName());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#isRequiresRestart()}.
	 */
	public void testIsRequiresRestart() {
		assertFalse(pref.isRequiresRestart());
		pref.setRequiresRestart(true);
		assertTrue(pref.isRequiresRestart());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#getUiName()}.
	 */
	public void testGetUiName() {
		assertNull(pref.getUiName());
		pref.setUiName("fred");
		assertEquals("fred",pref.getUiName());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#getValue()}.
	 */
	public void testGetValue() {
		assertNull(pref.getValue());
		pref.setValue("fred");
		assertEquals("fred",pref.getValue());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#toString()}.
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
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#equals(java.lang.Object)}.
	 */
	public void testEqualsObject() {
		assertTrue(pref.equals(pref));
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#addPropertyChangeListener(java.beans.PropertyChangeListener)}.
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
	 * Test method for {@link org.astrogrid.desktop.modules.system.pref.Preference#getUnits()}.
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
	
	public void testAsBoolean() throws Exception {
		assertFalse(pref.asBoolean());
		pref.setValue("true");
		assertTrue(pref.asBoolean());
		pref.setValue("boo");
		assertFalse(pref.asBoolean());
		pref.setValue("false");
		assertFalse(pref.asBoolean());
	}
	
	public void testHelpId() throws Exception {
		assertNull(pref.getHelpId());
		pref.setHelpId("fred");
		assertEquals("fred",pref.getHelpId());
	}

}