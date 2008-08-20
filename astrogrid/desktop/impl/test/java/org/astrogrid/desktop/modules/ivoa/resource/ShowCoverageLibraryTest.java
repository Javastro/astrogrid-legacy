/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.custommonkey.xmlunit.Transform;
/** Unit test of arnold's toy xslt stylesheet.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 20, 200810:45:18 AM
 */
public class ShowCoverageLibraryTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final URL u = this.getClass().getResource("coverage.xslt");
        assertNotNull("can't find stylesheet",u);
        stylesheet = new File(u.toURI());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        stylesheet = null;
    }

    File stylesheet;
    
    public void test2MassAllSky() throws Exception {
        final Transform t = new Transform(
                "<stc:STCResourceProfile xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                " <stc:AstroCoordSystem id=\"irsa.ipac_2MASS-PSC_UTC-ICRS-TOPO\" xlink:href=\"ivo://STClib/CoordSys#UTC-ICRS-TOPO\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"simple\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\"/>\n" + 
                " <stc:AstroCoords coord_system_id=\"irsa.ipac_2MASS-PSC_UTC-ICRS-TOPO\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "  <stc:Position1D xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "   <stc:Size pos_unit=\"arcsec\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "0.14   </stc:Size>\n" + 
                "  </stc:Position1D>\n" + 
                " </stc:AstroCoords>\n" + 
                " <stc:AstroCoordArea coord_system_id=\"irsa.ipac_2MASS-PSC_UTC-ICRS-TOPO\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "  <stc:TimeInterval xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "   <stc:StartTime xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "    <stc:ISOTime xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "1997-06-01T00:00:00.0Z    </stc:ISOTime>\n" + 
                "   </stc:StartTime>\n" + 
                "   <stc:StopTime xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "    <stc:ISOTime xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "2001-02-15T00:00:00.0Z    </stc:ISOTime>\n" + 
                "   </stc:StopTime>\n" + 
                "  </stc:TimeInterval>\n" + 
                "  <stc:AllSky xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\"/>\n" + 
                " </stc:AstroCoordArea>\n" + 
                "</stc:STCResourceProfile>"
                ,stylesheet);
        final String resultString = t.getResultString();
     //   System.err.println(resultString);
        assertSomeOutput(resultString);           
        assertAllSky(resultString);
    }

    /** assert that the output mentions 'all sky'
     * @param resultString
     */
    private void assertAllSky(final String resultString) {
        assertThat(resultString,containsString("AllSky"));
    }
    
    public void testGoodsPartial() throws Exception {
        final Transform t = new Transform(
                "<stc:STCResourceProfile xmlns=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                " <AstroCoordSystem id=\"mast.stsci_siap-cutout_goods.hstUTC-FK5-TOPO\" xlink:href=\"ivo://STClib/CoordSys#UTC-FK5-TOPO\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"simple\" xmlns=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\"/>\n" + 
                " <AstroCoords coord_system_id=\"mast.stsci_siap-cutout_goods.hstUTC-FK5-TOPO\" xmlns=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "  <Position1D>\n" + 
                "   <Size pos_unit=\"arcsec\">\n" + 
                "0.0500000007450581   </Size>\n" + 
                "  </Position1D>\n" + 
                " </AstroCoords>\n" + 
                "</stc:STCResourceProfile>"
                ,stylesheet);
        final String resultString = t.getResultString();
   //     System.err.println(resultString);
        assertSomeOutput(resultString);        
    }

    /** assert that at least something has been produced - by looking for the span tags that everythign is wrapped in.
     * @param resultString
     */
    private void assertSomeOutput(final String resultString) {
        assertThat(resultString,containsString("<span"));
    }
    
    public void testHubbleSiapPreview() throws Exception {
        final Transform t = new Transform(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
                "<stc:STCResourceProfile xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                " <stc:AstroCoordSystem id=\"stecf.euro-vo_siap_hst_preview_UTC-ICRS-TOPO\" xlink:href=\"ivo://STClib/CoordSys#UTC-ICRS-TOPO\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"simple\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\"/>\n" + 
                " <stc:AstroCoordArea coord_system_id=\"stecf.euro-vo_siap_hst_preview_UTC-ICRS-TOPO\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "  <stc:AllSky xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\"/>\n" + 
                " </stc:AstroCoordArea>\n" + 
                "</stc:STCResourceProfile>"
                ,stylesheet);
        final String resultString = t.getResultString();
     //   System.err.println(resultString);
        assertSomeOutput(resultString);           
        assertAllSky(resultString);         
    }
    
    public void testRC3() throws Exception {
        final Transform t = new Transform(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
                "<stc:STCResourceProfile xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                " <stc:AstroCoordSystem id=\"_rc3_UTC-ICRS-TOPO\" xlink:href=\"ivo://STClib/CoordSys#UTC-ICRS-TOPO\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"simple\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\"/>\n" + 
                " <stc:AstroCoords coord_system_id=\"_rc3_UTC-ICRS-TOPO\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "  <stc:Position1D xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "   <stc:Size pos_unit=\"deg\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "0.0833333333333333333   </stc:Size>\n" + 
                "  </stc:Position1D>\n" + 
                " </stc:AstroCoords>\n" + 
                " <stc:AstroCoordArea coord_system_id=\"_rc3_UTC-ICRS-TOPO\" xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\">\n" + 
                "  <stc:AllSky xmlns:stc=\"http://www.ivoa.net/xml/STC/stc-v1.30.xsd\"/>\n" + 
                " </stc:AstroCoordArea>\n" + 
                "</stc:STCResourceProfile>"
                ,stylesheet);
        final String resultString = t.getResultString();
        System.err.println(resultString);
        assertSomeOutput(resultString);           
        assertAllSky(resultString);               
    }
}
