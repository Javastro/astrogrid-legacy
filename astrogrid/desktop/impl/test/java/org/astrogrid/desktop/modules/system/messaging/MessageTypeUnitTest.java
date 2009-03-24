/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/** Tests my implementtion of equality for message types (hashcode is important)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20099:12:32 PM
 */
public class MessageTypeUnitTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    final MessageType a = new VotableMessageType();
    final MessageType b = new VotableMessageType();
    final MessageType c = new SpectrumMessageType();
    
    /** equalis should be by class, not by instance */
    public void testEquals() throws Exception {
        assertEquals(a,b);
        assertEquals(b,a);
        assertFalse(a.equals(c));
        assertFalse(c.equals(a));
        assertEquals(a,a);
        assertEquals(b,b);
        assertEquals(c,c);
        assertEquals(a,VotableMessageType.instance);
        assertEquals(b,VotableMessageType.instance);
        assertEquals(c,SpectrumMessageType.instance);
    }
    
    public void testAccepts() throws Exception {
        final Set msgs = new HashSet();
        msgs.add(a);
        msgs.add(c);
        final MessageTarget mt = new AbstractMessageTarget(msgs) {

            public <S extends MessageSender> S createMessageSender(
                    final MessageType<S> type) throws UnsupportedOperationException {
                return null;
            }
        };
        
        assertTrue(mt.accepts(a));
        assertTrue(mt.accepts(VotableMessageType.instance));
    }

}
