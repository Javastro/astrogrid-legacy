/**
 * 
 */
package org.astrogrid.desktop.modules.system.converters;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/** unit test for the collection convertor
 * @author Noel Winstanley
 * @since Jun 7, 200612:17:07 PM
 */
public class CollectionConvertorUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		this.conv  = new CollectionConvertor();
	}
	protected Converter conv;
	protected final static String sequence = "1,2,3,4";
@Override
protected void tearDown() throws Exception {
	super.tearDown();
	conv = null;
}
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.converters.CollectionConvertor.convert(Class, Object)'
	 */
	public void testConvertList() {
		final Object o = conv.convert(List.class,sequence);
		assertNotNull(o);
		assertTrue(o instanceof List);
		assertEquals(4,((List)o).size());
	}
	
	public void testConvertListEmpty() {
		final Object o = conv.convert(List.class,"");
		assertNotNull(o);
		assertTrue(o instanceof List);
		assertEquals(0,((List)o).size());
	}
	
	public void testConvertListSingleton() {
		final Object o = conv.convert(List.class,"a");
		assertNotNull(o);
		assertTrue(o instanceof List);
		assertEquals(1,((List)o).size());
	}
	
	public void testConvertCollection() {
		final Object o = conv.convert(Collection.class,sequence);
		assertNotNull(o);
		assertTrue(o instanceof Collection);
		assertEquals(4,((Collection)o).size());
	}
	
	public void testConvertSet() {
		final Object o = conv.convert(Set.class,sequence);
		assertNotNull(o);
		assertTrue(o instanceof Set);
		assertEquals(4,((Set)o).size());
	}

	// arbiutrary implementation class.
	public void testConvertTreeSet() {
		final Object o = conv.convert(TreeSet.class,sequence);
		assertNotNull(o);
		assertTrue(o instanceof TreeSet);
		assertEquals(4,((TreeSet)o).size());
	}
	
	public void testConvertNonCollection() {
		try {
			conv.convert(Integer.class,sequence);
			fail("expected to fail");
		} catch (final ConversionException x) {
			//ok
		}
	}

}
