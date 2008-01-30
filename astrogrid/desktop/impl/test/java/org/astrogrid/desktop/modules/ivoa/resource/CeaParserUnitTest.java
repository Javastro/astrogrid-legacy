/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaServerCapability;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.contracts.StandardIds;

/** unit tests for cea application and server resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20079:25:14 PM
 */
public class CeaParserUnitTest extends AbstractTestForParser {


    public void testCeaApplication1() throws Exception {
        ResourceStreamParser p = parse("crossmatcherCeaApp.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://org.astrogrid/CrossMatcher"
                , "AG Cross Matcher"
                , "AG Cross Matcher"
                , "CeaApplication");       
        
        assertTrue("not a cea application",r instanceof CeaApplication);
        CeaApplication app = (CeaApplication)r;
        assertNull(app.getApplicationKind());
        assertEmpty(app.getApplicationCapabilities());
// interfaces        
        InterfaceBean[] interfaces = app.getInterfaces();
        assertEquals(2,interfaces.length);
        
        InterfaceBean ibean = interfaces[1];
        assertNull(ibean.getDescription());
        assertEquals("simple",ibean.getName());
        
        ParameterReferenceBean[] inputs = ibean.getInputs();
        assertEquals(2,inputs.length);
        ParameterReferenceBean bean = inputs[1];
        assertEquals("matches",bean.getRef());
        assertEquals(0,bean.getMax());
        assertEquals(1,bean.getMin());
        
        ParameterReferenceBean[] outputs = ibean.getOutputs();
        assertEquals(1,outputs.length);
        bean = outputs[0];
        assertNotNull(bean);
        assertEquals("merged_output",bean.getRef());
        assertEquals(1,bean.getMax());
        assertEquals(1,bean.getMin());

// parameters.        
        ParameterBean[] parameters = app.getParameters();
        ParameterBean pb = parameters[1];
        assertNotNull(pb);
        assertEquals("1",pb.getArraysize());
        assertEquals(new String[]{"VOTable"},pb.getDefaultValues());
        assertEquals(pb.getDefaultValues()[0],pb.getDefaultValue()); // deprecated val
        assertNotNull(pb.getDescription());
        assertEquals("output",pb.getId());
        assertEquals(pb.getId(),pb.getName()); // deprecated val
        assertNull(pb.getMimeType());
        assertNull(pb.getOptions());
        assertNull(pb.getSubType());
        assertEquals("text",pb.getType());
        assertNull(pb.getUcd());
        assertNull(pb.getUnit());
        assertNull(pb.getUType());
        assertEquals("output type",pb.getUiName());
        
        WebTester wt = basicResourceRendererTests(app);
        wt.assertTextPresent(ibean.getName());
        // individual parameters aren't listed.
     //   wt.assertTextPresent(pb.getUiName());
     //   wt.assertTextPresent(pb.getDescription());
    }


    public void testCeaApplication2() throws Exception {
        ResourceStreamParser p = parse("merlinCeaApp.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://org.astrogrid/MERLINImager"
                , "MERLIN Imager"
                , "MERLIN Imager"
                , "CeaApplication");       
        
        assertTrue("not a cea application",r instanceof CeaApplication);
        CeaApplication app = (CeaApplication)r;
        assertNull(app.getApplicationKind());
        assertEmpty(app.getApplicationCapabilities());
// interfaces        
        InterfaceBean[] interfaces = app.getInterfaces();
        assertEquals(1,interfaces.length);
        
        InterfaceBean ibean = interfaces[0];
        assertNull(ibean.getDescription());
        assertEquals("full",ibean.getName());
        
        ParameterReferenceBean[] inputs = ibean.getInputs();
        assertEquals(10,inputs.length);
        ParameterReferenceBean bean = inputs[1];
        assertEquals("Dec",bean.getRef());
        assertEquals(1,bean.getMax());
        assertEquals(0,bean.getMin());
        
        ParameterReferenceBean[] outputs = ibean.getOutputs();
        assertEquals(1,outputs.length);
        bean = outputs[0];
        assertNotNull(bean);
        assertEquals("MERLINImages",bean.getRef());
        assertEquals(1,bean.getMax());
        assertEquals(1,bean.getMin());

// parameters.        
        ParameterBean[] parameters = app.getParameters();
        ParameterBean pb = parameters[1];
        assertNotNull(pb);
        assertEquals("1",pb.getArraysize());
        assertEquals(new String[]{"J"},pb.getDefaultValues());
        assertEquals(pb.getDefaultValues()[0],pb.getDefaultValue()); // deprecated val
        assertNotNull(pb.getDescription());
        assertEquals("Equinox",pb.getId());
        assertEquals(pb.getId(),pb.getName()); // deprecated val
        assertNull(pb.getMimeType());
        assertNull(pb.getOptions());
        assertNull(pb.getSubType());
        assertEquals("text",pb.getType());
        assertNull(pb.getUcd());
        assertNull(pb.getUnit());
        assertNull(pb.getUType());
        assertEquals("Equinox",pb.getUiName());
    }
    

    public void testDsaCeaApplication() throws Exception {
        ResourceStreamParser p = parse("dsaCeaApplication.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://wfau.roe.ac.uk/glimpse-dsa/wsa/ceaApplication"
                , null
                , "GLIMPSE (Galactic Legacy Infrared Mid-Plane Survey Extraordinaire): Cea Application"
                , "CeaApplication");       
        
        assertTrue("not a cea application",r instanceof CeaApplication);
        CeaApplication app = (CeaApplication)r;
        assertNull(app.getApplicationKind());
        assertEmpty(app.getApplicationCapabilities());
// interfaces        
        InterfaceBean[] interfaces = app.getInterfaces();
        assertEquals(3,interfaces.length);
        
        InterfaceBean ibean = interfaces[1];
        assertNull(ibean.getDescription());
        assertEquals("ConeSearch",ibean.getName());
        
        ParameterReferenceBean[] inputs = ibean.getInputs();
        assertEquals(5,inputs.length);
        ParameterReferenceBean bean = inputs[1];
        assertEquals("RA",bean.getRef());
        assertEquals(1,bean.getMax()); // default values - not specified in xml
        assertEquals(1,bean.getMin());
        
        ParameterReferenceBean[] outputs = ibean.getOutputs();
        assertEquals(1,outputs.length);
        bean = outputs[0];
        assertNotNull(bean);
        assertEquals("Result",bean.getRef());
        assertEquals(1,bean.getMax());
        assertEquals(1,bean.getMin());

// parameters.        
        ParameterBean[] parameters = app.getParameters();
        ParameterBean pb = parameters[2];
        assertNotNull(pb);
        assertEquals("1",pb.getArraysize());
        assertEquals(new String[]{"VOTABLE"},pb.getDefaultValues());
        assertEquals(pb.getDefaultValues()[0],pb.getDefaultValue()); // deprecated val
        assertNotNull(pb.getDescription());
        assertEquals("Format",pb.getId());
        assertEquals(pb.getId(),pb.getName()); // deprecated val
        assertNull(pb.getMimeType());
        assertEquals(new String[]{
                "VOTABLE","VOTABLE-BINARY","COMMA-SEPARATED","HTML"
        },pb.getOptions());
        assertNull(pb.getSubType());
        assertEquals("text",pb.getType());
        assertNull(pb.getUcd());
        assertNull(pb.getUnit());
        assertNull(pb.getUType());
        assertEquals("Format",pb.getUiName());
        
        
        pb = parameters[4];
        assertNotNull(pb);
        assertEquals("1",pb.getArraysize());
        assertEmpty(pb.getDefaultValues());
        assertNull(pb.getDefaultValue());
        assertNotNull(pb.getDescription());
        assertEquals("RA",pb.getId());
        assertEquals(pb.getId(),pb.getName()); // deprecated val
        assertNull(pb.getMimeType());
        assertNull(pb.getOptions());
        assertNull(pb.getSubType());
        assertEquals("RA",pb.getType());
        assertEquals("POS_RA_MAIN",pb.getUcd());
        assertEquals("deg",pb.getUnit());
        assertNull(pb.getUType());
        assertEquals("RA",pb.getUiName());
    }
    
    public void testDsaCeaServer() throws Exception {
        ResourceStreamParser p = parse("dsaConeCea.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://mssl.ucl.ac.uk_full/mysql-first-5-0"
                , null
                , "MYSQL TEST DSA"
                , "CatalogService");    
        
     // services.
        Service s = validateService(r);
        assertEmpty(s.getRights());
        assertEquals(2,s.getCapabilities().length);
        assertTrue(s instanceof CeaService);
        CeaService cs = (CeaService)s;
        assertSame(s.getCapabilities()[1],cs.findCeaServerCapability());
        
        CeaServerCapability cap = cs.findCeaServerCapability();
        assertNotNull(cap);                
        checkCapability(cap,StandardIds.CEA_1_0,"CeaCapability",1,
                "Access to two applications: general ADQL query, and asynchronous cone-search where relevant/enabled."); // this example doesn't provide a standardID.
        assertEquals(new URI[]{new URI("ivo://agtest.roe.ac.uk/mysql-first-5-0first/ceaApplication")},cap.getManagedApplications());
        Interface i = cap.getInterfaces()[0];
        checkInterface(i,null,"cea:CECInterface",null,new SecurityMethod[0],new AccessURL[]{
                new AccessURL() {{
                    setUse("full");
                    setValueURI(new URI("http://srif112.roe.ac.uk/mysql-first/services/CommonExecutionConnectorService"));
                }}
        });
        
        WebTester wt = basicResourceRendererTests(s);
        wt.assertTextPresent(cap.getDescription());
        wt.assertTextPresent(cap.getManagedApplications()[0].toString());
       
    }
    
    public void testCeaAppServer() throws Exception {
        ResourceStreamParser p = parse("msslCeaAppServer.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://mssl.ucl.ac.uk/MSSLToolCEAAccess"
                , "MSSL-CEA"
                , "MSSL CEA service"
                , "Service");    
        
     // services.
        Service s = validateService(r);
        assertEmpty(s.getRights());
        assertEquals(1,s.getCapabilities().length);
        assertTrue(s instanceof CeaService);
        CeaService cs = (CeaService)s;
        assertSame(s.getCapabilities()[0],cs.findCeaServerCapability());
        
        CeaServerCapability cap = cs.findCeaServerCapability();
        assertNotNull(cap);                
        checkCapability(cap,StandardIds.CEA_1_0,"CeaCapability",1); 
        URI[] uris = cap.getManagedApplications();
        assertEquals(14,uris.length);        
        assertEquals(new URI("ivo://mssl.ucl.ac.uk/WDCDataGet"),uris[5]);
        
        Interface i = cap.getInterfaces()[0];
        checkInterface(i,"std","cea:CECInterface","1.0",new SecurityMethod[0],new AccessURL[]{
                new AccessURL() {{
                    setValueURI(new URI("http://msslxv.mssl.ucl.ac.uk:8080/astrogrid-cea-MSSL/services/CommonExecutionConnectorService"));
                }}
        });
        
        WebTester wt = basicResourceRendererTests(s);
        wt.assertTextPresent(cap.getManagedApplications()[3].toString());
       
        
    }

}
