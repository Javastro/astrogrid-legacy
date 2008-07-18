/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.easymock.EasyMock.createMock;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.ivoa.AdqlInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.util.DomHelper;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

/** Unit test for enhancement 2253
 * 
 * <blockquote>
 * There's a slight snag with using the struct form of the tooldoc when calling CEA apps through the WB: what do you do about multiple parameters with the same name?  Of course, you can work around this by using the XML form, but I wonder if we can find another way, since using a struct is so much more convenient.  One idea would be to allow a struct of the form
{"param1" :  "singlevalued", "param2" : ["value1", "value2"...]}
 * </blockquote>
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 16, 200812:58:17 PM
 */
public class DocumentToStructureConversionUnitTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        // although implemented in DocuemntToStructureConversion, we'll test the public exposure of this in applications.
        app = new ApplicationsImpl(
                createMock(RemoteProcessManager.class)
                ,createMock(FileSystemManager.class)
                ,createMock(RegistryInternal.class)
                ,createMock(AdqlInternal.class)
                );
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        app = null;
    }
    
    ApplicationsImpl app;
    
    public void testSerializeNoParamsDoc() throws Exception {
        Tool t = new Tool();
        t.setInterface("interface");
        t.setName("name");
        assertTrue(t.isValid());
        Document doc = DomHelper.newDocument();
        Marshaller.marshal(t,doc);
        
        
        Map m = app.convertDocumentToStruct(doc);
        Document doc1 = app.convertStructToDocument(m);
        assertXMLEqual(doc,doc1);
        Tool t1 = (Tool)Unmarshaller.unmarshal(Tool.class,doc1);
        assertTrue(t1.isValid());
        assertEquals(t.getInterface(),t1.getInterface());
        assertEquals(t.getName(),t1.getName());
        assertNull(t1.getInput());
        assertNull(t1.getOutput());
        
    }
    
    public void testSerializeEmptyParamsDoc() throws Exception {
        Tool t = new Tool();
        t.setInterface("interface");
        t.setName("name");
        t.setInput(new Input());
        t.setOutput(new Output());
        assertTrue(t.isValid());
        Document doc = DomHelper.newDocument();
        Marshaller.marshal(t,doc);
        
        
        Map m = app.convertDocumentToStruct(doc);
        Document doc1 = app.convertStructToDocument(m);
        assertXMLEqual(doc,doc1);
        Tool t1 = (Tool)Unmarshaller.unmarshal(Tool.class,doc1);
        assertTrue(t1.isValid());
        assertEquals(t.getInterface(),t1.getInterface());
        assertEquals(t.getName(),t1.getName());
        assertNotNull(t1.getInput());
        assertEquals(0,t1.getInput().getParameterCount());
        assertNotNull(t1.getOutput());
        assertEquals(0,t1.getOutput().getParameterCount());
    }
    
    public void testSerializeSimpleParamsDoc() throws Exception {
        Tool t = new Tool();
        t.setInterface("interface");
        t.setName("name");
        final Input input = new Input();
        t.setInput(input);
        
        ParameterValue pv = new ParameterValue();
        pv.setName("p1");
        pv.setValue("val");
        pv.setIndirect(false);
        
        input.addParameter(pv);
        t.setOutput(new Output());
        assertTrue(t.isValid());
        Document doc = DomHelper.newDocument();
        Marshaller.marshal(t,doc);
        
        
        Map m = app.convertDocumentToStruct(doc);
        Document doc1 = app.convertStructToDocument(m);
        assertXMLEqual(new DetailedDiff(new Diff(doc,doc1)),true);
        Tool t1 = (Tool)Unmarshaller.unmarshal(Tool.class,doc1);
        assertTrue(t1.isValid());
        assertEquals(t.getInterface(),t1.getInterface());
        assertEquals(t.getName(),t1.getName());
        assertNotNull(t1.getInput());
        assertEquals(1,t1.getInput().getParameterCount());
        ParameterValue parameter = t1.getInput().getParameter(0);
        assertEquals(pv.getName(),parameter.getName());
        assertEquals(pv.getValue(),parameter.getValue());
        assertEquals(pv.getIndirect(),parameter.getIndirect());
        assertNotNull(t1.getOutput());
        assertEquals(0,t1.getOutput().getParameterCount());        
    }

    /** param isn't valid,so can't even serialize it. */
    public void testSerializePAramWithNoVal() throws Exception {
        Tool t = new Tool();
        t.setInterface("interface");
        t.setName("name");
        final Input input = new Input();
        t.setInput(input);
        
        ParameterValue pv = new ParameterValue();
        pv.setName("p1");
        
        input.addParameter(pv);
        t.setOutput(new Output());
        assertFalse(t.isValid());
        
    }    
    
    /** param isn't valid, so we don't get any further - can;t be serialized. */
    public void testSerializePAramWithNoName() throws Exception {
        Tool t = new Tool();
        t.setInterface("interface");
        t.setName("name");
        final Input input = new Input();
        t.setInput(input);
        
        ParameterValue pv = new ParameterValue();
        pv.setValue("val");
        
        input.addParameter(pv);
        t.setOutput(new Output());
        assertFalse(t.isValid());
    }    
    
    

    public void testSerializeMultipleParamsDoc() throws Exception {
        Tool t = new Tool();
        t.setInterface("interface");
        t.setName("name");
        final Input input = new Input();
        t.setInput(input);
        
        ParameterValue pv = new ParameterValue();
        pv.setName("p1");
        pv.setValue("val");
        pv.setIndirect(true);
        
        ParameterValue pv1 = new ParameterValue();
        pv1.setName("p2");
        pv1.setValue("val1");
        pv1.setIndirect(false);
        
        input.addParameter(pv);
        input.addParameter(pv1);
        assertEquals(2,input.getParameterCount());
        
        t.setOutput(new Output());
        assertTrue(t.isValid());
        Document doc = DomHelper.newDocument();
        Marshaller.marshal(t,doc);
                
        Map m = app.convertDocumentToStruct(doc);
        Document doc1 = app.convertStructToDocument(m);
        
     // ordering isn't preserved.  assertXMLEqual(new DetailedDiff(new Diff(doc,doc1)),true);
        Tool t1 = (Tool)Unmarshaller.unmarshal(Tool.class,doc1);
        assertTrue(t1.isValid());
        assertEquals(t.getInterface(),t1.getInterface());
        assertEquals(t.getName(),t1.getName());
        assertNotNull(t1.getInput());
        assertEquals("only a single input parameter returned",t.getInput().getParameterCount(),t1.getInput().getParameterCount());
        // ordering isn't preserved.
        for (int i = 0; i < t1.getInput().getParameterCount(); i++) {
            ParameterValue parameter = t1.getInput().getParameter(i);
            if (parameter.getName().equals("p1")) {
            assertEquals(pv.getName(),parameter.getName());
            assertEquals(pv.getValue(),parameter.getValue());
            assertEquals(pv.getIndirect(),parameter.getIndirect());
            } else {
            assertEquals(pv1.getName(),parameter.getName());
            assertEquals(pv1.getValue(),parameter.getValue());
            assertEquals(pv1.getIndirect(),parameter.getIndirect());
            }
        }
        assertNotNull(t1.getOutput());
        assertEquals(0,t1.getOutput().getParameterCount());              
    }
    // flaw reported by 2253, which needs to be fixed.
    public void testSerializeRepeatedParamsDoc() throws Exception {
        Tool t = new Tool();
        t.setInterface("interface");
        t.setName("name");
        final Input input = new Input();
        t.setInput(input);
        
        ParameterValue pv = new ParameterValue();
        pv.setName("p1");
        pv.setValue("val");
        pv.setIndirect(true);
        
        ParameterValue pv1 = new ParameterValue();
        pv1.setName(pv.getName());
        pv1.setValue("val1");
        pv1.setIndirect(false);
        
        input.addParameter(pv);
        input.addParameter(pv1);
        assertEquals(2,input.getParameterCount());
        
        t.setOutput(new Output());
        assertTrue(t.isValid());
        Document doc = DomHelper.newDocument();
        Marshaller.marshal(t,doc);
                
        Map m = app.convertDocumentToStruct(doc);
        // check structure
        assertTrue(m.containsKey("input"));
        Map inputMap = (Map)m.get("input");
        assertTrue(inputMap.containsKey("p1"));
        Object param = inputMap.get("p1");
        assertTrue("p1 is not list-valued",param instanceof List);
        // check it round-trips back again.
        Document doc1 = app.convertStructToDocument(m);
        
     // ordering isn't preserved.  assertXMLEqual(new DetailedDiff(new Diff(doc,doc1)),true);
        Tool t1 = (Tool)Unmarshaller.unmarshal(Tool.class,doc1);
        assertTrue(t1.isValid());
        assertEquals(t.getInterface(),t1.getInterface());
        assertEquals(t.getName(),t1.getName());
        assertNotNull(t1.getInput());
        assertEquals("only a single input parameter returned",t.getInput().getParameterCount(),t1.getInput().getParameterCount());
        // ordering isn't preserved.
        for (int i = 0; i < t1.getInput().getParameterCount(); i++) {
            ParameterValue parameter = t1.getInput().getParameter(i);
            if (parameter.getValue().equals("val")) {
            assertEquals(pv.getName(),parameter.getName());
            assertEquals(pv.getValue(),parameter.getValue());
            assertEquals(pv.getIndirect(),parameter.getIndirect());
            } else {
            assertEquals(pv1.getName(),parameter.getName());
            assertEquals(pv1.getValue(),parameter.getValue());
            assertEquals(pv1.getIndirect(),parameter.getIndirect());
            }
        }
        assertNotNull(t1.getOutput());
        assertEquals(0,t1.getOutput().getParameterCount());                  
    }
}
