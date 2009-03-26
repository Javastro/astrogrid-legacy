/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.custommonkey.xmlunit.XMLAssert;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import junit.framework.TestCase;

/** test of the parts of castor that we use.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 21, 20089:17:32 AM
 */
public class CastorUnitTest extends TestCase {

    private URL u;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        u = this.getClass().getResource("example-tool.xml");
        assertNotNull(u);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        u = null;
    }
    
    /** test we can parse an existing tool document. */
    public void testToolUnmarshal() throws Exception {
        Tool tool = Tool.unmarshalTool(new InputStreamReader(u.openStream()));
        validateExampleTool(tool);
    }
    public void testUnmarshallerUnmarhsal() throws Exception {
        Object o = Unmarshaller.unmarshal(Tool.class,new InputStreamReader(u.openStream()));
        assertTrue(o instanceof Tool);        
        validateExampleTool((Tool)o);
    }
    
    
    // test we can read in and then write out, and the document is xml-equivalent.
    public void testRoundTripUnmarshaller() throws Exception {
        Object o = Unmarshaller.unmarshal(Tool.class,new InputStreamReader(u.openStream()));
        File f = File.createTempFile(this.getClass().getSimpleName(),"tool");
        Marshaller.marshal(o,new FileWriter(f));
        XMLAssert.assertXMLEqual(
                new InputStreamReader(u.openStream())
                ,new FileReader(f)
                );
    }
    
    
    // test we can read in and then write out, and the document is xml-equivalent.
    public void testRoundTripTool() throws Exception {
        Tool tool = Tool.unmarshalTool(new InputStreamReader(u.openStream()));        
        File f = File.createTempFile(this.getClass().getSimpleName(),"tool");
        tool.marshal(new FileWriter(f));
        XMLAssert.assertXMLEqual(
                new InputStreamReader(u.openStream())
                ,new FileReader(f)
                );
    }
    
    /** test that we can construct a tool object, and serialize to a document. 
     * reconstruct the other tool document, so we can re-use the testing code.
     * */
    public void testConstructAndWriteToolDocument() throws Exception {                
        Tool t = new Tool();
        t.setInterface("SIAP");
        t.setName("uk.ac.cam.ast/INT-WFS/images/CEA-application");
        Input ip = new Input();
        for (int i = 0; i < paramNames.length; i++) {
            ParameterValue pv = new ParameterValue();
            pv.setName(paramNames[i]);
            pv.setValue(paramValues[i]);
            ip.addParameter(pv);
        }
        Output op = new Output();
        ParameterValue pv = new ParameterValue();
        pv.setName("IMAGES");
        pv.setValue("ALL");
        op.addParameter(pv);
        t.setInput(ip);
        t.setOutput(op);
        
        // validate what we've build.
        validateExampleTool(t);
        // write it out and assert it's identical to the example.
        File f = File.createTempFile(this.getClass().getSimpleName(),"tool");
        Marshaller.marshal(t,new FileWriter(f));
        XMLAssert.assertXMLEqual(
                new InputStreamReader(u.openStream())
                ,new FileReader(f)
                );        
        
    }
    
    /**test we can do things with xpath.
     * i.e. verify the BaseBean class.
     * @throws Exception
     */

    public void testXPath() throws Exception {
        Tool tool = Tool.unmarshalTool(new InputStreamReader(u.openStream()));  
        assertNotNull(tool);
        
        assertEquals("1.0",tool.findXPathValue("/input/parameter[name='SIZE']/value"));
   
        Iterator iterator = tool.findXPathIterator("/input/parameter/value");
        assertNotNull(iterator);
        ParameterValue[] parameters = tool.getInput().getParameter();
        for (int i = 0; i < parameters.length; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(parameters[i].getValue(),iterator.next());
        }
        
        assertNotNull(tool.accessJXPathContext());
        
        String xpath = tool.getXPathFor("image/fits");
        assertNotNull(xpath);
        assertEquals("/input/parameter[3]/value",xpath);
    
    }
    
    
    private static final String[] paramNames = new String[]{"POS","SIZE","FORMAT"};
    private static final String[] paramValues = new String[]{"180.0,0.0","1.0","image/fits"};
    private void validateExampleTool(Tool tool) {
        assertNotNull(tool);
        assertEquals("SIAP",tool.getInterface());
        assertEquals("uk.ac.cam.ast/INT-WFS/images/CEA-application",tool.getName());
        final Input input = tool.getInput();
        assertEquals(3,input.getParameterCount());
        for(int i = 0; i < input.getParameterCount(); i++) {
            ParameterValue p = input.getParameter(i);
            assertNotNull(p);
            assertEquals(paramNames[i],p.getName());
            assertEquals(paramValues[i],p.getValue());
            assertFalse(p.getIndirect());
        }
        final Output output = tool.getOutput();
        assertEquals(1,output.getParameterCount());
        ParameterValue p = output.getParameter(0);
        assertNotNull(p);
        assertEquals("IMAGES",p.getName());
        assertEquals("ALL",p.getValue());
        assertFalse(p.getIndirect());        
    }
}
