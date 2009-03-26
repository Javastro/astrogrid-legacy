/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.HarvestCapability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SearchCapability;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.WebServiceInterface;
import org.astrogrid.contracts.StandardIds;

/** Tests parsing of  registry service resources.
 * implement:
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:51:20 PM
 */
public class RegistryServiceParserUnitTest extends AbstractTestForParser {


        public void testRegistry1() throws Exception {
            ResourceStreamParser p = parse("exeterRegistry.xml");
            Resource r =assertOnlyOne(p);
            validateResource(r);    
            checkResource(r
                    , "ivo://uk.ac.ex.astro/org.astrogrid.registry.RegistryService"
                    , null
                    , "Registry"
                    , "Registry");
            
            Service s = validateService(r);
            assertEmpty(s.getRights());
            assertEquals(2,s.getCapabilities().length);
            
            assertTrue(" not an instanceof registry service",  s instanceof RegistryService);
            RegistryService reg =(RegistryService)s;
            assertFalse(reg.isFull());
            assertEquals(new String[]{
                    "uk.ac.ex.astro"
                    ,"uk.org.estar"
            },reg.getManagedAuthorities());
            
            assertSame(s.getCapabilities()[0],reg.findHarvestCapability());
            Capability cap = s.getCapabilities()[0];
            checkCapability(cap,StandardIds.REGISTRY_1_0,"Harvest",2);
            assertTrue(cap instanceof HarvestCapability);
            HarvestCapability hCap = (HarvestCapability)cap;
            assertEquals(200,hCap.getMaxRecords());
            
            Interface i = cap.getInterfaces()[0];
            checkInterface(i,"std","vg:OAIHTTP",null,new SecurityMethod[0], new AccessURL[] {
                    new AccessURL() {{
                        setValueURI(new URI("http://registry.astro.ex.ac.uk:8080/registry/OAIHandlerv1_0"));
                    }}
            });
            i = cap.getInterfaces()[1];
            checkInterface(i,"std","vg:OAISOAP",null,new SecurityMethod[0], new AccessURL[] {
                    new AccessURL() {{
                        setValueURI(new URI("http://registry.astro.ex.ac.uk:8080/registry/services/RegistryHarvestv1_0"));
                    }}
            });            
            assertTrue("shoudl be a webservice interface",i instanceof WebServiceInterface);
            
            assertSame(s.getCapabilities()[1],reg.findSearchCapability());
            cap = s.getCapabilities()[1];
            checkCapability(cap,StandardIds.REGISTRY_1_0,"Search",1);
            assertTrue(cap instanceof SearchCapability);
            SearchCapability sCap = (SearchCapability) cap;
            assertEquals("full",sCap.getExtensionSearchSupport());
            assertEquals(200,sCap.getMaxRecords());
            assertEquals(new String[]{"XQuery"},sCap.getOptionalProtocol());
            
            i = cap.getInterfaces()[0];
            checkInterface(i,"std","vr:WebService",null,new SecurityMethod[0], new AccessURL[] {
                    new AccessURL() {{
                        setValueURI(new URI("http://registry.astro.ex.ac.uk:8080/registry/services/RegistryQueryv1_0"));
                    }}
            });            
            assertTrue("shoudl be a webservice interface",i instanceof WebServiceInterface);
             
            WebTester wt = basicResourceRendererTests(s);
            wt.assertTextPresent("Registry");
            wt.assertTextPresent(reg.getManagedAuthorities()[0]);
            wt.assertTextPresent(reg.getManagedAuthorities()[1]);
            wt.assertTextPresent("" + hCap.getMaxRecords());
            wt.assertTextPresent(sCap.getExtensionSearchSupport());
            wt.assertTextPresent(sCap.getOptionalProtocol()[0]);
        }
        

        public void testRegistry2() throws Exception {
            ResourceStreamParser p = parse("heasarcRegistry.xml");
            Resource r =assertOnlyOne(p);
            validateResource(r);    
            checkResource(r
                    , "ivo://nasa.heasarc/heasarc.xml"
                    , "HEASARC"
                    , "Heasarc Publishing Registry"
                    , "Registry");
            
            Service s = validateService(r);
            assertEmpty(s.getRights());
            assertEquals(2,s.getCapabilities().length);
            
            assertTrue(" not an instanceof registry service",  s instanceof RegistryService);
            RegistryService reg =(RegistryService)s;
            assertTrue(reg.isFull());
            assertEquals(new String[]{
                    "authority"
                    ,"nasa.heasarc"
            },reg.getManagedAuthorities());
            
            assertSame(s.getCapabilities()[0],reg.findHarvestCapability());
            Capability cap = s.getCapabilities()[0];
            checkCapability(cap,StandardIds.REGISTRY_1_0,"Harvest",2);
            assertTrue(cap instanceof HarvestCapability);
            HarvestCapability hCap = (HarvestCapability)cap;
            assertEquals(0,hCap.getMaxRecords());
            
            Interface i = cap.getInterfaces()[0];
            checkInterface(i,"std","vg:OAIHTTP","1.0",new SecurityMethod[0], new AccessURL[] {
                    new AccessURL() {{
                        setValueURI(new URI("http://heasarc.gsfc.nasa.gov/cgi-bin/OAI2/XMLFile/nvo/oai.pl"));
                        setUse("base");
                    }}
            });
            i = cap.getInterfaces()[1];
            checkInterface(i,"std","vg:OAIHTTP","0.8",new SecurityMethod[0], new AccessURL[] {
                    new AccessURL() {{
                        setValueURI(new URI("http://heasarc.gsfc.nasa.gov/cgi-bin/OAI2/XMLFile/nvo/oai.pl"));
                        setUse("base");
                    }}
            });
            assertSame(s.getCapabilities()[1],reg.findSearchCapability());
            cap = s.getCapabilities()[1];
            checkCapability(cap,StandardIds.REGISTRY_1_0,"Search",2);
            assertTrue(cap instanceof SearchCapability);
            SearchCapability sCap = (SearchCapability) cap;
            assertEquals("partial",sCap.getExtensionSearchSupport());
            assertEquals("records",0,sCap.getMaxRecords());
            assertNotNull(sCap.getOptionalProtocol());
            assertEquals("optional",new String[0],sCap.getOptionalProtocol());
            
            i = cap.getInterfaces()[0];
            checkInterface(i,"std","vr:WebService","1.0",new SecurityMethod[0], new AccessURL[] {
                    new AccessURL() {{
                        setValueURI(new URI("http://heasarc.gsfc.nasa.gov/cgi-bin/OAI2/XMLFile/nvo/oai.pl"));
                        setUse("base");                        
                    }}
            });            
            assertTrue("shoudl be a webservice interface",i instanceof WebServiceInterface);
            i = cap.getInterfaces()[1];
            checkInterface(i,"std","vr:WebService","0.8",new SecurityMethod[0], new AccessURL[] {
                    new AccessURL() {{
                        setValueURI(new URI("http://heasarc.gsfc.nasa.gov/cgi-bin/OAI2/XMLFile/nvo/oai.pl"));
                        setUse("base");                        
                    }}
            });            
            assertTrue("shoudl be a webservice interface",i instanceof WebServiceInterface);

            WebTester wt = basicResourceRendererTests(s);
            wt.assertTextPresent("Registry");
            wt.assertTextPresent(reg.getManagedAuthorities()[0]);
            wt.assertTextPresent(reg.getManagedAuthorities()[1]);
            wt.assertTextPresent("" + hCap.getMaxRecords());
            wt.assertTextPresent(sCap.getExtensionSearchSupport());
            wt.assertTextPresent("1.0"); // versions
            wt.assertTextPresent("0.8"); // versions.
        }
}
