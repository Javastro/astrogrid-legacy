/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import junit.framework.TestCase;

/** Tests my implementtion of equality for message types (hashcode is important)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20099:12:32 PM
 */
public class MessageTypeUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
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
    
  

}
