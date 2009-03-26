/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.contracts.StandardIds;

/** Tests parsing of spectrym services
 * @todo find more examples to test against.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:51:20 PM
 */
public class SpectrumServiceParserUnitTest extends AbstractTestForParser {

	public void testSsap1() throws Exception {
	    ResourceStreamParser p = parse("cielossaSsap.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://esavo/cielossa"
                , "CIELO SSAP"
                , "CIELO-AGN XMM-Newton/RGS spectra"
                , "Service");
        
        Service s = validateService(r);
        assertEmpty(s.getRights());
        assertEquals(1,s.getCapabilities().length);
        
        assertTrue(" not an instanceof ssap service",  s instanceof SsapService);
        SsapService stap =(SsapService)s;

        
        assertSame(s.getCapabilities()[0],stap.findSsapCapability());
        Capability cap = s.getCapabilities()[0];
        checkCapability(cap,StandardIds.SSAP_1_0,"SimpleSpectralAccess",1);
        assertTrue(cap instanceof SsapCapability);
        
        SsapCapability scap = (SsapCapability)cap;
        assertEquals("full",scap.getComplianceLevel());
        assertEquals(new String[]{"cutout"},scap.getCreationTypes());
        assertEquals(new String[]{"pointed"},scap.getDataSources());
        assertEquals(100,scap.getDefaultMaxRecords());
        assertEquals(0.0,scap.getMaxAperture(),0.001);
        assertEquals(2000,scap.getMaxRecords());
        assertEquals(1.1,scap.getMaxSearchRadius(),0.001);
        assertEmpty(scap.getSupportedFrames());
        assertNull(scap.getTestQuery());
        
        Interface i = cap.getInterfaces()[0];
        checkInterface(i,null,"vr:WebBrowser",null,new SecurityMethod[0], new AccessURL[] {
                new AccessURL() {{
                    setValueURI(new URI("http://esavo02.esac.esa.int:8080/cielossaToolKit/cielossa.jsp"));
                    setUse("base");
                }}
        });
        WebTester wt = basicResourceRendererTests(s);
        wt.assertTextPresent("Spectr");
        wt.assertTextPresent(scap.getComplianceLevel());
        wt.assertTextPresent(scap.getCreationTypes()[0]);
        wt.assertTextPresent(scap.getDataSources()[0]);
        wt.assertTextPresent("" + scap.getDefaultMaxRecords());
        wt.assertTextPresent("" + scap.getMaxRecords());
        wt.assertTextPresent("" + scap.getMaxSearchRadius());
        		
	}
	
}
