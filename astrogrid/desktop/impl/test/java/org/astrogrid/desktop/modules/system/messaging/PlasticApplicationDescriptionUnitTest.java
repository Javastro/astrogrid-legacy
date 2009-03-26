/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import static org.easymock.EasyMock.createNiceMock;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 24, 20091:25:22 PM
 */
public class PlasticApplicationDescriptionUnitTest extends TestCase {

    private URI id;
    private String name;
    private String description;
    private Set<MessageType<?>> mtypes;
    private ImageIcon icon;
    private TupperwareInternal tupp;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        id = new URI("plastic://id");
        name = "A Name";
        description = "A description";
        mtypes = new HashSet<MessageType<?>>();
        mtypes.add(SpectrumMessageType.instance);
        mtypes.add(FitsImageMessageType.instance);
        icon = new ImageIcon();
        tupp = createNiceMock(TupperwareInternal.class);
        pad = new PlasticApplicationDescription(
                id
                ,name
                ,description
                ,mtypes
                ,icon
                ,tupp
                );
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        id = null;
        pad = null;
        name= null;
        description = null;
        icon = null;
        tupp = null;
    }
    
    PlasticApplicationDescription pad;

    /**
     * Test method for {@link org.astrogrid.desktop.modules.system.messaging.PlasticApplicationDescription#getId()}.
     */
    public void testGetId() {
       assertSame(id,pad.id);
       assertEquals(id.toString(),pad.getId());
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.system.messaging.PlasticApplicationDescription#getName()}.
     */
    public void testGetName() {
       assertEquals(name,pad.getName());
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.system.messaging.PlasticApplicationDescription#getDescription()}.
     */
    public void testGetDescription() {
        assertEquals(description,pad.getDescription());
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.system.messaging.PlasticApplicationDescription#getIcon()}.
     */
    public void testGetIcon() {
        assertSame(icon,pad.getIcon());
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.system.messaging.PlasticApplicationDescription#equals(java.lang.Object)}.
     */
    public void testEqualsObject() {
        assertEquals(pad,pad);
    }
    
    public void testToString() throws Exception {
        assertNotNull(pad.toString());
    }

    
    public void testAccepts() throws Exception {
        
        assertTrue(pad.accepts(SpectrumMessageType.instance));
        assertTrue(pad.accepts(FitsImageMessageType.instance));        
        assertFalse(pad.accepts(VotableMessageType.instance));
        
        assertEquals(mtypes,pad.acceptedMessageTypes());
    }
    
    /**
     * Test method for {@link org.astrogrid.desktop.modules.system.messaging.PlasticApplicationDescription#createMessageSender(org.astrogrid.desktop.modules.system.messaging.MessageType)}.
     */
    public void testCreateMessageSender() {
        assertNotNull(pad.createMessageSender(SpectrumMessageType.instance));
    }
    
    public void testCreateUnsupportedMessageSender() {
       try {
           pad.createMessageSender(VotableMessageType.instance);
           fail("expected to chuck");
       } catch (final UnsupportedOperationException e) {
           // ok. expected'
       }
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.system.messaging.PlasticApplicationDescription#getTupperware()}.
     */
    public void testGetTupperware() {
        assertEquals(tupp,pad.getTupperware());
    }

}
