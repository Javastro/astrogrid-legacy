/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.ParamHttpInterface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.contracts.StandardIds;


/** Tests parsing of Table Service resources.
 * @todo add another tests with an example which has a testQuery.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:51:20 PM
 */
public class TimeServiceParserUnitTest extends AbstractTestForParser {
    public void testStap1() throws Exception {
        ResourceStreamParser p = parse("sohoStap.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://org.astrogrid.regtest/stap/vso/mdi"
                , "VSO-MDI"
                , "SOHO MDI (Michelson Doppler Imager) solar dataset"
                , "Service");
        
        Service s = validateService(r);
        assertEmpty(s.getRights());
        assertEquals(1,s.getCapabilities().length);
        
        assertTrue(" not an instanceof stap service",  s instanceof StapService);
        StapService stap =(StapService)s;

        
        assertSame(s.getCapabilities()[0],stap.findStapCapability());
        Capability cap = s.getCapabilities()[0];
        checkCapability(cap,StandardIds.STAP_1_0,"SimpleTimeAccess",1);
        assertTrue(cap instanceof StapCapability);
        
        StapCapability scap = (StapCapability)cap;
        assertEquals(0,scap.getMaxRecords());
        assertEquals("supported formats",new String[]{"fits"},scap.getSupportedFormats());
        assertFalse(scap.isSupportPositioning());
        assertNull(scap.getTestQuery());
        
        Interface i = cap.getInterfaces()[0];
        checkInterface(i,null,"vs:ParamHTTP",null,new SecurityMethod[0], new AccessURL[] {
                new AccessURL() {{
                    setValueURI(new URI("http://msslxv.mssl.ucl.ac.uk:8080/solarsearch/SolarSearch?service=vso&version=1.0&PROVIDER=SDAC&INSTRUMENT_ID=MDI&"));
                    setUse("full");
                }}
        });
        assertTrue(i instanceof ParamHttpInterface);
        WebTester wt = basicResourceRendererTests(s);
        wt.assertTextPresent("Time");
        wt.assertTextPresent(scap.getSupportedFormats()[0]);
        wt.assertTextPresent("" + scap.isSupportPositioning());
        
    }

}
