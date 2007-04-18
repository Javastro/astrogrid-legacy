/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import java.util.ArrayList;
import java.util.List;

import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.pref.PreferencesArranger;
import org.astrogrid.desktop.modules.system.pref.PreferencesArrangerImpl;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 26, 200712:58:34 PM
 */
public class PreferencesArrangerImplUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testEmptyList() {
		
		PreferencesArranger pai = new PreferencesArrangerImpl(new ArrayList()) ;
		List l = pai.listPreferenceCategories();
		assertNotNull(l);
		assertEquals(0,l.size());
		
		l = pai.listAdvancedPreferencesForCategory("unknown");
		assertNotNull(l);
		assertEquals(0,l.size());
		
		l = pai.listBasicPreferencesForCategory("unknown");
		assertNotNull(l);
		assertEquals(0,l.size());		
	}
	
	public void testNull() {
		try {
			new PreferencesArrangerImpl(null);
			fail("expected to chuck");
		} catch (IllegalArgumentException e) {
			// ok
		}
	}
	
	public void testSome() {
		List l = new ArrayList();
		Preference p = new Preference();
		p.setName("c");
		p.setModuleName("moduleB");
		l.add(p);
		
		Preference p1 = new Preference();
		p1.setName("b");
		p1.setModuleName("moduleA");
		l.add(p1);
		
		Preference p2 = new Preference();
		p2.setName("a");
		p2.setModuleName("moduleB");
		l.add(p2);
		
		Preference p3 = new Preference();
		p3.setName("adv");
		p3.setModuleName("moduleC");
		p3.setAdvanced(true);
		l.add(p3);
		
		PreferencesArranger pai = new PreferencesArrangerImpl(l) ;
		
		// check module names
		l = pai.listPreferenceCategories();
		assertNotNull(l);
		assertEquals(3,l.size());
		assertEquals("ModuleA",l.get(0));
		assertEquals("ModuleB",l.get(1));
		assertEquals("ModuleC",l.get(2));
		
		// check each module in turn.
		l = pai.listBasicPreferencesForCategory("ModuleA");
		assertNotNull(l);
		assertEquals(1,l.size());
		assertSame(p1,l.get(0));
		
		l = pai.listAdvancedPreferencesForCategory("ModuleA");
		assertNotNull(l);
		assertEquals(0,l.size());
		
		//
		l = pai.listBasicPreferencesForCategory("ModuleB");
		assertNotNull(l);
		assertEquals(2,l.size());
		assertSame(p,l.get(0));  // preferences are unsorted within a module - looks better.
		assertSame(p2,l.get(1));
		
		l = pai.listAdvancedPreferencesForCategory("ModuleB");
		assertNotNull(l);
		assertEquals(0,l.size());

		//
		l = pai.listBasicPreferencesForCategory("ModuleC");
		assertNotNull(l);
		assertEquals(0,l.size());		

		
		l = pai.listAdvancedPreferencesForCategory("ModuleC");
		assertNotNull(l);
		assertEquals(1,l.size());
		assertSame(p3,l.get(0));
		
	}

}
