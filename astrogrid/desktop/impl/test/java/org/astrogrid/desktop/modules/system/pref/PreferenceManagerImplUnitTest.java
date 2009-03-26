/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import junit.framework.TestCase;

import org.apache.hivemind.SymbolSource;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.pref.PreferenceManagerImpl;
import static org.easymock.EasyMock.*;

/**
 * @author Noel Winstanley
 * @since Jan 5, 200712:38:59 AM
 */
public class PreferenceManagerImplUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		prefs = new HashMap();
		pref1 = new Preference();
		pref1.setName("a");
		pref1.setDefaultValue("a");
		pref2 = new Preference();
		pref2.setName("b");
		pref2.setDefaultValue("b");
		root = new ArrayList();
		Class clazz = this.getClass();
		Preferences javaPrefs = Preferences.userNodeForPackage(clazz);
		javaPrefs.removeNode();
		javaPrefs.flush();
		root.add(clazz);
		symbols = createMock(SymbolSource.class);
		
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		prefs = null;
		pref1 = null;
		pref2 = null;
		root = null;
		symbols= null;
		if (pMan != null) { 
			pMan.preferenceRegistry.removeNode(); // clean up any props left.
			pMan = null;
		}
	}
	protected PreferenceManagerImpl pMan;
	protected Preference pref1;
	protected Preference pref2;
	protected Map prefs;
	protected List root;
	protected SymbolSource symbols;
	
	public void testInitialization() throws Exception {
		assertNotNull(pref1);
		assertNotNull(pref2);
		assertNotNull(prefs);
		assertNotNull(root);
		assertNotNull(symbols);
		
	}
	
	// should fail fast if incorrect important params are passed in.
	public void testIncorerectFailsFast() {
		
		try {

			pMan = new PreferenceManagerImpl(prefs,new ArrayList(), symbols);
		fail("expected to chuck");
		} catch (IllegalArgumentException e) {
			// ok
		}
		try {
			List l = new ArrayList();
			l.add(new Object());
			pMan = new PreferenceManagerImpl(prefs,l, symbols);
			fail("expected to chuck");
			} catch (IllegalArgumentException e) {
				// ok
			}		
	}
	
	
	public void testFindNull() throws Exception {
		try {
			pMan = new PreferenceManagerImpl(prefs,root,symbols);			
			Preference candidate = pMan.find(null);
			fail("expected to fail");
		} catch (IllegalArgumentException e) {
			// ok
		}
	}
	public void testFindUnknown() throws Exception {
		try {
			pMan = new PreferenceManagerImpl(prefs,root,symbols);			
			Preference candidate = pMan.find(pref1.getName());
			fail("expected to fail");
		} catch (IllegalArgumentException e) {
			// ok
		}
		
		// as we've set it all up already, just check this last stage here.
		assertNull(pMan.valueForSymbol(pref1.getName()));
	}
	

	public void testFindInSysproperties() throws Exception {
		prefs.put(pref1.getName(), pref1);
		// not populated with a value.
		// so, create expectation that symbol source will be consulted.
		expect(symbols.valueForSymbol(pref1.getName())).andReturn("x");
		replay(symbols);
		
		pMan = new PreferenceManagerImpl(prefs,root,symbols);
		Preference candidate = pMan.find(pref1.getName());
		assertSame(pref1,candidate);
		assertNotNull(candidate.getValue());
		assertEquals("x",candidate.getValue());
		verify(symbols);
		
		// as we've set it all up already, just check this last stage here.		
		assertEquals("x",pMan.valueForSymbol(pref1.getName()));
		assertEquals("x",pMan.provideObject(null, String.class, pref1.getName(), null));
	}
	 
	public void testFindInDefault() throws Exception {
		prefs.put(pref1.getName(), pref1);
		// not populated with a value.
		// so, create expectation that symbol source will be consulted.
		expect(symbols.valueForSymbol(pref1.getName())).andReturn(null);// won't fiond it here.
		replay(symbols);

		pMan = new PreferenceManagerImpl(prefs,root,symbols);		
		Preference candidate = pMan.find(pref1.getName());
		assertSame(pref1,candidate);
		assertNotNull(candidate.getValue());
		assertEquals(candidate.getDefaultValue(),candidate.getValue());
		
		verify(symbols);
		
		// as we've set it all up already, just check this last stage here.		
		assertEquals(candidate.getDefaultValue(),pMan.valueForSymbol(pref1.getName()));		
	}
	
	public void testFindInPreferenceStore() throws Exception {
		prefs.put(pref1.getName(), pref1);
		// not populated with a value.
		
		// so, create expectation that symbol source will be consulted.
	    expect(symbols.valueForSymbol(pref1.getName())).andReturn(null);// won't fiond it here.
		replay(symbols);

		pMan = new PreferenceManagerImpl(prefs,root,symbols);		
		// and place a value waiting in the preferenceStore.
		pMan.setKey(pref1.getName(), "q");
		pause();
		
		Preference candidate = pMan.find(pref1.getName());
		assertSame(pref1,candidate);
		assertNotNull(candidate.getValue());
		assertEquals("q",candidate.getValue());
		
		verify(symbols);
		// as we've set it all up already, just check this last stage here.		
		assertEquals("q",pMan.valueForSymbol(pref1.getName()));		
	}

	// test mapping through to Config
	public void testPreferencesPropagateToConfig() throws Exception {
		// every created, but lets slip in a preference - not looked up yet.
		prefs.put(pref1.getName(), pref1);
		prefs.put(pref2.getName(), pref2);
		pref1.setPropagateToConfig(true);
		
		// verify simple config is empty.
		try {
			SimpleConfig.getProperty(pref1.getName());
			fail("expected to barf");
		} catch (PropertyNotFoundException e) {
			// ok
		}
		try {
			SimpleConfig.getProperty(pref2.getName());
		} catch (PropertyNotFoundException e) {
			// ok
		}
		expect(symbols.valueForSymbol(pref1.getName())).andReturn(null);
        expect(symbols.valueForSymbol(pref2.getName())).andReturn(null);		
		replay(symbols);
		pMan = new PreferenceManagerImpl(prefs,root,symbols);
		
		// verify it contains the propagated prefernece, but not the other one.
		try {
			String val = SimpleConfig.getProperty(pref1.getName());
			assertEquals(pref1.getDefaultValue(),val);
		} catch (PropertyNotFoundException e) {
			fail("property was not propagated to simple config");
		}			
		try {
			SimpleConfig.getProperty(pref2.getName());
		} catch (PropertyNotFoundException e) {
			// ok
		}
	
		// now modify the properties, and test the change to the propagates one 
		//gets mapped through to the config.
		pref1.setValue("x");
		pref2.setValue("y");
		pause();
		// verify it contains the propagated prefernece, but not the other one.
		try {
			String val = SimpleConfig.getProperty(pref1.getName());
			assertEquals("x",val);
		} catch (PropertyNotFoundException e) {
			fail("property was not propagated to simple config");
		}			
		try {
			SimpleConfig.getProperty(pref2.getName());
		} catch (PropertyNotFoundException e) {
			// ok
		}		
		
	}	

	// test mapping between preferences and properties.
	public void testPreferencesPropagateToProperties() throws Exception {
		// every created, but lets slip in a preference - not looked up yet.
		prefs.put(pref1.getName(), pref1);

		expect(symbols.valueForSymbol(pref1.getName())).andReturn("sys");	
		replay(symbols);
		pMan = new PreferenceManagerImpl(prefs,root,symbols);
		
		assertEquals("sys",pref1.getValue()); // value is being grabbed from sys.
		assertEquals(pref1.getValue(),pMan.getKey(pref1.getName()));
	
		// now modify the properties, and test the change to the propagates 
		pref1.setValue("x");
		pause();
		// verify 
		assertEquals(pref1.getValue(),pMan.getKey(pref1.getName()));
		
		// and now vise-versa.
		pMan.setKey(pref1.getName(), "props");
		pause();
		// verify 
		assertEquals(pref1.getValue(),pMan.getKey(pref1.getName()));		
		
		// now try deleting.
		pMan.removeKey(pref1.getName());
		pause();
		// as it's a propetty, not deleted - expect it to have reverted to defaul
		assertEquals(pref1.getDefaultValue(),pref1.getValue());
		assertEquals(pref1.getDefaultValue(),pMan.getKey(pref1.getName()));
	}	

	
	public void testReset() throws Exception {
		prefs.put(pref1.getName(), pref1);
		// not populated with a value.
		
		// so, create expectation that symbol source will be consulted.
		expect(symbols.valueForSymbol(pref1.getName())).andReturn("sys");
		replay(symbols);

		pMan = new PreferenceManagerImpl(prefs,root,symbols);	
		
		Preference candidate = pMan.find(pref1.getName());
		assertSame(pref1,candidate);
		assertNotNull(pref1.getValue());
		assertEquals("sys",pref1.getValue());
		
		// and override.
		pref1.setValue("nu");
		pause();	
		
		// now reset.
		pMan.reset();
		pause();			
		
		// after reset, values get set back to default value - _not_ any value set in sys properties.
		assertEquals(pref1.getDefaultValue(),pref1.getValue()); // should get system value back.
		
		// as we've set it all up already, check same is seen through the lower-level api too		
		assertEquals(pref1.getDefaultValue(),pMan.getKey(pref1.getName()));			
		
		verify(symbols);	
	}
	
	public void testList() throws Exception {
		prefs.put(pref1.getName(), pref1);
		// not populated with a value.
		
		// so, create expectation that symbol source will be consulted.
		expect(symbols.valueForSymbol(pref1.getName())).andReturn("sys");
		replay(symbols);

		pMan = new PreferenceManagerImpl(prefs,root,symbols);	
		
		Preference candidate = pMan.find(pref1.getName());
		assertSame(pref1,candidate);
		assertNotNull(pref1.getValue());
		assertEquals("sys",pref1.getValue());
		
		// add an arbitrary property.
		pMan.setKey("key", "value");
	
		Map m = pMan.list();
		assertNotNull(m);
		assertEquals(2,m.size());
		assertTrue(m.containsKey(pref1.getName()));
		assertEquals(pref1.getValue(),m.get(pref1.getName()));
		assertTrue(m.containsKey("key"));
		assertEquals("value",m.get("key"));
		verify(symbols);
		
		// while we're at it, test list keys too.		
		String[] keys = pMan.listKeys();
		assertEquals(m.keySet(),new HashSet(Arrays.asList(keys)));			
	}	
	
	public void testPreferenceNoChangeOnNulls() throws Exception {
		prefs.put(pref1.getName(), pref1);
		// not populated with a value.
		
		// so, create expectation that symbol source will be consulted.
		expect(symbols.valueForSymbol(pref1.getName())).andReturn("sys");
		replay(symbols);

		pMan = new PreferenceManagerImpl(prefs,root,symbols);	
		
		Preference candidate = pMan.find(pref1.getName());
		assertSame(pref1,candidate);
		assertNotNull(pref1.getValue());
		assertEquals("sys",pref1.getValue());
		assertEquals(pref1.getValue(),pMan.getKey(pref1.getName()));
		
		// and set to null.
		pref1.setValue(null);
		pause();	
		
		// assertNoChange.
		assertEquals("sys",pref1.getValue());
		assertEquals(pref1.getValue(),pMan.getKey(pref1.getName()));
		
		// now try setting property to null.
		pMan.setKey(pref1.getName(), null);
		pause();	
		
		// assertNoChange.
		assertEquals("sys",pref1.getValue());
		assertEquals(pref1.getValue(),pMan.getKey(pref1.getName()));		
			
		verify(symbols);
	}
	
	public void testPropertyUnknowns() throws Exception {

		pMan = new PreferenceManagerImpl(prefs,root,symbols);	
		String u = pMan.getKey("unknown");
		assertNull(u);
		
		pMan.removeKey("unknown"); // nothing should happen.
	
	}

	/**
	 * @throws InterruptedException
	 */
	private void pause() throws InterruptedException {
		Thread.currentThread().yield(); // as notifcation happens in other thread
		Thread.currentThread().sleep(100);
	}
	

	
	
}
