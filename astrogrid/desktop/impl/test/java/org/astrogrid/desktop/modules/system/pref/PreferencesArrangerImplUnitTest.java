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

	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
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
		p.setModuleName("General");
		l.add(p);
		
		Preference p1 = new Preference();
		p1.setName("b");
		p1.setModuleName("System");
		l.add(p1);
		
		Preference p2 = new Preference();
		p2.setName("a");
		p2.setModuleName("General");
		l.add(p2);
		
		Preference p3 = new Preference();
		p3.setName("adv");
		p3.setModuleName("Network");
		p3.setAdvanced(true);
		l.add(p3);
		
		PreferencesArranger pai = new PreferencesArrangerImpl(l) ;
		
		// check module names
		l = pai.listPreferenceCategories();
		assertNotNull(l);
		assertEquals(3,l.size());
		assertEquals("System",l.get(0));
		assertEquals("General",l.get(1));
		assertEquals("Network",l.get(2));
		
		// check each module in turn.
		l = pai.listBasicPreferencesForCategory("System");
		assertNotNull(l);
		assertEquals(1,l.size());
		assertSame(p1,l.get(0));
		
		l = pai.listAdvancedPreferencesForCategory("System");
		assertNotNull(l);
		assertEquals(0,l.size());
		
		//
		l = pai.listBasicPreferencesForCategory("General");
		assertNotNull(l);
		assertEquals(2,l.size());
		assertSame(p,l.get(0));  // preferences are unsorted within a module - looks better.
		assertSame(p2,l.get(1));
		
		l = pai.listAdvancedPreferencesForCategory("General");
		assertNotNull(l);
		assertEquals(0,l.size());

		//
		l = pai.listBasicPreferencesForCategory("Network");
		assertNotNull(l);
		assertEquals(0,l.size());		

		
		l = pai.listAdvancedPreferencesForCategory("Network");
		assertNotNull(l);
		assertEquals(1,l.size());
		assertSame(p3,l.get(0));
		
	}

}
