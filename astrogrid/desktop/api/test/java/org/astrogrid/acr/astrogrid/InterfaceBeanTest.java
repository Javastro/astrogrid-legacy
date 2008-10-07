/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200811:59:49 AM
 */
public class InterfaceBeanTest extends TestCase {

    private String name;
    private String description;
    private ParameterReferenceBean[] inputs;
    private ParameterReferenceBean[] outputs;
    private InterfaceBean ib;

    protected void setUp() throws Exception {
        super.setUp();
        name = "a name";
        description = "a descr";
        inputs = new ParameterReferenceBean[] {
        };
        outputs = new ParameterReferenceBean[] {
        };
        ib=new InterfaceBean(name,description,inputs, outputs);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        name = null;
        description = null;
        inputs = null;
        outputs = null;
        ib = null;
    }
    public void testToString() throws Exception {
        assertNotNull(ib.toString());
    }
    public void testEquals() throws Exception {
        assertEquals(ib,ib);
    }
    
    public void testName() throws Exception {
        assertEquals(name,ib.getName());
    }
    public void testDescr() throws Exception {
        assertEquals(description,ib.getDescription());
    }
    
    public void testInputs() throws Exception {
        assertSame(inputs,ib.getInputs());
    }
    public void testOutputs() throws Exception {
        assertSame(outputs,ib.getOutputs());
    }
    

}
