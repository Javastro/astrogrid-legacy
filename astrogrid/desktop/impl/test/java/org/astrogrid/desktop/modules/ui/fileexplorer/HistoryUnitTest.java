/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.EmptyStackException;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.ui.fileexplorer.History;
import org.astrogrid.desktop.modules.ui.fileexplorer.History.BoundedUniqueEventStack;
import org.astrogrid.desktop.modules.ui.fileexplorer.History.HistoryListener;
import static org.easymock.EasyMock.*;

import ca.odell.glazedlists.EventList;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 28, 20076:37:12 PM
 */
public class HistoryUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		h = new History();
		s = new History.BoundedUniqueEventStack(3);
	}
	History h;
	BoundedUniqueEventStack s;
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// tests for stack
	public void testPopEmptyStack() throws Exception {
		try {
			s.pop();
			fail("expected to chuck");
		} catch (EmptyStackException e) {
			// expected
		}	
	}
	
	public void testPeekEmptyStack() throws Exception {
		try {
			s.peek();
			fail("expected to chuck");
		} catch (EmptyStackException e) {
			// expected
		}			
	}
	
	public void testStack() throws Exception {
		assertTrue(s.isEmpty());
		
		Object o = new Object();
		s.push(o);
		assertFalse(s.isEmpty());
		assertEquals(o,s.peek());
		
		Object o2 = new Object();
		s.push(o2);
		assertFalse(s.isEmpty());
		assertEquals(o2,s.peek());
		//
		assertEquals(o2,s.pop());
		assertFalse(s.isEmpty());
		assertEquals(o,s.peek());
		assertEquals(o,s.pop());
		
		assertTrue(s.isEmpty());
	}
	
	public void testStackBounds() throws Exception {
		assertEquals(3,s.getMaxSize());
		EventList l = s.getEventList();
		assertTrue(l.isEmpty());
		// list of test data.
		Object[] os = new Object[]{
				new Object()
				,new Object()
				,new Object()
				,new Object()
				,new Object()
				,new Object()
		};
		//verify that we're getting a bounding effect.
		for (int i = 0; i < os.length; i++) {
			s.push(os[i]);
			assertEquals("Index " + i 
					, i < 3 ? i+1 : 3
					,l.size());
		}
		
		// now verify that it's the last items in the test data that are remaining in the stack.
		assertEquals(os[5],s.pop());
		assertEquals(os[4],s.pop());
		assertEquals(os[3],s.pop());
		// and now that it's all popped, is empty.
		assertTrue(s.isEmpty());
	}
	
	public void testStackUnique() throws Exception {
		EventList l = s.getEventList();
		Object o = new Object();
		s.push(o);
		Object o1 = new Object();
		s.push(o1);
		assertEquals(2,l.size());
		assertEquals(o,l.get(0));
		assertEquals(o1,l.get(1));
		
		// test uniqueness.
		s.push(o);
		assertEquals(2,l.size());
		assertEquals(o1,l.get(0));
		assertEquals(o,l.get(1));		
		
	}
	
	// tests for History.
	public void testStartingHistory() throws Exception {
		assertNotNull(h.getNextList());
		assertNotNull(h.getPreviousList());
		assertEquals(0,h.getNextList().size());
		assertEquals(0,h.getPreviousList().size());
		
		assertNull(h.current());
	}
	
	public void testEmptyPrevious() throws Exception {
		try {
			h.movePrevious();
			fail("expected to chuck");
		} catch (IllegalStateException e) {
			// ok
		}
	}
	
	public void testEmptyNext() throws Exception {
		try {
			h.moveNext();
			fail("expected to chuck");
		} catch (IllegalStateException e) {
			// ok
		}
	}	
	
	public void testMovement() throws Exception {
		
		Object o = "a";
		h.move(o);
		assertEquals(o,h.current());
		assertEquals(0,h.getNextList().size());
		assertEquals(1,h.getPreviousList().size());
		
		Object o1 = "b";
		h.move(o1);
		assertEquals(o1,h.current());
		assertEquals(0,h.getNextList().size());
		assertEquals(2,h.getPreviousList().size());
		
		h.movePrevious();
		assertEquals(o,h.current());
		assertEquals(1,h.getNextList().size());
		assertEquals(1,h.getPreviousList().size());
		
		h.moveNext();
		assertEquals(o1,h.current());
		
		h.movePrevious();
		Object o3 = new Object();
		h.move(o3);
		// verify we've lost all future history.
		assertTrue(h.getNextList().isEmpty());
	}

	public void testMoreMovement() throws Exception {
		
		Object o = "a";
		h.move(o);
		assertEquals(o,h.current());
		assertEquals(0,h.getNextList().size());
		assertEquals(1,h.getPreviousList().size());
		
		Object o1 = "b";
		h.move(o1);
		assertEquals(o1,h.current());
		assertEquals(0,h.getNextList().size());
		assertEquals(2,h.getPreviousList().size());
		
	
		h.movePrevious();
		assertEquals(o,h.current());
		assertEquals(1,h.getNextList().size());
		assertEquals(1,h.getPreviousList().size());


		h.moveNext();
		assertEquals(o1,h.current());
		assertEquals(0,h.getNextList().size());
		assertEquals(2,h.getPreviousList().size());

	
	}	
	
	public void testMoveKnownInHistory() throws Exception {
		
		Object o = "a";
		h.move(o);
		assertEquals(o,h.current());
		assertEquals(0,h.getNextList().size());
		assertEquals(1,h.getPreviousList().size());
		
		Object o1 = "b";
		h.move(o1);
		assertEquals(o1,h.current());
		assertEquals(0,h.getNextList().size());
		assertEquals(2,h.getPreviousList().size());

		
		Object o2 = "c";
		h.move(o2);
		assertEquals(o2,h.current());
		assertEquals(0,h.getNextList().size());
		assertEquals(3,h.getPreviousList().size());		


		h.move(o1);// move to something that's in our history. (actuall in the previous list).
		assertEquals(o1,h.current());
		assertEquals(1,h.getNextList().size());
		assertEquals(2,h.getPreviousList().size());

		h.movePrevious();
		// now move to something in the nextList..
		h.move(o1);
		assertEquals(o1,h.current());
		assertEquals(1,h.getNextList().size());
		assertEquals(2,h.getPreviousList().size());	
		
	}

	public void testEvents() throws Exception {
		HistoryListener l =createMock(HistoryListener.class);
		Object o = "a";
		l.currentChanged(new History.HistoryEvent(h,o));
		replay(l);
		h.addHistoryListener(l);
		
		h.move(o);
		assertEquals(o,h.current());
		
		
		// now unregister the listener, and chek we get no more messages.
		h.removeHistoryListener(l);
		Object o1 = "b";
		h.move(o1);
		
		verify(l); // verifies that the listener was only called once.
	}
	
}
