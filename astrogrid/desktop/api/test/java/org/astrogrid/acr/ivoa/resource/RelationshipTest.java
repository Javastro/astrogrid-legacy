/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:19:05 PM
 */
public class RelationshipTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        rel = new Relationship();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    Relationship rel;
    
    public void testToString() throws Exception {
        assertNotNull(rel.toString());
    }
    
    public void testHashCode() throws Exception {
        rel.hashCode();
    }
    
    public void testEquals() throws Exception {
        assertEquals(rel,rel);
    }
    
    public void testRelatedResources() throws Exception {
        
        final ResourceName[] rns = new ResourceName[] {
                    new ResourceName()
                    ,new ResourceName()
        };
        rel.setRelatedResources(rns);
        assertSame(rns, rel.getRelatedResources());
    }
    
    public void testRelationshipType() throws Exception {
        final String relType = " new reltype";
        rel.setRelationshipType(relType);
        assertEquals(relType,rel.getRelationshipType());
    }

}
