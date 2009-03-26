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


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.easymock.EasyMock.*;
/**
 * @author Noel Winstanley
 * @since Jan 5, 200712:15:47 AM
 */
public class PreferenceUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		pref = new Preference();
	}

	@Override
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
	
	public void testPropagateToConfig() throws Exception {
	    assertFalse(pref.isPropagateToConfig());
	    pref.setPropagateToConfig(true);
	    assertTrue(pref.isPropagateToConfig());
	    pref.setPropagateToConfig(false);
	    assertFalse(pref.isPropagateToConfig());
        
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
	    final String value = "fred";
	    PropertyChangeListener l = createMock(PropertyChangeListener.class);
	    l.propertyChange((PropertyChangeEvent)anyObject());
// setting a value causes event to be fired.	    
	    replay(l);
	    pref.addPropertyChangeListener(l);
		assertNull(pref.getValue());
        pref.setValue(value);
		assertEquals(value,pref.getValue());
		verify(l);
// setting to null has no effect.		
		reset(l);
		replay(l);
		pref.setValue(null); // null mmakes no change.
        assertEquals(value,pref.getValue());
        verify(l);
//setting to same value has no effect - no event is fired.     
        reset(l);
        replay(l);
        pref.setValue(value); 
        assertEquals(value,pref.getValue());
        verify(l);        
	}
	
	public void testPropertyChangeListener() throws Exception {
	    final String value = "fred";
        PropertyChangeListener l = createMock(PropertyChangeListener.class);
         l.propertyChange((PropertyChangeEvent) notNull());
         expectLastCall().times(2);
         
// setting a value causes event to be fired.        
        replay(l);
        pref.addPropertyChangeListener(l);
        assertNull(pref.getValue());
        pref.setValue(value);
        assertEquals(value,pref.getValue());
// test for removing a listener
        pref.removePropertyChangeListener(l);
        pref.setValue("1"); 

     // test for removing a listener that's not listening.
        pref.removePropertyChangeListener(l);
        pref.setValue("2"); // null mmakes no change.        

// can also trigger an event by 'initialize through listener.
        pref.initializeThroughListener(l);
        verify(l);                
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
	
	public void testGetAllAlternatives() throws Exception {
        pref.setValue("foo");
        pref.setDefaultValue("nar");
        assertThat(pref.getAllAlternatives(),equalTo(new String[] {"foo","nar"}));
        pref.addAlternative("foo");
        pref.addAlternative("nar");
        pref.addAlternative("bing");
        assertThat(pref.getAllAlternatives(),equalTo(new String[] {"foo","nar","bing"}));
        
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
		pref.setModuleName("fred");
		assertEquals("fred",pref.getModuleName());
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
